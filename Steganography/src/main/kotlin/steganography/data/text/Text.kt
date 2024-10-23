package steganography.data.text

import java.io.File
import java.io.IOException
import java.io.FileNotFoundException
import java.util.regex.Pattern
import java.util.regex.Matcher

/**
 * Reads the file and converts it to a list of characters or Strigs.
 * @return A list of the characters in the text.
 * @throws FileNotFoundException If the file doesn't exist.
*/
fun readFile(pathText : String, type:Int): List<Any> {

    if (type == 1){
        val characters = mutableListOf<Char>()
        val text = File(pathText).readText()
        for (char in text) {
            characters.add(char) 
        }
        return characters
    } else if (type == 0){
        val rows = mutableListOf<String>()
        val text = File(pathText).readText()
        for (line in text.lines()) {
            rows.add(line)
        }
        return rows
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
 * Compresses the text by searching for regular expressions to create a dictionary with that expression.
 * 
 */
fun compressText(text: List<Any>): List<String>{
    val sentence = text[0]
    val regulars = mutableListOf<String>()

    val pattern = Pattern.compile("\\b\\w+\\s+\\w+\\b")
    val match = pattern.matcher(sentence.toString())
    if (match.find()){
        regulars.add(match.group())
    }    
    return regulars
}