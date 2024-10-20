package steganography

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
        readFile("./resoureces")?.size ?: 0 shouldBeGreaterThan 0
    }

    "the text should be the same" {
        val filePath = "src/test/resources/example.txt"
        val text = File(filePath).readText()
        val finalText = text.trim()
        val characters = mutableListOf<Char>()
        for (char in finalText) {
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

        val text = "test"
        val chars = listOf('t', 'e', 's', 't')

        toFile(chars, filePath)

        val writtenText = file.readText()
        writtenText shouldBe text
    }

    "should create a file" {
        val filePath = "src/test/resources/createTest.txt"
        val file = File(filePath)
        val characters = listOf('t', 'e', 's', 't')

        if (file.exists()) {
            file.delete() 
        }

        toFile(characters, filePath)

        file.exists() shouldBe true
    }

})