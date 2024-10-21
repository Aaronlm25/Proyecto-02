package steganography.data.text

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
/**
 * Reads the file and converts it to a list of characters.
 * @return A list of the characters in the text.
*/
fun readFile(pathText : String): List<Char> {
    val characters = mutableListOf<Char>()
    try {
        val text = File(pathText).readText()
        for (char in text) {
            val ascii = char.code
            if ((ascii < 65 || ascii > 122) && ascii != 32) {
                throw IllegalArgumentException("The text contains special characters")
            }else {
                characters.add(char) 
            }
        }
        return characters
    }catch (e: FileNotFoundException) {
        throw FileNotFoundException("File not found")
    }catch (e: IOException) {
        throw IOException("File cannot be read")
    }catch (e: Exception) {
        throw Exception("An error occurred while reading the file")
    }
}

/**
 * Writes a list of characters to a file at the specified path.
 * 
 * @param characters List of characters.
 * @param textPath Path to which the message will be written.
 * @return The file with the message.
*/
fun toFile(characters : List<Char>, textPath: String): File {
    // Implementation
    return File("pathname")
}