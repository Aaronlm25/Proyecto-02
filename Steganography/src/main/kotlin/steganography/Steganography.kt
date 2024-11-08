package steganography

import java.awt.image.BufferedImage
import java.util.Random
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

private val intToChar = charToInt.entries.associate { (key, value) -> value to key }
/**
 * Encodes the text into an image represented by an integer array of the pixels.
 *
 * @param text The text to be encoded.
 * @param image Contains the pixels that make up the image.
 * @return An object of type BufferedImage.
 * @throws IllegalStateException if the text is too large for the pixels array or contains invalid characters.
 */
fun encodeText(text: List<Char>, imageO: BufferedImage): BufferedImage {
    if (text.size * 4 > imageO.width * imageO.height) throw IllegalStateException("El texto es demasiado largo para la imagen.")
    if (!text.all { it in charToInt }) throw IllegalStateException("El texto contiene caracteres invalidos")
    val image = getImage(imageO)
    var textIndex = 0
    val textValues = replaceAlphabet(text).map { it.toString(2).padStart(8, '0')}
    val bits =  textValues.flatMap { it.map { char -> char.toString().toInt() } }
    for (y in 0 until image.height) {
        for (x in 0 until image.width) {
            if (textIndex == bits.size) return image
            val pixel1 = image.getRGB(y, x)
            var alpha1 = (pixel1 shr 24) and 0xff
            var blue1 = pixel1 and 0xff
            val lsbBlue1 = getLSB(blue1)
            val lsbAlpha1 = getLSB(alpha1)
            blue1 = changeColor(bits[textIndex], lsbBlue1, blue1)
            textIndex++
            alpha1 = changeColor(bits[textIndex], lsbAlpha1, alpha1)
            val newPixel1 = (alpha1 shl 24) or (pixel1 and 0x00FFFF00) or blue1
            image.setRGB(y, x, newPixel1)
            textIndex++
        }
    }
    return image
}

/**
 * Change the color of a channel, add or subtract one randomly.
 * 
 * @param bit Bit to be inserted into the channel.
 * @param lsbColor LSB of the current channel.
 * @param colorChannel Current value in the channel.
 * @return Updated channel color
 */
private fun changeColor(bit:Int, lsbColor: Int, colorChannel: Int): Int{
    val random = Random()
    var color = colorChannel
    if (bit != lsbColor) {
        if (color < 255 && color > 0) {
            if (random.nextInt(0,1) == 0) color += 1 else color -= 1
        } else if (color == 255) {
            color -= 1
        } else if (color == 0) {
            color += 1
        }
    }
    return color
}

/**
 * Makes a copy of a BufferedImage object
 * 
 * @param image Image to be copied.
 * @return BufferedImage type object with the values ​​of image.
 */
private fun getImage(image : BufferedImage): BufferedImage {
    val modifiedImage = BufferedImage(image.width, image.height, image.type)
    val graphics = modifiedImage.createGraphics()
    graphics.drawImage(image, 0, 0, null)
    graphics.dispose()
    return modifiedImage
}

/**
 * Get the LSB of a specific channel.
 * 
 * @param channel Channel from which you want to recover the LSB.
 * @return The LSB of the channel.
 */
private fun getLSB(channel: Int): Int {
    return (channel and 1)
}

/**
 * Modifies the LSB of the desired channel.
 * 
 * @param channel The channel to be modified.
 * @param bit The bit to be compared with the channel's LSB.
 * @return The modified channel.
 */
private fun modifyLSB(channel: Int, bit: Int): Int {
    return (channel and 0xFE) or (bit and 1)
}

/**
 * Replaces words in a text with the corresponding integer value according
 * to the alphabet.
 *
 * @param content Text with the words you want to exchange according
 * to the alphabet.
 * @return A list with the integer that corresponds to each word in the text.
 */
private fun replaceAlphabet(text: List<Char>): List<Int>{
    val numbers = mutableListOf<Int>()
    for (char in text) {
        charToInt[char]?.let { numbers.add(it) }
    }
    numbers.add(0)
    return numbers
}
/** 
 * Decodes an image represented by an integer array of the pixels into the 
 * original text.
 *
 * @param image A BufferedImage of the image.
 * @return The decoded text.
 * @throws IllegalStateException if no key is found on the pixels array.
 */
fun decodeText(image: BufferedImage): List<Char> {
    val text = mutableListOf<Char>()
    val bits = StringBuilder()
    
    for (y in 0 until image.height) {
        for (x in 0 until image.width) {
            val rgb = image.getRGB(y, x)
            
            val alphaLSB = (rgb shr 24) and 1
            val blueLSB = rgb and 1

            bits.append(blueLSB)
            bits.append(alphaLSB)
            if (bits.length == 8) {
                val charValue = Integer.parseInt(bits.toString(), 2)
                if (bits.toString() == "00000000"||charValue > 128) {
                    return text
                } else{
                    val char = intToChar[charValue] ?: throw IllegalStateException("$charValue char invalido")
                    text.add(char)
                    bits.clear()
                }
            }
        }
    }
    return text
}