package steganography

import steganography.encodeText
import steganography.decodeText
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class SteganographyTest : StringSpec ({
    "should encode text be clean" {
        0 shouldBe 0
    }

    "should decode text correctly" {
        0 shouldBe 0
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