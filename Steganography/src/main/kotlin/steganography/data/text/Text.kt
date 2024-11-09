/**
 * Module that provides functions for reading and writing text files.
 * 
 * Functions:
 * - readFile: Reads a text file and converts it into a list of characters.
 * - toFile: Writes a list of characters to a text file.
 * 
 * Exceptions:
 * - FileNotFoundException: If the file is not found.
 * - IllegalArgumentException: If the file is not of type txt.
 * - IOException: If an error occurs while writing to the file.
 */
package steganography.data.text
import java.io.File
import java.io.IOException
import java.io.FileNotFoundException
import java.util.regex.Pattern
import java.util.regex.Matcher
import java.util.HashMap    

/**
 * Reads the file at a specified path and converts it to a list of characters.
 *
 * @property The path of the text to read.
 * @return A list of the characters in the text.
 * @throws FileNotFoundException If no file is found.
 * @throws IllegalArgumentException If file is not txt.
 */
fun readFile(path : String): List<Char> {
    val type = path.substringAfterLast(".")
    if(type == "txt") {
        val file = File(path)
        return file.readText().toList()
    }
    throw IllegalArgumentException("La extension del archivo no es valida.")
}

/**
 * Writes a list of characters to a file at the specified path.
 * 
 * @property characters List of characters.
 * @property textPath Path to which the message will be written.
 * @return The file with the message.
 * @throws IOException If the path is invalid.
*/
fun toFile(characters : List<Char>, path: String): File {
    if (path.substringAfterLast(".") != "txt") {
        throw IOException("La extension del archivo no es valida.")
    }
    val file = File(path)
    val text = characters.joinToString("")
    file.writeText(text)
    return file
}