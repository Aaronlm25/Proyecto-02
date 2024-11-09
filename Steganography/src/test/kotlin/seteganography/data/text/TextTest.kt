package steganography.data.text

import steganography.data.text.readFile
import steganography.data.text.toFile
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.collections.shouldContain
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Paths
import java.nio.file.Files

class TextTest : StringSpec ({
    lateinit var textPaths: MutableList<String>

    beforeSpec {
        val dirPath = Paths.get("src/test/resources/text")
        textPaths = mutableListOf<String>()
        Files.newDirectoryStream(dirPath, "*.txt").use { stream ->
            stream.forEach { path ->
                textPaths.add(path.toString())
            }
        }
    }

    "should the list of charactres of a given text file not be empty" {
        for(path in textPaths) {
            readFile(path).size shouldBeGreaterThan 0
        }
    }

    "should be the list be same" {
        for(path in textPaths) {
            val text = File(path).readText()
            val characters = text.toList()
            characters shouldBe readFile(path)
        }
    }

    "should not modify text" {
        for(path in textPaths) {
            val text = readFile(path).toList()
            val savePath = path.substringBefore(".txt") + "_saved.txt"
            val file = File(savePath)
            toFile(text, savePath)
            readFile(savePath) shouldBe text
            file.delete()
        }
    }

    "should write a simple text on the file" {
        val path = "src/test/resources/text/saved.txt"
        val file = File(path)
        file.createNewFile()
        file.setWritable(true)
        val text = "this is the write test"
        val chars = text.toList()
        toFile(chars, path)
        val writtenText = file.readText()
        writtenText shouldBe text
        file.delete();
    }

    "should write a text with special characters on the file" {
        val path = "src/test/resources/special_saved.txt"
        val file = File(path)
        file.createNewFile()
        file.setWritable(true)
        val text = "! # \$ % & ' ( ) * + , - . / : ; < = > ? @ [ \\ ] ^ _ { | } ~ " +
                   "\$ € £ ¥ ₹ ₣ ₩ ₪ ₱ ฿ ₦ ₲ ₴ ₽ " +
                   "+ - × ÷ ± ∓ ∑ ∏ √ ∞ ≠ ≈ ≡ ≤ ≥ ∫ ∴ ∵ ⊕ ⊗ ⊥ " +
                   "¬ ∧ ∨ ∀ ∃ ∴ ⊂ ⊆ ⊄ ∩ ∪ " +
                   "1 2 3 4 6 7 8 9 10"
        val chars = text.toList()
        toFile(chars, path)
        val writtenText = file.readText()
        writtenText shouldBe text
        file.delete();
    }

    "should write a text with more spaces between words on the file" {
        val path = "src/test/resources/spaces_saved.txt"
        val file = File(path)
        file.createNewFile()
        file.setWritable(true)
        val text = "th     is is the    test o f the tex t  with  morespaces between     w o r d s"
        val chars = text.toList()
        toFile(chars, path)
        val writtenText = file.readText()
        writtenText shouldBe text
        file.delete();
    }

    "should write a text with more than one line on the file" {
        val path = "src/test/resources/line_saved.txt"
        val file = File(path)
        file.createNewFile()
        file.setWritable(true)
        val text = "\n this \n\n is \n the \n text \n in \n\n\n lines"
        val chars = text.toList()
        toFile(chars, path)
        val writtenText = file.readText()
        writtenText shouldBe text
        file.delete()
    }

    "should create a file" {
        val path = "src/test/resources/created_saved.txt"
        val file = File(path)
        val text = "this is the create test"
        val characters = text.toList()
        if (file.exists()) {
            file.delete() 
        }
        toFile(characters, path)
        file.exists() shouldBe true
        file.isFile shouldBe true
        file.delete()
    }

    "should throw an exception when trying to read a file that doesn't exist" {
        val path = "src/test/resources/non-existent.txt"
        shouldThrow<FileNotFoundException> {
            readFile(path)
        }
    }

    "should throw an exception when the path is invalid" {
        val filePath = "src/test/resources"
        val text = "Invalid path"
        val characters = text.toList()
        shouldThrow<IOException> {
            toFile(characters, filePath)
        }
    }

    "should throw IllegalArgumentException if extension is not supported" {
        val invalidFilePaths = listOf(
            "text.text",  
            "text.",                 
            "text.pdf",   
            "text.svg",   
            "text.docx",   
            "text.html",    
            "text.xml",  
            "text.csv",    
            "text.json",    
            "text.ai",     
            "text.xsd",   
            "text.gli",    
            "text.gol",       
            ".",                  
            " ",                  
            "",      
        )
        for(invalidFilePath in invalidFilePaths) {
            val file = File(invalidFilePath)
            shouldThrow<IllegalArgumentException> {
                readFile(invalidFilePath)
            }
            File(invalidFilePath).delete()
        }
    }
})