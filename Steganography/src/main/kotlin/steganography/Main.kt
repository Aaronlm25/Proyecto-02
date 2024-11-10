/**
 * This module provides the main entry point for the steganography application.
 * It includes functions to display the menu, read user input, and handle the main operations.
 * 
 * Functions:
 * - main: The main entry point of the application.
 * - displayMenu: Displays the main menu to the user.
 * - readNonNullInput: Reads non-null input from the user.
 * - hideTextInImage: Handles the process of hiding text in an image.
 * - revealTextFromImage: Handles the process of revealing text from an image.
 * - saveImage: Saves the encoded image to a specified path.
 * - saveText: Saves the decoded text to a specified path.
 * - getText: Prompts the user to enter the text to be hidden.
 * - getImage: Prompts the user to provide the path to the image.
 */
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
                    println("\nEl programa ha terminado.")
                    return
                }
                else -> println("\nIntroduzca una opción válida (u) (h) (x).")
            }
        } catch (ise: IllegalStateException) {
            println(ise.message)
        }catch (e: Exception) {
            println("\nSe ha producido un error. Intente de nuevo.")
        }
    }
}

/**
 * Displays the main menu to the user.
 */
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

/**
 * Reads non-null input from the user.
 * @return The trimmed input string.
 */
private fun readNonNullInput(): String = lineReader.readLine().trim()

/**
 * Handles the process of hiding text in an image.
 */
private fun hideTextInImage() {
    val text = getText()
    val image = getImage("\nProporcione la ruta de la imagen donde se ocultará el texto.")
    saveImage(text, image)
}

/**
 * Handles the process of revealing text from an image.
 */
private fun revealTextFromImage() {
    val image = getImage("\nProporcione la ruta de la imagen que contiene los datos ocultos.")
    saveText(image)
}

/**
 * Saves the encoded image to a specified path.
 * @property text The text to be hidden in the image.
 * @property image The image in which the text will be hidden.
 */
private fun saveImage(text: List<Char>, image: BufferedImage) {
    val encoded = encodeText(text, image)
    while (true) {
        try {
            println("\nProporcione el nombre de la imagen resultante (ejemplo: nombre-imagen.png):")
            val fileName = readNonNullInput()
            val resultPath = System.getProperty("user.dir") + "/" + fileName
            
            saveImage(encoded, resultPath)
            println("\nEl texto se ha ocultado exitosamente en: $resultPath")
            return
        } catch (ioe: IOException) {
            println("\nNo se pudo guardar la imagen en la ruta proporcionada.")
        } catch (iae: IllegalArgumentException) {
            println("\nEl formato de la imagen no es válido. Debe ser (png) o (jpg).")
        } catch (e: Exception) {
            println("\nSe ha producido un error. Intente de nuevo.")
        }
    }
}

/**
 * Saves the decoded text to a specified path.
 * @property image The image from which the text will be revealed.
 */
private fun saveText(image: BufferedImage) {
    while (true) {
        try {
            println("\nProporcione el nombre del archivo en el que se guardará el texto decodificado.")
            val resultPath = readNonNullInput()
            val text = decodeText(image)
            toFile(text, resultPath)
            println("\nSe ha decodificado el mensaje en: $resultPath")
            return
        } catch (ise: IOException) {
            println("\nNo se pudo guardar el texto en la ruta proporcionada.")
        } catch (iae: IllegalArgumentException) {
            println("\nEl formato del archivo debe ser .txt")
        }catch (e: Exception) {
            println("\nSe ha producido un error. Intente de nuevo.")
        }
    }
}

/**
 * Prompts the user to enter the text to be hidden.
 * @return The text entered by the user as a list of characters.
 */
private fun getText(): List<Char> {
    while (true) {
        try {
            println("\nProporcione la ruta del archivo con el texto a ocultar.")
            val textPath = readNonNullInput()
            return readFile(textPath)
        } catch (iae: IllegalArgumentException) {
            println("\nEl archivo debe ser .txt")
        } catch (ise: IllegalStateException) {
            println("\nEl archivo tiene caracteres no soportados.")
        } catch (fnfe: FileNotFoundException) {
            println("\nNo se encontró el archivo.")
        }catch (e: Exception) {
            println("\nSe ha producido un error. Intente de nuevo.")
        }
    }
}

/**
 * Prompts the user to provide the path to the image.
 * @property prompt The prompt message to display to the user.
 * @return The loaded image.
 */
private fun getImage(prompt: String): BufferedImage {
    while (true) {
        try {
            println(prompt)
            val imagePath = readNonNullInput()
            return loadImage(imagePath)
        } catch (e: IOException) {
            println("\nError: No se pudo leer la imagen desde la ruta proporcionada. Asegúrese de que el archivo existe y es accesible.")
        } catch (e: IllegalArgumentException) {
            println("\nError: El archivo proporcionado no es una imagen válida o tiene un formato no soportado.")
        } catch (fnfe: FileNotFoundException) {
            println("\nNo se encontró el archivo.")
        }catch (e: Exception) {
            println("\nSe ha producido un error. Intente de nuevo.")
        }
    }
}