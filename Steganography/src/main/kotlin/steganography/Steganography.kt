package steganography

import steganography.data.text.compress
import java.util.Random
/**
 * Encodes the text into an image represented by an integer array of the pixels.
 *
 * @param text The text to be encoded.
 * @param pixels The 2D pixel array of the image.
 * @return 2D pixel array with the message encoded.
 * @throws IllegalStateException if the text is too large for the pixels array.
 */
fun encodeText(text: String,pixels: Array<IntArray>): Array<IntArray> {
    val totalPixels = pixels.size * pixels[0].size //Largo por alto de la matriz de pixeles
    val algorithm = Random(pixels[0][0].toLong()) //Determinara cual pixel cambiar (la semilla es la componente 0,0).
    val compressText = compress(text) //LIsta de enteros que representan las palabras y letras en el texto.
    val binaries = compressText.map { it.toString(2).padStart(10, '0') }
    var current = 0
    val modifiedPixels = mutableSetOf<Pair<Int,Int>>() //Pixeles que se vayan modificando.
    for (binary in binaries) { //Los binarios son un Strinf (No me funen).
        for (bitChar in binary) { //Recorre los chars que forman al binario que es un String.
            if (current >= totalPixels) break //No se pueden agregar mas bits que pixeles en la imagen.
            val x = algorithm.nextInt(pixels[0].size) //Valor de la columna
            val y = algorithm.nextInt(pixels.size) //Valor de la fila
            if (Pair(y, x) in modifiedPixels) continue //Evita que el random caiga en un pixel ya visitado.
            modifiedPixels.add(Pair(y,x))//Se anade el pixel visitado.
            val rgb = pixels[y][x] //componente i,j-esima
            val red = (rgb shr 16) and 0xFF
            val green = (rgb shr 8) and 0xFF
            val blue = rgb and 0xFF
            val bit = bitChar.toString().toInt() //En principio los elementos son Char.
            val newBlue = //Se modifica el valor en Blue.
            if ((blue and 1) == bit) {//Matching (Solo cambios necesarios).
                blue
            } else {
                if (blue == 0) 1 else blue - 1
            }
            val newRgb = (red shl 16) or (green shl 8) or newBlue
            pixels[y][x] = newRgb
            current++ //Indicamos que se va a usar otro pixel.
        }
    }
    return pixels
}

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
@Throws(IllegalStateException::class)
fun decodeText(pixels: Array<IntArray>): List<Char> {
    return listOf()
}
/**
 * Implementacion basica, solo reune los bits modificados, no hace la conversion
 * para texto o regular expression.
 * 
 * 
 * Tiene un error. Agarra bits aun cuando ya no se necesitan es por la forma en la que 
 * condicione el for.
 */
fun decode(pixels: Array<IntArray>): List<Int> {
    val seed = pixels[0][0].toLong()
    val algorithm = Random(seed)
    val binaryText = mutableListOf<Int>()
    val totalPixels = pixels.size * pixels[0].size
    val decimals = mutableListOf<Int>()
    for (i in 0 until totalPixels) {
        val x = algorithm.nextInt(pixels[0].size) // Valor de la columna
        val y = algorithm.nextInt(pixels.size) // Valor de la fila
        val blue = pixels[y][x] and 0xFF // Obtener el valor azul del píxel
        // Extraer el bit menos significativo del azul(LSB)
        binaryText.add(blue and 1)
    }
    val stringList = binaryText.map { it.toString() }
    val compact = stringList.chunked(10).map { it.joinToString("") }
    for (i in compact) {
        decimals.add(i.toInt(2))
    }
    return decimals
}