package steganography

import steganography.data.text.replaceAlphabet
import java.util.Random
import java.awt.image.BufferedImage

/**
 * Encodes the text into an image represented by an integer array of the pixels.
 *
 * @param text The text to be encoded.
 * @param pixels Contains the pixels that make up the image.
 * @return An object of type BufferedImage.
 * @throws IllegalStateException if the text is too large for the pixels array.
 */
fun encodeText(text: List<Char>,pixels: BufferedImage): BufferedImage {
    //width ancho, Columas
    val algorithm = Random(pixels.getRGB(0, 0).toLong())
    val textValues = replaceAlphabet(text) //Lista de enteros
    val binaries = textValues.map { it.toString(2).padStart(6, '0') }
    var current = 0
    val modifiedPixels = mutableSetOf<Pair<Int,Int>>() //Pixeles que se vayan modificando.
    val totalPixels = pixels.width*pixels.height //Numero total de pixeles
    
    for (binary in binaries) { //Los binarios son un Strinf (No me funen).
        for (bitChar in binary.indices step(2)) { //Recorre los chars que forman al binario que es un String de dos en dos.
            if (current >= totalPixels) break //No se pueden agregar mas bits que pixeles en la imagen.

            // Extraer los bits de dos en dos
            val bitPair = if (bitChar + 1 < binary.length) {
                binary.substring(bitChar, bitChar + 2) // Obtiene el par de bits, revisar doc de Substring si hay dudas.
            } else {
                binary[bitChar].toString() // Si solo hay un bit
            }

           // Guardar cada bit en variables separadas como Int
            val bit1 = bitPair[0].digitToInt() // Convierte el primer bit a Int
            val bit2 = if (bitPair.length > 1) bitPair[1].digitToInt() else 0 // Convierte el segundo bit a Int o 0 si solo hay uno

            val x = algorithm.nextInt(pixels.width) //Valor de la columna
            val y = algorithm.nextInt(pixels.height) //Valor de la fila

            if (Pair(y, x) in modifiedPixels) continue //Evita que el random caiga en un pixel ya visitado.
            
            modifiedPixels.add(Pair(y,x))//Se anade el pixel visitado.

            val rgb = pixels.getRGB(y, x) //componente i,j-esima
            val alpha = (rgb shr 24) and 0xFF //corrimiento de bits y mascaras para filtrar los 8 ultimos bits
            val red = (rgb shr 16) and 0xFF 
            val green = (rgb shr 8) and 0xFF
            val blue = rgb and 0xFF
            
            val newBlue = channel(blue, bit1)//Guarda el 1er bit en blue
            val newAlpha = channel(alpha, bit2)//Guarda el 2do bit en alpha
            val newRgb = (newAlpha shl 24) or (red shl 16) or (green shl 8) or newBlue
            pixels.setRGB(x, y, newRgb) //Modificamos el valor del pixel guardando la infromacion desada
            current++ //Indicamos que se va a usar otro pixel.
        }
    }
    return pixels
}
/**
 * Modifies the LSB of the desired channel.
 * @param channel The channel to be modified.
 * @param bit The bit to be compared with the channel's LSB.
 * @return The modified channel.
 */
private fun channel(channel: Int, bit: Int): Int {
    return if ((channel and 1) == bit) {
        channel
    } else {
        if (channel == 0) 1 else channel - 1
    }
}

private val reverseAlphabet = mapOf(
     1 to 'a', 2 to 'b', 3 to 'c', 4 to 'd', 5 to 'e',
    6 to 'f', 7 to 'g', 8 to 'h', 9 to 'i', 10 to 'j',
    11 to 'k', 12 to 'l', 13 to 'm', 14 to 'n', 15 to 'o',
    16 to 'p', 17 to 'q', 18 to 'r', 19 to 's', 20 to 't',
    21 to 'u', 22 to 'v', 23 to 'w', 24 to 'x', 25 to 'y',
    26 to 'z', 27 to '?', 28 to '!', 29 to '@', 30 to '(',
    31 to ')', 32 to ':', 33 to ',', 34 to '.', 35 to '-',
    36 to '\'', 37 to 'á', 38 to 'é', 39 to 'í', 40 to 'ó',
    41 to 'ú', 42 to '1', 43 to '2', 44 to '3', 45 to '4',
    46 to '5', 47 to '6', 48 to '7', 49 to '8', 50 to '9',
    51 to '#', 52 to '$', 53 to '%', 54 to '&', 55 to '[',
    56 to ']', 57 to '{', 58 to '}', 59 to ' ', 60 to '\n'
    )

/** 
 * Decodes an image represented by an integer array of the pixels into the 
 * original text.
 * Handles letters (a-z) and (A-Z) numbers (0-9) and some special characters :
 * (!.+-?¿àèìòù¡¿/!)
 *
 * @param pixels The 2D pixel array of the image.
 * @return The decoded text.
 * @throws IllegalStateException if no key is found on the pixels array.
 */
fun decodeText(image: BufferedImage): List<Char> {
    val decimals = decode(image)    
    val string = mutableListOf<Char>()
    for (decimal in decimals) {
        reverseAlphabet[decimal]?.let { string.add(it) }
    }
    return string
}

/**
* Auxiliary function to decode the text from the image.
*
* @param image A BufferedImage of the image.
* @return A list with the numerical value of the letters.
 */
private fun decode(image: BufferedImage): List<Int> {
    val seed = image.getRGB(0, 0).toLong()
    val algorithm = Random(seed)
    val binaryText = mutableListOf<Int>()
    val totalPixels = image.width * image.getHeight()
    val decimals = mutableListOf<Int>()
    for (i in 0 until totalPixels) {
        val x = algorithm.nextInt(image.width)
        val y = algorithm.nextInt(image.height) 
        val blue = image.getRGB(x, y) and 0xFF 
        binaryText.add(blue and 1)
    }
    val stringList = binaryText.map { it.toString() }
    val compact = stringList.chunked(6).map { it.joinToString("") }
    for (i in compact) {
        decimals.add(i.toInt(2))
    }    
    return decimals
}