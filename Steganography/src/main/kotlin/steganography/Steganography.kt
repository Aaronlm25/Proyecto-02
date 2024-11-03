package steganography

import steganography.data.text.replaceAlphabet
import java.util.Random
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
    ']' to 56, '{' to 57, '}' to 58, ' ' to 59, '\n' to 60
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
    // Importante hagan los calculos para que se lance la respectiva excepcion
    // cuando el texto es muy largo para la imagen, por cada pixel hay dos bits del char
    // esto es un char esta en cada 3 pixeles
    // ahorita esta el metodo lsb replace (el mas basico)
    // esto es el generador para el lsb matching
    val random = Random(image.getRGB(0, 0).toLong())
    val length = text.size
    var textIndex = 0
    // le dice al decode hasta donde llega el texto
    image.setRGB(image.width - 1, 0, length)
    // no modificamos la semilla pues en x vamos desde el 1
    for(y in 0 until image.height) {
        // considera tres pixeles por caracter
        for (x in 1 until image.width step 3) {
            // cuando se se guarden todos los caracteres se sale 
            // modifcarlo esto es provicional 
            if (textIndex == length) 
                return image
            // consideramos tres pixeles consecuitivos esto se hara igual en lsb matching
            val pixel1 = image.getRGB(x, y)
            val pixel2 = image.getRGB(x + 1, y)
            val pixel3 = image.getRGB(x + 2, y)
            // vemos por la representacion de la letra en nuestro alfabeto
            val charValue = charToInt[text[textIndex]] ?: 0
            // consideramos los 8 bits de cada canal (recordar que los 8 bit representan un numero)
            // del 0 al 255
            val alpha1 = (pixel1 shr 24) and 0xff
            val blue1 = pixel1 and 0xff
            // agregamos el primer bit del caracter en el canal alpha y luego el segundo
            // bit en el azul y asi sucesivamente 
            val newAlpha1 = modifyLSB(alpha1, (charValue shr 5) and 1)
            val newBlue1 = modifyLSB(blue1, (charValue shr 4) and 1)
            // creamos el nuevo pixel con nuestros canales modificados ignorando los demas 
            val newPixel1 = (newAlpha1 shl 24) or (pixel1 and 0x00FFFF00) or newBlue1
            val alpha2 = (pixel2 shr 24) and 0xff
            val blue2 = pixel2 and 0xff
            val newAlpha2 = modifyLSB(alpha2, (charValue shr 3) and 1)
            val newBlue2 = modifyLSB(blue2, (charValue shr 2) and 1)
            val newPixel2 = (newAlpha2 shl 24) or (pixel2 and 0x00FFFF00) or newBlue2
            val alpha3 = (pixel3 shr 24) and 0xff
            val blue3 = pixel3 and 0xff
            val newAlpha3 = modifyLSB(alpha3, (charValue shr 1) and 1)
            val newBlue3 = modifyLSB(blue3, charValue and 1)
            val newPixel3 = (newAlpha3 shl 24) or (pixel3 and 0x00FFFF00) or newBlue3
            // modifica los pixeles en la misma imagen
            image.setRGB(x, y, newPixel1)
            image.setRGB(x + 1, y, newPixel2)
            image.setRGB(x + 2, y, newPixel3)
            textIndex++
        }
    }
    return image
}
/**
 * Modifies the LSB of the desired channel.
 * @param channel The channel to be modified.
 * @param bit The bit to be compared with the channel's LSB.
 * @return The modified channel.
 */
private fun modifyLSB(channel: Int, bit: Int): Int {
    return (0xFFFFFFE or bit) and channel
}

/** 
 * Decodes an image represented by an integer array of the pixels into the 
 * original text.
 * Handles letters (a-z) and (A-Z) numbers (0-9) and some special characters :
 * (!.+-?¿àèìòù¡¿/!)
 *
 * @param image A BufferedImage of the image.
 * @return The decoded text.
 * @throws IllegalStateException if no key is found on the pixels array.
 */
fun decodeText(image: BufferedImage): List<Char> {
    // Implementation
    return listOf()
}

/**
* Auxiliary function to decode the text from the image.
*
* @param image A BufferedImage of the image.
* @return A list with the numerical value of the letters.
 */
private fun decode(image: BufferedImage): List<Int> {
    // Implementation
    return listOf()
}