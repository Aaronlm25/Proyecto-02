package steganography

import kotlin.text.trim
import kotlin.io.readlnOrNull
import steganography.data.text.toFile
import steganography.data.text.readFile
import steganography.data.image.loadImage
import steganography.data.image.saveImage
import steganography.decodeText
import steganography.encodeText
import java.awt.image.BufferedImage
import java.io.IOException
import java.nio.file.Paths
import org.jline.reader.LineReaderBuilder
import org.jline.reader.LineReader
import org.jline.terminal.TerminalBuilder
import org.jline.builtins.Completers.FileNameCompleter 

val lineReader = LineReaderBuilder.builder()
    .terminal(TerminalBuilder.terminal())
    .completer(FileNameCompleter())
    .build()

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
        } catch (e: Exception) {
            println(e.message)
        }
    }
}

fun displayMenu() {
    println(
        """
        Esteganografía.
        (Pulse tab para autocompletar)
        (h) Ocultar texto en imagen.
        (u) Develar texto de imagen.
        (x) Salir.
        """.trimIndent()
    )
}

fun readNonNullInput(): String {
    return lineReader.readLine().trim()
}

fun hideTextInImage() {
    val text = getTextFromFile()
    val pixels = getImage("Proporcione la ruta de la imagen donde se ocultarà el texto.")
    val encoded = encodeText(text, pixels)
    println("Proporcione la ruta de la imagen resultante.")
    val resultPath = readNonNullInput()
    println(resultPath)
    saveImage(encoded, resultPath)
    println("El texto se ha ocultado exitosamente en : $resultPath")
}

fun revealTextFromImage() {
    val pixels = getImage("Proporcione la ruta de la imagen que contiene los datos ocultos.")
    println("Proporcione el nombre del archivo en el que se guardará el texto develado.")
    val resultPath = readNonNullInput()
    val text = decodeText(pixels)
    toFile(text, resultPath)
    println("Se ha decodificado el mensaje en la imagen en: $resultPath")
}

fun getTextFromFile(): List<Char> {
    while (true) {
        try {
            println("Proporcione la ruta del archivo con el texto a ocultar.")
            val textPath = readNonNullInput()
            return readFile(textPath)
        } catch (e: Exception) {
            println("No se pudo abrir el archivo que proporcionó!")
        }
    }
}

fun getImage(prompt: String): BufferedImage {
    while (true) {
        try {
            println(prompt)
            val imagePath = readNonNullInput()
            return loadImage(imagePath)
        } catch (e: IOException) {
            println("Error: No se pudo leer la imagen desde la ruta proporcionada. Asegúrese de que el archivo existe y es accesible.")
        } catch (e: IllegalArgumentException) {
            println("Error: El archivo proporcionado no es una imagen válida o tiene un formato no soportado.")
        }
    }
}