package steganography.data.text

import java.io.File
import java.io.IOException
import java.io.FileNotFoundException
/**
 * Reads the file and converts it to a list of characters.
 * @return A list of the characters in the text.
 * @throws FileNotFoundException If the file doesn't exist.
*/
fun readFile(pathText : String): List<Char> {
    val characters = mutableListOf<Char>()
    val text = File(pathText).readText()
    for (char in text) {
        characters.add(char) 
    }
    return characters
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