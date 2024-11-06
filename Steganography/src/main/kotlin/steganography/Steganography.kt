package steganography

import java.awt.image.BufferedImage

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
    '#' to 51, '$' to 52, '%' to 53, '0' to 54, '[' to 55,
    ']' to 56, '{' to 57, '}' to 58, ' ' to 59, '\n' to 60,
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
    /*
    Mi idea es implementar shuffle para mas placer, pero despues */
    if (text.size * 3 > imageO.width * imageO.height) throw IllegalStateException("El texto es demasiado largo para la imagen.")
    if (!text.all { it in charToInt }) throw IllegalStateException("El texto contiene caracteres invalidos")
    val image = getImage(imageO)
    var textIndex = 0
    //Lista de binarios
    val textValues = replaceAlphabet(text).map { it.toString(2).padStart(6, '0')}
    //println(textValues)
    //Lista de bits
    val bits =  textValues.flatMap { it.map { char -> char.toString().toInt() } }
    for (y in 0 until image.height) {
        for (x in 0 until image.width) {
            if (textIndex == bits.size) {
                return image
            }
            //println(bits[textIndex])
            val pixel1 = image.getRGB(y, x)

            val alpha1 = (pixel1 shr 24) and 0xff
            val blue1 = pixel1 and 0xff
            var lsbBlue1 = getLSB(blue1)
            var lsbAlpha1 = getLSB(alpha1)

            if (!(bits[textIndex] == lsbBlue1)) {
                if (blue1 < 255 && blue1 > 0) {
                    lsbBlue1 = if (lsbBlue1 == 0) blue1 + 1 else blue1 - 1
                } else if (blue1 == 255) {
                    lsbBlue1 = blue1 - 1
                } else if (blue1 == 0) {
                    lsbBlue1 = blue1 + 1
                }
            }
            textIndex++
            if(!((bits[textIndex]) == lsbAlpha1)) {
                if (alpha1 < 255 && alpha1 > 0) {
                    lsbAlpha1 = if (lsbAlpha1 == 0) alpha1 + 1 else alpha1 - 1
                } else if (alpha1 == 255) {
                    lsbAlpha1 = alpha1 - 1
                } else if (alpha1 == 0) {
                    lsbAlpha1 = alpha1 + 1
                }
            }
            val newPixel1 = (lsbAlpha1 shl 24) or (pixel1 and 0x00FFFF00) or lsbBlue1

            image.setRGB(y, x, newPixel1)
            textIndex++
        }
    }
    
    return image
}

/**
 * 
 */
private fun getImage(image : BufferedImage): BufferedImage {
    val modifiedImage = BufferedImage(image.width, image.height, image.type)
    val graphics = modifiedImage.createGraphics()
    graphics.drawImage(image, 0, 0, null)
    graphics.dispose()
    return modifiedImage
}

/**
 * Modifies the pixel by changing the least significant bits.
 * @param pixel The pixel to be modified.
 * @param bit1 The first bit to set.
 * @param bit2 The second bit to set.
 * @return The modified pixel.
 */
private fun modifyPixel(pixel: Int, bit1: Int, bit2: Int): Int {
    val alpha = (pixel shr 24) and 0xff
    val red = (pixel shr 16) and 0xff
    val green = (pixel shr 8) and 0xff
    val blue = pixel and 0xff

    val newRed = (red and 0xFE) or bit1
    val newGreen = (green and 0xFE) or bit2
    return (alpha shl 24) or (newRed shl 16) or (newGreen shl 8) or blue
}
/**
 * 
 */
private fun getLSB(channel: Int): Int {
    return (channel and 1)
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
        charToInt[char.lowercaseChar()]?.let { numbers.add(it) }
    }
    numbers.add(0) //Es analogo a la ide de pablo pero con un numero que en teoria nunca sale
    //print(numbers+"\n")
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
            //println(blueLSB.toString()+"  "+alphaLSB.toString())
            // Verificamos si ya tenemos 6 bits para formar un carácter a reserva de meter mayusculas
            if (bits.length == 6) {
                //println(bits)
                val charValue = Integer.parseInt(bits.toString(), 2)
                //println("Bits: ${bits.toString()}, Char Value: $charValue")
                if (bits.toString() == "000000"||charValue > 60) {
                    return text
                } else{
                    val char = intToChar[charValue] ?: throw IllegalStateException("char invalido")
                    text.add(char)
                    bits.clear()
                }
            }
        }
    }
    return text
}