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
    '#' to 51, '$' to 52, '%' to 53, '&' to 54, '[' to 55,
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
fun encodeText(text: List<Char>, image: BufferedImage): BufferedImage {
    val length = text.size
    var textIndex = 0
    var lastX = 1
    var lastY = 0

    if (length * 3 > image.width * image.height) {
        throw IllegalStateException("El texto es demasiado largo para la imagen.")
    }

    for (y in 0 until image.height) {
        for (x in 1 until image.width step 3) {
            if (textIndex == length) {
                image.setRGB(image.width - 1, image.height - 1, length * 3)
                val goal = charToInt['~'] ?: 0
                image.setRGB(lastX, lastY, goal)
                return image
            }

            if (x + 3 >= image.width) continue

            val charValue = charToInt[text[textIndex]] ?: 0

            val pixel1 = image.getRGB(x, y)
            val pixel2 = image.getRGB(x + 1, y)
            val pixel3 = image.getRGB(x + 2, y)

            val alpha1 = (pixel1 shr 24) and 0xff
            val blue1 = pixel1 and 0xff
            val alpha2 = (pixel2 shr 24) and 0xff
            val blue2 = pixel2 and 0xff
            val alpha3 = (pixel3 shr 24) and 0xff
            val blue3 = pixel3 and 0xff


            val lsbBlue1 = getLSB(blue1)
            val lsbBlue2 = getLSB(blue2)
            val lsbBlue3 = getLSB(blue3)
            val lsbAlpha1 = getLSB(alpha1)
            val lsbAlpha2 = getLSB(alpha2)
            val lsbAlpha3 = getLSB(alpha3)

            var lastBlue1 = blue1
            var lastBlue2 = blue2
            var lastBlue3 = blue3
            var lastAlpha1 = alpha1
            var lastAlpha2 = alpha2
            var lastAlpha3 = alpha3

            if (!(((charValue shr 5) and 1) == lsbBlue1)) {
                if (blue1 + 1 < 255){
                    lastBlue1 = blue1 + 1
                } else {
                    lastBlue1 = blue1 - 1
                }
            }

            if(!(((charValue shr 4) and 1) == lsbAlpha1)) {
                if (alpha1 + 1 < 255){
                    lastAlpha1 = alpha1 + 1
                }else {
                    lastAlpha1 = alpha1 - 1
                }
            }

            if(!(((charValue shr 3) and 1) == lsbBlue2)) {
                if (blue2 + 1 < 255){
                    lastBlue2 = blue2 + 1
                }else {
                    lastBlue2 = blue2 - 1
                }           
            } 

            if(!(((charValue shr 2) and 1) == lsbAlpha2)) {
                if (alpha2 + 1 < 255){
                    lastAlpha2 = alpha2 + 1
                }else {
                    lastAlpha2 = alpha2 - 1
                }
            }
 
            if(!(((charValue shr 1) and 1) == lsbBlue3)) {
                if (blue3 + 1 < 255){
                    lastBlue3 = blue3 + 1 
                }else {
                    lastBlue3 = blue3 - 1
                }           
            }

            if(!((charValue and 1) == lsbAlpha3)) {
                if (alpha3 + 1 < 255){
                    lastAlpha3 = alpha3 + 1
                }else {
                    lastAlpha3 = alpha3 - 1
                }
            }
                        
            val newPixel1 = (lastAlpha1 shl 24) or (pixel1 and 0x00FFFF00) or lastBlue1          
            val newPixel2 = (lastAlpha2 shl 24) or (pixel2 and 0x00FFFF00) or lastBlue2        
            val newPixel3 = (lastAlpha3 shl 24) or (pixel3 and 0x00FFFF00) or lastBlue3

            image.setRGB(x, y, newPixel1)
            image.setRGB(x + 1, y, newPixel2)
            image.setRGB(x + 2, y, newPixel3)

            textIndex++
            lastX++
        }
        lastY++
    }

    image.setRGB(image.width - 1, image.height - 1, length * 3)
    return image
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

private fun getLSB(channel: Int): Int {
    return (channel and 0xFE)
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
    val length = image.getRGB(image.width - 1, image.height - 1) / 3
    val text = mutableListOf<Char>()
    var textIndex = 0

    for (y in 0 until image.height) {
        for (x in 0 until image.width step 3) {
            if (textIndex == length) {
                return text
            }

            if (x + 2 >= image.width) continue

            val pixel1 = image.getRGB(x, y)
            val pixel2 = image.getRGB(x + 1, y)
            val pixel3 = image.getRGB(x + 2, y)

            val charValue = ((pixel1 and 1) shl 5) or ((pixel1 shr 16) and 1) or
                            ((pixel2 and 1) shl 3) or ((pixel2 shr 16) and 1) or
                            ((pixel3 and 1) shl 1) or ((pixel3 shr 16) and 1)

            val char = intToChar[charValue] ?: throw IllegalStateException("Invalid character value: $charValue")
            text.add(char)
            textIndex++
        }
    }

    return text
}