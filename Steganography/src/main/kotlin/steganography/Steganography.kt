package steganography
import java.util.Random
import java.awt.image.BufferedImage
import kotlin.math.floor

private val charToInt = mapOf(
    'a' to 1, 'b' to 2, 'c' to 3, 'd' to 4, 'e' to 5,
    'f' to 6, 'g' to 7, 'h' to 8, 'i' to 9, 'j' to 10,
    'k' to 11, 'l' to 12, 'm' to 13, 'n' to 14, 'o' to 15,
    'p' to 16, 'q' to 17, 'r' to 18, 's' to 19, 't' to 20,
    'u' to 21, 'v' to 22, 'w' to 23, 'x' to 24, 'y' to 25,
    'z' to 26, '?' to 27, '!' to 28, '@' to 29, '(' to 30,
    ')' to 31, ':' to 32, ',' to 33, '.' to 34, '-' to 35,
    '\'' to 36, 'á' to 37, 'é' to 38, 'í' to 39, 'ó' to 40,
    'ú' to 41, '1' to 42, '2' to 43, '3' to 44, '4' to 45,
    '5' to 46, '6' to 47, '7' to 48, '8' to 49, '9' to 50,
    '#' to 51, '$' to 52, '%' to 53, '&' to 54, '[' to 55,
    ']' to 56, '{' to 57, '}' to 58, ' ' to 59, '\n' to 60,
    '0' to 61, '¿' to 62, '+' to 63, '/' to 64, '¡' to 65,
    'A' to 66, 'B' to 67, 'C' to 68, 'D' to 69, 'E' to 70,
    'F' to 71, 'G' to 72, 'H' to 73, 'I' to 74, 'J' to 75,
    'K' to 76, 'L' to 77, 'M' to 78, 'N' to 79, 'O' to 80,
    'P' to 81, 'Q' to 82, 'R' to 83, 'S' to 84, 'T' to 85,
    'U' to 86, 'V' to 87, 'W' to 88, 'X' to 89, 'Y' to 90,
    'Z' to 91, '~' to 92, 'Ñ' to 93, '^' to 94, '_' to 95,
    '`' to 96, '|' to 97, '\\' to 98, 'ñ' to 99, ';' to 100,
    '<' to 101, '=' to 102, '>' to 103, '?' to 104, '¡' to 105,
    '¢' to 106, '£' to 107, '¤' to 108, '¥' to 109, '¦' to 110,
    '§' to 111, '¨' to 112, '©' to 113, 'ª' to 114, '«' to 115,
    '¬' to 116, '­' to 117, '®' to 118, '¯' to 119, '°' to 120,
    '±' to 121, '²' to 122, '³' to 123, '´' to 124, 'µ' to 125,
    '¶' to 126, '·' to 127, '¸' to 128
)

private val intToChar = charToInt.entries.associate { (k, v) -> v to k }

/**
 * Encodes the text into an image represented by an integer array of the pixels.
 *
 * @param text The text to be encoded.
 * @param image Contains the pixels that make up the image.
 * @return An object of type BufferedImage.
 * @throws IllegalStateException if the text is too large for the pixels array.
 */
fun encodeText(text: List<Char>, image: BufferedImage): BufferedImage {
    val length = text.size
    val width = image.width
    val height = image.height
    validateText(text, floor(image.width * image.height / 6.5).toInt() - 10)
    var textIndex = 0
    val modifiedImage = getImage(image)
    var bitIndex = 0
    var charValue = charToInt[text[textIndex]] ?: 0
    for (y in 0 until height) {
        for (x in 1 until width) {
            var pixel = modifiedImage.getRGB(x, y)
            for (i in 0..1) {   
                if (bitIndex == 7) {
                    textIndex++
                    if (textIndex == length + 1) {
                        return modifiedImage
                    }
                    if(textIndex != length) {
                        charValue = charToInt[text[textIndex]] ?: 0
                    } else {
                        charValue = 0
                    }
                    bitIndex = 0
                }
                val (currentChannel, displacement) = getCurrentChannel(pixel, i)
                val newChannel = modifyLSB(currentChannel, (charValue shr 6 - bitIndex) and 1)
                bitIndex++
                pixel = getNewPixel(pixel, Pair(newChannel, displacement))
                modifiedImage.setRGB(x, y, pixel)
            }
        }
    }
    return modifiedImage
}

/**

 * Retrieves the current color channel from a pixel and its corresponding bit displacement.
 * @param pixel The pixel value from which to extract the channel.
 * @param i The index used to determine which channel to extract (0 for red, 1 for green).
 * @return A Pair containing the extracted channel value and its bit displacement.
 * The first element is the channel value (0-255), and the second element is the bit displacement (8 or 16).
 */
private fun getCurrentChannel(pixel: Int, i: Int): Pair<Int, Int> {
    return when (i % 2) {
        0 -> Pair((pixel shr 16) and 0xFF, 16)
        else -> Pair((pixel shr 8) and 0xFF, 8)
    }
}

/**
 * Modifies a pixel by updating a specific color channel with a new value.
 * @param pixel The original pixel value to be modified.
 * @param channel A Pair containing the new channel value and its bit displacement.
 * @return The modified pixel value with the updated channel.
 */
private fun getNewPixel(pixel: Int, channel: Pair<Int, Int>): Int {
    val (newChannel, displacement) = channel
    return (pixel and (0xFF shl displacement).inv()) or (newChannel shl displacement)
}

/**
 * Creates a modified copy of the given image.
 * @param image The original BufferedImage to be copied.
 * @return A new BufferedImage that is a copy of the original image.
 */
private fun getImage(image : BufferedImage): BufferedImage {
    val modifiedImage = BufferedImage(image.width, image.height, image.type)
    val graphics = modifiedImage.createGraphics()
    graphics.drawImage(image, 0, 0, null)
    graphics.dispose()
    return modifiedImage
}

/**
 * Validates the provided text for size and character support.
 * @param text The list of characters to validate.
 * @param maxLength The maximum allowed length for the text.
 * @throws IllegalStateException If the text exceeds maxLength, is empty, or contains unsupported characters.
 */
private fun validateText(text: List<Char>, maxLength : Int) {
    if(text.size >= maxLength) {
        throw IllegalStateException("El texto es muy grande para la imagen.")
    }
    if(text.size == 0) {
        throw IllegalStateException("El texto debe tener por lo menos un caracter.")
    }
    if (!text.all { it in charToInt}){
        throw IllegalStateException("El texto contiene caracteres invalidos.")
    }
}

/**
 * Modifies the LSB of the desired channel.
 * @param channel The channel to be modified.
 * @param bit The bit to be compared with the channel's LSB.
 * @return The modified channel.
 */
private fun modifyLSB(channel: Int, bit: Int): Int {
    return (channel and 0xFE) or (bit and 1)
}

/** 
 * Decodes an image represented by an integer array of the pixels into the 
 * original text.
 * Handles letters (a-z) and (A-Z) numbers (0-9) and some special characters :
 * (!.+-?¿àèìòù¡¿/!)
 *
 * @param image A BufferedImage of the image.
 * @return The decoded text.
 */
fun decodeText(image: BufferedImage): List<Char> {
    val width = image.width
    val height = image.height
    val text = mutableListOf<Char>()
    val bits = StringBuilder()
    for (y in 0 until height) {
        for (x in 1 until width) {
            val pixel = image.getRGB(x, y)
            for(i in 0..1) {
                val channel = getCurrentChannel(pixel, i).first
                bits.append(channel and 1)
                if (bits.length == 7) {
                    val charValue = Integer.parseInt(bits.toString(), 2)
                    val char = intToChar[charValue] ?: '€'
                    if(char == '€') {
                        return text
                    }
                    text.add(char)
                    bits.clear()
                }
            }
        }
    }
    return text
}