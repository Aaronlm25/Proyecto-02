package steganography.data.text

import java.io.File
import java.io.IOException
import java.io.FileNotFoundException
import java.util.regex.Pattern
import java.util.regex.Matcher
import java.util.HashMap    

private val alphabet = mapOf(
    'a' to 1, 'b' to 2, 'c' to 3, 'd' to 4, 'e' to 5, 
    'f' to 6, 'g' to 7, 'h' to 8, 'i' to 9, 'j' to 10, 
    'k' to 11, 'l' to 12, 'm' to 13, 'n' to 14, 'o' to 15, 
    'p' to 16, 'q' to 17, 'r' to 18, 's' to 19, 't' to 20, 
    'u' to 21, 'v' to 22, 'w' to 23, 'x' to 24, 'y' to 25, 
    'z' to 26, '?' to 27, '!' to 28, '@' to 29, '(' to 30,
    ')' to 31, ':' to 32, ',' to 33, '.' to 34, '-' to 35,
    '\'' to 36, 'á' to 37, 'é' to 38, 'í' to 39, 'ó' to 40, 
    'ú' to 41, '1' to 42, '2' to 43, '3' to 44, '4' to 45, 
    '5' to 46, '6' to 47, '7' to 48, '8' to 49, '9' to 50,
    '#' to 51, '$' to 52, '%' to 53, '&' to 54, '[' to 55,
    ']' to 56, '{' to 57, '}' to 58, ' ' to 59, '\n' to 60
    )

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

/**
 * Replaces words in a text with the corresponding integer value according
 * to the alphabet.
 *
 * @param content Text with the words you want to exchange according
 * to the alphabet.
 * @return A list with the integer that corresponds to each word in the text.
 */
fun replaceAlphabet(text: List<Char>): List<Int>{
    val numbers = mutableListOf<Int>()
    for (char in text) {
        if (char.lowercaseChar().isLetter()){
            alphabet[char]?.let { numbers.add(it) }
        } else{
            alphabet[char]?.let { numbers.add(it) }
        }
    }
    return numbers
}