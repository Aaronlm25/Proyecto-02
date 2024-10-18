package steganography
import java.io.File
/**
 * Text
 * 
 * This class proccess a text so as to obtain a list of Strings
 */
class TextHandler {
    /**
     * Reads the file and converts it to a list of strings.
     * @return A list of the words in the text.
    */
    fun readFile(pathText : String):List<Char>{
        val characters = mutableListOf(' ')
        return characters
    }

    /**
     * Writes the encoded message to a file at the specified path.
     * 
     * @param characters Characters that make up the encoded message.
     * @param textPath Path to which the encoded message will be written.
     * @return A file with the encoded message.
    */
    fun toFile(characters : List<Char>, textPath: String): File{
        val message = File(textPath)
        return message
    }
}