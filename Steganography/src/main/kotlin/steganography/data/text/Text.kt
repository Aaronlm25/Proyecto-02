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
 * Gets the expressions in a text that match a specific pattern.
 * @param file Text Text to compress.
 * @return A list with the regular expressions of the text.
 */ 
fun getRegulars(fileText: String): List<String> {
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
fun generateMap(expressions: List<String>): Map<String, String> {
    val regEx = mutableMapOf<String, String>()
    var i = 10
    for (item in expressions) {
        regEx[item] = i.toString()
        i++
    }
    return regEx
}
/**
 * provisional methos, don't delet
 */
fun map(expressions: List<String>): Map<String, String> {
    val regEx = mutableMapOf<String, String>()
    var i = 10
    for (item in expressions) {
        regEx[i.toString()] = item
        i++
    }
    return regEx
}
/**
 * provisional methos, don't delet
 */
fun replaceAlphabet(alphabet: Map<String,String>, content: String): String{
    var text = content
    for (expression in alphabet.keys) {
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(text)
        val a = alphabet[expression]
        text = matcher.replaceAll(a)
    }
    return text
}
/**
* provisional methos, don't delet
 */
fun reassemble(alphabet: Map<String,String>, content: String): String{
    var text = content
    for (key in alphabet.keys) {
        val value = "\\b"+key+"\\b"
        val pattern = Pattern.compile(value)
        val matcher = pattern.matcher(text)
        val a = alphabet[key]
        text = matcher.replaceAll(a)
    }
    return text
}