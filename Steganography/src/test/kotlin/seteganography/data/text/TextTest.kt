package steganography.data.text

import steganography.data.text.readFile
import steganography.data.text.toFile
import steganography.data.text.compressText
import steganography.data.text.genratePatterns
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.collections.shouldContain
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class TextTest : StringSpec ({

    "the list should be no empty" {
        readFile("src/test/resources/example.txt",1).size shouldBeGreaterThan 0
    }

    "the list should be the same" {
        val filePath = "src/test/resources/example.txt"
        val text = File(filePath).readText()
        val characters = mutableListOf<Char>()
        for (char in text) {
            characters.add(char) 
        }
        characters shouldBe readFile(filePath,1)
    }

    "should write a simple text on the file" {
        val filePath = "src/test/resources/writeTest.txt"
        val file = File(filePath)

        file.createNewFile()
        file.setWritable(true)

        val text = "this is the write test"
        val chars = text.toList()

        toFile(chars, filePath)

        val writtenText = file.readText()
        writtenText shouldBe text

        file.delete();
    }

    "should write a text with special characters on the file" {
        val filePath = "src/test/resources/writeTest.txt"
        val file = File(filePath)

        file.createNewFile()
        file.setWritable(true)

        val text = "¿¡this is, - the write test with_special °! ch@r@ct3rs []]{}"
        val chars = text.toList()

        toFile(chars, filePath)

        val writtenText = file.readText()
        writtenText shouldBe text

        file.delete();
    }

    "should write a text with more spaces between words on the file" {
        val filePath = "src/test/resources/writeTest.txt"
        val file = File(filePath)

        file.createNewFile()
        file.setWritable(true)

        val text = "th     is is the    test o f the tex t  with  morespaces between     w o r d s"
        val chars = text.toList()

        toFile(chars, filePath)

        val writtenText = file.readText()
        writtenText shouldBe text

        file.delete();
    }

    "should write a text with more than one line on the file" {
        val filePath = "src/test/resources/writeTest.txt"
        val file = File(filePath)

        file.createNewFile()
        file.setWritable(true)

        val text = "\n this \n\n is \n the \n text \n in \n\n\n lines"
        val chars = text.toList()

        toFile(chars, filePath)

        val writtenText = file.readText()
        writtenText shouldBe text

        file.delete()
    }

    "should write a text with more than one line on the file" {
        val filePath = "src/test/resources/writeTest.txt"
        val file = File(filePath)

        file.createNewFile()
        file.setWritable(true)

        val text = "\n this \n\n is \n the \n text \n in \n\n\n lines"
        val chars = text.toList()

        toFile(chars, filePath)

        val writtenText = file.readText()
        writtenText shouldBe text

        file.delete()
    }

    "should write a text that contains numbers" {
        val filePath = "src/test/resources/writeTest.txt"
        val file = File(filePath)

        file.createNewFile()
        file.setWritable(true)

        val text = "The number is 123456789 10 1 1"
        val chars = text.toList()

        toFile(chars, filePath)

        val writtenText = file.readText()
        writtenText shouldBe text

        file.delete()
    }

    "should write a text of numbers" {
        val filePath = "src/test/resources/writeTest.txt"
        val file = File(filePath)

        file.createNewFile()
        file.setWritable(true)

        val text = " 3 56 123456789 10 1 1"
        val chars = text.toList()

        toFile(chars, filePath)

        val writtenText = file.readText()
        writtenText shouldBe text

        file.delete()
    }

    "should create a file" {
        val filePath = "src/test/resources/createTest.txt"
        val file = File(filePath)
        val text = "this is the create test"
        val characters = text.toList()

        if (file.exists()) {
            file.delete() 
        }

        toFile(characters, filePath)

        file.exists() shouldBe true
        file.isFile shouldBe true

        file.delete()
    }

    "should throw an exception when the file doesn't exist" {
        val filePath = "src/test/resources/non-existent.txt"
        val exception = shouldThrow<FileNotFoundException> {
            readFile(filePath,1)
        }
    }

    "should throw an exception when the path is invalid" {
        val filePath = "src/test/resources"
        val text = "Invalid path"
        val characters = text.toList()
        val exception = shouldThrow<IOException> {
            toFile(characters, filePath)
        }
    }

    // "should throw an exception when the file cannot be read" {
    //    val tempFile = File.createTempFile("non-readable", ".txt")

    //     tempFile.writeText("This text cannot be read")
    //     tempFile.setReadable(false, false)

    //     val exception = shouldThrow<SecurityException> {
    //         readFile(tempFile.path,1)
    //     }

    //     tempFile.setReadable(true, true)

    //     tempFile.delete()
    // }

})