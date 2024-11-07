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
import java.io.FileNotFoundException
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
                "h" -> {
                    val text = getText()
                    val image = getImage("Proporcione la ruta de la imagen donde se ocultarà el texto.")
                    saveImage(text, image)
                }
                "u" -> {
                    val image = getImage("Proporcione la ruta de la imagen que contiene los datos ocultos.")
                    saveText(image)
                }
                "x" -> {
                    active = false
                    println("El programa ha terminado.")
                }
                else -> println("Introduzca una opción válida (u) (h) (x).")
            }
        } catch (ise: IllegalStateException) {
            println(ise.message)
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

private fun readNonNullInput(): String = lineReader.readLine().trim()


private fun saveImage(text : List<Char>, image : BufferedImage) {
    val encoded = encodeText(text, image)
    while (true) {
        try {
            println("Proporcione la ruta con el nombre de la imagen resultante.")
            val resultPath = readNonNullInput()
            saveImage(encoded, resultPath)
            println("El texto se ha ocultado exitosamente en : $resultPath")
            return
        } catch (ioe: IOException) {
            println("No se pudo guardar la imagen en la ruta proporcionada.")
        } catch(iae : IllegalArgumentException) {
            println("El formato de la imagen no es valido debe ser (png) o (jpg).")
        }
    }
}

private fun saveText(image : BufferedImage) {
    while (true) {
        try {
            println("Proporcione el nombre del archivo en el que se guardará el texto develado.")
            val resultPath = readNonNullInput()
            val text = decodeText(image)
            toFile(text, resultPath)
            println("Se ha decodificado el mensaje en la imagen en: $resultPath")
            return
        } catch (ise: IOException) {
            println("No se pudo guardar el texto en la ruta que proporciono.")
        } catch (iae: IllegalArgumentException) {
            println("El formato del archivo deber ser .txt")
        }
    }
}

private fun getText(): List<Char> {
    while (true) {
        try {
            println("Proporcione la ruta del archivo con el texto a ocultar.")
            val textPath = readNonNullInput()
            return readFile(textPath)
        } catch (iae: IllegalArgumentException) {
            println("El archivo debe ser .txt")
        } catch (ise: IllegalStateException) {
            println("El archivo tiene caracteres no soportados.")
        } catch(fnfe : FileNotFoundException) {
            println("No se encontro el archivo.")
        }
    }
}

private fun getImage(prompt: String): BufferedImage {
    while (true) {
        try {
            println(prompt)
            val imagePath = readNonNullInput()
            return loadImage(imagePath)
        } catch (e: IOException) {
            println("Error: No se pudo leer la imagen desde la ruta proporcionada. Asegúrese de que el archivo existe y es accesible.")
        } catch (e: IllegalArgumentException) {
            println("Error: El archivo proporcionado no es una imagen válida o tiene un formato no soportado.")
        } catch(fnfe : FileNotFoundException) {
            println("No se encontro el archivo.")
        }
    }
}