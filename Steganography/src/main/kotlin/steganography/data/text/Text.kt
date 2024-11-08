package steganography.data.text

import java.io.File
import java.io.IOException
import java.io.FileNotFoundException

/**
 * Reads the file at a specified path and converts it to a list of characters.
 *
 * @param The path of the text to read.
 * @return A list of the characters in the text.
 */
fun readFile(path : String): List<Char> {
    val file = File(path)
    return file.readText().toList()
}

/**
 * Writes a list of characters to a file at the specified path.
 * 
 * @param characters List of characters.
 * @param textPath Path to which the message will be written.
 * @return The file with the message.
 * @throws IOException If the path is invalid.
*/
fun toFile(characters : List<Char>, path: String): File {
    val file = File(path)
    val text = characters.joinToString("")
    file.writeText(text)
    return file
}