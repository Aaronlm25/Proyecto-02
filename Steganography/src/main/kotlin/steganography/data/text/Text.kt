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
    val text = File(path).readText()
    return text.toList()
}
/**
 * Reads the text at the specified path and returns the full text
 * as a string and all words in the text are converted to lowercase.
 *
 * @param pathFile Path of the text to read.
 * @return The full text of the file at the path.
 */
fun readFull(path: String):String{
    val lineas = File(path).readText().lowercase()
    return lineas
}
/**
 * Writes a list of characters to a file at the specified path.
 * 
 * @param characters List of characters.
 * @param textPath Path to which the message will be written.
 * @return The file with the message.
 * @throws IOException If the path is invalid.
*/
fun toFile(characters : List<Char>, textPath: String): File {
    if (!textPath.endsWith(".txt")) {
        throw IOException("Invalid path.")
    }
    val file = File(textPath)
    file.createNewFile()
    val text = characters.joinToString("")
    file.writeText(text)
    return file
}
/**
 * Gets the expressions in a text that match a specific pattern and
 * creates a map containing all these expressions bound to an integer.
 *
 * @param fileText Text to compress.
 * @return A map with the regular expressions in the text bound to an integer.
 */
private fun findRegulars(fileText: String): Map<String, String> {
    var text = fileText
    val pattern = Pattern.compile("\\b\\w+\\s+\\w+\\b")
    val regulars = mutableListOf<String>()
    val regEx = mutableMapOf<String, String>()
    while (true) {
        val matcher = pattern.matcher(text)
        var found = false
        while (matcher.find()) {
            found = true
            val match = matcher.group()
            if (match !in regulars) {
                regulars.add(match)
            }
        }
        if (!found) break
        val splittext = pattern.split(fileText)
        text = splittext.joinToString(" ")
    }    
    var i = 61
    for (item in regulars) {
        regEx[item] = i.toString()
        i++
    }
    return regEx
}
/**
 * Replaces words in a text with the corresponding integer value
 * according to the provided map
 *
 * @param alphabet Contains the expressions in the text and their
 * corresponding value.
 * @param content Text to be replaced based on the provided map.
 * @return The provided text but the words will be replaced by
 * the values ​​from the map.
 */
private fun replaceMap(map: Map<String,String>, content: String): String{
    var text = content
    for (expression in map.keys) {
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(text)
        val a = map[expression]
        text = matcher.replaceAll(a)
    }
    return text
}
/**
 * Replaces words in a text with the corresponding integer value according
 * to the alphabet.
 *
 * @param content Text with the words you want to exchange according
 * to the alphabet.
 * @return A list with the integer that corresponds to each word in the text.
 */
private fun replaceAlphabet(content:String):List<Int>{
    val text = content.toCharArray()
    val numbers = mutableListOf<Int>()
    var currentNumber = ""
    for (char in text) {
        if (char.isDigit()) {
            currentNumber += char
        }else if (char.isWhitespace() && currentNumber.isNotEmpty()){
            numbers.add(currentNumber.toInt())
            currentNumber = ""
        } else if (char.lowercaseChar().isLetter()){
            alphabet[char]?.let { numbers.add(it) }
        } else{
            alphabet[char]?.let { numbers.add(it) }
        }
    }
    if (currentNumber.isNotEmpty()) {
        numbers.add(currentNumber.toInt())
    }
    return numbers
}
/**
 * Compresses a text and converts words to integer values.
 * 
 * @param text Text to be compressed.
 * @return List of integer values ​​representing the words in the text.
 */
fun compress(text: String): List<Int>{
    val regularsMap = findRegulars(text)
    val replaced = replaceMap(regularsMap, text)
    val compressText = replaceAlphabet(replaced)
    return compressText
}