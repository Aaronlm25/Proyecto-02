package steganography

import steganography.encodeText
import steganography.decodeText
import steganography.data.image.loadImage
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.assertions.throwables.shouldThrow
import kotlin.random.Random
import java.io.File
import kotlinx.coroutines.handleCoroutineException

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
            var textSpecial = getTextSpecialCharacters(width * height + 10)
            shouldThrow<IllegalStateException> {
                encodeText(text.toList(), pixels)
            }
            shouldThrow<IllegalStateException> {
                encodeText(textSpecial.toList(), pixels)
            }
        }
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

fun getTextSpecialCharacters(size: Int) : List<Char> {
    val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + "!@#$%^&*()-_=+[]{}|;:'\",.<>?/\\`~ "
    return List(size) { chars.random() as Char}
}

