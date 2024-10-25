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
fun readFile(pathText : String, type:Int): List<Any> {
    val filetext = File(pathText).readText()
    val text = mutableListOf<Any>()
    if (type == 1){
        for (char in filetext) {
            text.add(char) 
        }
        return text
    }else{
        throw IllegalArgumentException()
    }
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
 * Reads the text at the specified path and returns the entire text 
 * as a string and all words in the text are converted to lowercase.
 * @param pathFile Path of the text to read
 * @return The full text of the file at the path
 */
fun readFull(pathFile: String):String{
    val lineas = File(pathFile).readText().lowercase()
    return lineas
}
/**
 * Compresses a text by searching for its regular expressions.
 * @param file Text Text to compress.
 * @return A list with the regular expressions of the text.
 */ 
fun compressText(fileText: String): List<String> {
    var text = fileText
    val pattern = Pattern.compile("\\b\\w+\\s+\\w+\\b")
    val regulars = mutableListOf<String>()
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
    return regulars
}
/**
 * Generates a map representing the alphabet to be used to perform
 * the text encoding.
 * @param expressions The regular expressions that make up the text
 * to be encoded.
 * @return A map formed by regular expressions linked to an integer. 
 */
fun generateMap(expressions: List<String>): Map<Int, String> {
    val regEx = mutableMapOf<Int, String>()
    var i = 0
    for (item in expressions) {
        regEx[i] = item
        i++
    }
    return regEx
}
/**
 * 
 */
fun replaceAlphabet(): Int{
    return 0
}