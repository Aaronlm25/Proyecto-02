package steganography.data.text

import steganography.data.text.readFile
import steganography.data.text.toFile
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.ints.shouldBeGreaterThan
import java.io.File
import java.io.IOException

class TextTest : StringSpec ({
    "the list should be no empty" {
        readFile("src/test/resources/example.txt").size shouldBeGreaterThan 0
    }

    "the list should be the same" {
        val filePath = "src/test/resources/example.txt"
        val text = File(filePath).readText()
        val characters = mutableListOf<Char>()
        for (char in text) {
            characters.add(char) 
        }
        characters shouldBe readFile(filePath)
    }

    "should throw an exception when the text have special characters" {
        val exception = shouldThrow<IllegalArgumentException> {
            readFile("src/test/resources/invalid.txt")
        }
        exception.message shouldBe "The text contains special characters"
    }

    "should write on the file" {
        val filePath = "src/test/resources/writeTest.txt"
        val file = File(filePath)

        file.createNewFile()
        file.setWritable(true)

        val text = "this is the write test"
        val chars = listOf('t','h','i','s',' ','i','s',' ','t','h','e',' ','w','r','i','t','e',' ','t','e','s','t')

        toFile(chars, filePath)

        val writtenText = file.readText()
        writtenText shouldBe text
    }

    "should create a file" {
        val filePath = "src/test/resources/createTest.txt"
        val file = File(filePath)
        val characters = listOf('t','h','i','s',' ','i','s',' ','t','h','e',' ','c','r','e','a','t','e', ' ','t','e','s','t')

        if (file.exists()) {
            file.delete() 
        }

        toFile(characters, filePath)

        file.exists() shouldBe true
    }

    "should throw an exception when the parameter isn't a list of characters" {
        val filePath = "src/test/resources/writeTest.txt"
        val invalidList = listOf(1,2,3,4,5,6,7,8,9,0)
        val exception = shouldThrow<ClassCastException> {
            toFile(invalidList, filePath)
        }
        exception.message shouldBe "The parameter is not a list of characters"
    }

    "should throw an exception when the file doesn't exist" {
        val filePath = "src/test/resources/non-existent.txt"
        val exception = shouldThrow<IOException> {
            readFile(filePath)
        }
        exception.message shouldBe "File not found"
    }

    "should throw an exception when the file cannot be read" {
        val filePath = "src/test/resources/non-readable.txt"
        val file = File(filePath)
        file.createNewFile()

        file.writeText("This text cannot be read")

        file.setReadable(false)

        val exception = shouldThrow<IOException> {
            readFile(filePath)
        }
        exception.message shouldBe "File cannot be read"

        file.delete()
    }

})