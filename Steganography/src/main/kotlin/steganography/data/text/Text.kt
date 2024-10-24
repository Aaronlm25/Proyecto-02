package steganography.data.text

import java.io.File
import java.io.IOException
import java.io.FileNotFoundException
import java.util.regex.Pattern
import java.util.regex.Matcher
import java.lang.StringBuilder

/**
 * Reads the file and converts it to a list of characters or Strigs.
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
    } else if (type == 0){
        for (line in filetext.lines()) {
            text.add(line)
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

fun toparagraph(rows: List<Any>):String{
    val sb = StringBuilder()
    for (i in rows) {
        sb.append(i.toString().lowercase())
    }
    return sb.toString()
}

fun genratePatterns():List<String>{
    val patterns = mutableListOf<String>()
    val basePattern = "\\\\b0\\\\w+\\\\b".toCharArray()
    for (char in 'a'..'z' ){
        basePattern[3] = char
        val pattern = String(basePattern)
        patterns.add(pattern)
    }
    return patterns
}
/**
 * Compresses the text by searching for regular expressions to create a dictionary with that expression.
 * 
 */
fun compressText(text: String): List<String>{
    val patterns = genratePatterns()
    val regulars = mutableListOf<String>()
    for (i in patterns) {
        val pattern = Pattern.compile(i.toString())
        val match = pattern.matcher(text)
        if (match.find()){
            regulars.add(match.group())
        }
    }
    return regulars
}