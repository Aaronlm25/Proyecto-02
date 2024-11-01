package steganography.data.text

import java.io.File
import java.io.IOException
import java.io.FileNotFoundException
import java.util.regex.Pattern
import java.util.regex.Matcher
import java.util.HashMap
import java.lang.StringBuilder

/**
 * Reads the file and converts it to a list of characters.
 * @return A list of the characters in the text.
 * @throws FileNotFoundException If the file doesn't exist.
*/
fun readFile(path : String): List<Char> {
    return listOf()
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
    // Implementation
}

/**
 * Replaces words in a text with the corresponding integer value according
 * to the alphabet.
 *
 * @param content Text with the words you want to exchange according
 * to the alphabet.
 * @return A list with the integer that corresponds to each word in the text.
 */
fun replaceAlphabet(text:List<Char>): List<Int> {
    // Implementation
}