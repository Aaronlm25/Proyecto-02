package steganography

import kotlin.text.trim
import kotlin.io.readlnOrNull
import steganography.data.text.toFile
import steganography.data.text.readFile
import steganography.data.image.loadImage
import steganography.data.image.saveImage

fun main() {
    var active = true
    while (active) {
        displayMenu()
        val option = readNonNullInput()
        try {
            when (option) {
                "h" -> hideTextInImage()
                "u" -> revealTextFromImage()
                "x" -> {
                    active = false
                    println("El programa ha terminado.")
                }
                else -> println("Introduzca una opción válida (u) (h) (x).")
            }
        } catch (iae: IllegalArgumentException) {
            println(iae.message)
        }
    }
}

fun displayMenu() {
    println(
        """
        Esteganografía.
        (h) Ocultar texto en imagen.
        (u) Develar texto de imagen.
        (x) Salir.
        """.trimIndent()
    )
}

fun readNonNullInput(): String {
    return readLine()?.trim() ?: throw IllegalArgumentException("Todos los parámetros son necesarios")
}

fun hideTextInImage() {
    val text = getTextFromFile()
    val pixels = getPixelsFromImage("Proporcione la ruta de la imagen donde se ocultarà el texto.")
    val encoded = encodeText(text, pixels)
    println("Proporcione la ruta de la imagen resultante.")
    val resultPath = readNonNullInput()
    saveImage(encoded, resultPath)
}

fun revealTextFromImage() {
    val pixels = getPixelsFromImage("Proporcione la ruta de la imagen que contiene los datos ocultos.")
    println("Proporcione el nombre del archivo en el que se guardará el texto develado.")
    val resultPath = readNonNullInput()
    val text = decodeText(pixels)
    toFile(text, resultPath)
    println("Se ha decodificado el mensaje en la imagen en: $resultPath")
}

fun getTextFromFile(): List<Any> {
    while (true) {
        try {
            println("Proporcione la ruta del archivo con el texto a ocultar.")
            val textPath = readNonNullInput()
            return readFile(textPath,1)
        } catch (e: Exception) {
            println("No se pudo abrir el archivo que proporcionó!")
        }
    }
}

fun getPixelsFromImage(prompt : String): Array<IntArray> {
    while (true) {
        try {
            println(prompt)
            val imagePath = readNonNullInput()
            return loadImage(imagePath)
        } catch (e: Exception) {
            println("No se pudo abrir el archivo que proporcionó!")
        }
    }
}