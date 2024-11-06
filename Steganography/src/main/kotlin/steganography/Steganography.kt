package steganography

import java.awt.image.BufferedImage

private val charToInt = mapOf(
    '!' to 0, '.' to 1, '+' to 2, '-' to 3, '?' to 4, '¿' to 5,
    'á' to 6, 'é' to 7, 'í' to 8, 'ó' to 9, 'ú' to 10,
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
    if (length > (image.width * image.height) / 3) {
        throw IllegalStateException("Text is too large for the image")
    }

    var textIndex = 0
    for (y in 0 until image.height) {
        for (x in 0 until image.width step 3) {
            if (textIndex == length) {
                image.setRGB(image.width - 1, image.height - 1, length * 3)
                return image
            }

            if (x + 2 >= image.width) continue

            val pixel1 = image.getRGB(x, y)
            val pixel2 = image.getRGB(x + 1, y)
            val pixel3 = image.getRGB(x + 2, y)

            val charValue = charToInt[text[textIndex]] ?: throw IllegalStateException("Invalid character: ${text[textIndex]}")

            val newPixel1 = modifyPixel(pixel1, (charValue shr 4) and 1, (charValue shr 5) and 1)
            val newPixel2 = modifyPixel(pixel2, (charValue shr 2) and 1, (charValue shr 3) and 1)
            val newPixel3 = modifyPixel(pixel3, charValue and 1, (charValue shr 1) and 1)

            image.setRGB(x, y, newPixel1)
            image.setRGB(x + 1, y, newPixel2)
            image.setRGB(x + 2, y, newPixel3)

            textIndex++
        }
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