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
import org.jline.reader.LineReaderBuilder
import org.jline.reader.LineReader
import org.jline.terminal.TerminalBuilder
import org.jline.builtins.Completers.FileNameCompleter 

val lineReader = LineReaderBuilder.builder()
    .terminal(TerminalBuilder.terminal())
    .completer(FileNameCompleter())
    .build()

fun main() {
    while (true) {
        displayMenu()
        val option = readNonNullInput()
        try {
            when (option) {
                "h" -> hideTextInImage()
                "u" -> revealTextFromImage()
                "x" -> {
                    println("El programa ha terminado.")
                    return
                }
                else -> println("Introduzca una opción válida (u) (h) (x).")
            }
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }catch (e: Exception) {
            println("Se ha producido un error general: ${e.message}")
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

private fun hideTextInImage() {
    val text = getText()
    val image = getImage("Proporcione la ruta de la imagen donde se ocultará el texto.")
    saveImage(text, image)
}

private fun revealTextFromImage() {
    val image = getImage("Proporcione la ruta de la imagen que contiene los datos ocultos.")
    saveText(image)
}

private fun saveImage(text: List<Char>, image: BufferedImage) {
    val encoded = encodeText(text, image)
    while (true) {
        try {
            println("Proporcione la ruta con el nombre de la imagen resultante. \nEjemplo: /home/user/nombre-imagen.png")
            val resultPath = readNonNullInput()
            saveImage(encoded, resultPath)
            println("El texto se ha ocultado exitosamente en: $resultPath")
            return
        } catch (ioe: IOException) {
            println("No se pudo guardar la imagen en la ruta proporcionada.")
        } catch (iae: IllegalArgumentException) {
            println("El formato de la imagen no es válido. Debe ser (png) o (jpg).")
        }catch (e: Exception) {
            println("Se ha producido un error general: ${e.message}")
        }
    }
}

private fun saveText(image: BufferedImage) {
    while (true) {
        try {
            println("Proporcione el nombre del archivo en el que se guardará el texto decodificado.")
            val resultPath = readNonNullInput()
            val text = decodeText(image)
            toFile(text, resultPath)
            println("Se ha decodificado el mensaje en: $resultPath")
            return
        } catch (ise: IOException) {
            println("No se pudo guardar el texto en la ruta proporcionada.")
        } catch (iae: IllegalArgumentException) {
            println("El formato del archivo debe ser .txt")
        }catch (e: Exception) {
            println("Se ha producido un error general: ${e.message}")
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
        } catch (fnfe: FileNotFoundException) {
            println("No se encontró el archivo.")
        }catch (e: Exception) {
            println("Se ha producido un error general: ${e.message}")
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
        } catch (fnfe: FileNotFoundException) {
            println("No se encontró el archivo.")
        }catch (e: Exception) {
            println("Se ha producido un error general: ${e.message}")
        }
    }
}