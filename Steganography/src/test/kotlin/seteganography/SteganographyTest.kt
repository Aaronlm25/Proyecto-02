package steganography

import steganography.encodeText
import steganography.decodeText
import steganography.data.image.loadImage
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.assertions.throwables.shouldThrow
import java.io.File

class SteganographyTest : StringSpec ({
    "should correctly implement LSB encoding" {

    }

    "should correctly encrypt text using Caesar cipher" {

    }

    "should throw IllegalStateException if text is too large for a given pixel array" {
        val directoryPath = "src/test/resources"
        val pngFiles = File(directoryPath).listFiles { _, name -> name.endsWith(".png") } ?: arrayOf()
        pngFiles.forEach { file ->
            val pixels = loadImage(file.path)
            val width = pixels.size
            val height = pixels[0].size 
            var text = getText(width * height + 10)
            shouldThrow<IllegalStateException> {
                encodeText(text.toList(), pixels)
            }
        }
    }

    "should throw IllegalStateException when text contains invalid characters during encoding" {
        val path = "src/test/resources/invalidText.txt"
        val file = File(path)
        val text = file.readText()
        shouldThrow<IllegalStateException> {
            val pixels = arrayOf(intArrayOf(0))
            encodeText(text.toList(), pixels)
        }
    }

    "should not handle multiple encodings" {
        val text1 = "First encoding"
        val text2 = "Second encoding"
        val pixels = Array(100) { IntArray(100) }
        val encodedImage1 = encodeText(text1.toList(), pixels)
        val encodedImage2 = encodeText(text2.toList(), encodedImage1)
        val decodedText = decodeText(encodedImage2)
        decodedText shouldBe text2.toList()
    }

    "should encode text correctly into the pixel array" {
        val text = "Hello"
        val pixels = Array(1) { IntArray(5) { 0 } } 
        val encodedPixels = encodeText(text.toList(), pixels)
        encodedPixels shouldNotBe pixels 
    }

    "should decode text correctly from the pixel array" {
        val originalText = "Hello"
        val pixels = Array(1) { IntArray(5) { 0 } }
        val encodedPixels = encodeText(originalText.toList(), pixels)
        val decodedText = decodeText(encodedPixels)
        decodedText shouldBe originalText.toList()
    }

    "should return empty list when decoding empty pixel array" {
        val pixels = Array(1) { IntArray(5) { 0 } }
        val decodedText = decodeText(pixels)
        decodedText shouldBe emptyList() 
    }
    
    "should handle special characters during encoding and decoding" {
        val originalText = "Hello, World! 🌍"
        val pixels = Array(1) { IntArray(5) { 0 } }
        val encodedPixels = encodeText(originalText.toList(), pixels)
        val decodedText = decodeText(encodedPixels)
        decodedText shouldBe originalText.toList()
    }
})

fun getText(size: Int) : List<Char> {
    val chars = ('A'..'Z') + ('a'..'z') + ' '
    return List(size) { chars.random() }
}
