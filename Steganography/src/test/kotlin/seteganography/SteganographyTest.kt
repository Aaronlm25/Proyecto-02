package steganography

import steganography.encodeText
import steganography.decodeText
import steganography.data.image.loadImage
import steganography.data.text.readFull
import steganography.data.text.compress
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import java.io.File
import org.apache.commons.math3.stat.inference.ChiSquareTest

class SteganographyTest : StringSpec ({
    lateinit var imageData: MutableList<Array<IntArray>>

    beforeSpec {
        imageData = mutableListOf()
        val pngFiles = File("src/test/resources/images").listFiles { _, name -> name.endsWith(".png") } ?: arrayOf()
        pngFiles.forEach { file: File ->
            val pixels = loadImage(file.path)
            imageData.add(pixels)
        }
    }

    "should throw IllegalStateException if text is too large for a given pixel array" {
        for(pixels in imageData) {
            val width = pixels.size
            val height = pixels[0].size 
            var text = ""
            shouldThrow<IllegalStateException> {
                encodeText(text, pixels)
            }
        }
    }

    "should throw IllegalStateException when text contains invalid characters during encoding" {
        val path = "src/test/resources/text/special_characters.txt"
        val file = File(path)
        val text = file.readText()
        shouldThrow<IllegalStateException> {
            val pixels = loadImage("src/test/resources/images/random_noise_1440x900.png")
            encodeText(text, pixels)
        }
    }

    "should not handle multiple encodings" {
        val text1 = "First encoding"
        val text2 = "Second encoding"
        val pixels = imageData.get(0)
        val encodedImage1 = encodeText(text1, pixels)
        val encodedImage2 = encodeText(text2, encodedImage1)
        val decodedText = decodeText(encodedImage2)
        decodedText shouldBe text2.toList()
    }

    

    "should encode text correctly into the pixel array" {
        val text = readFull("src/test/resources/text/short.txt")
        for(pixels in imageData) {
            val encodedPixels = encodeText(text, pixels)
            encodedPixels shouldNotBe pixels
        }
    }

    "should decode text correctly from the pixel array" {
        val text = readFull("src/test/resources/text/short.txt")
        for(pixels in imageData) {
            val encodedPixels = encodeText(text, pixels)
            decodeText(encodedPixels) shouldBe text
        }
    }

    "should return empty list when decoding empty pixel array" {
        val pixels: Array<IntArray> = arrayOf()
        val decodedText = decodeText(pixels)
        decodedText shouldBe emptyList() 
    }
    
    "should handle some common special characters during encoding and decoding" {
        val text = ("!.+-?¿àèìòù¡¿/!")
        for(pixels in imageData) {
            val encodedPixels = encodeText(text, pixels)
            decodeText(encodedPixels) shouldBe text
        }
    }

    "should changes not be apparent" {
        for(pixels in imageData) {
            val lsbCounts = IntArray(2)
            for (y in 0 until pixels.size) {
                for (x in 0 until pixels[0].size) {
                    val lsb = pixels[y][x] and 1
                    lsbCounts[lsb]++
                }
            }
            val expected = DoubleArray(2) { lsbCounts.sum() / 2.0 }
            val chiSquareTest = ChiSquareTest()
            val pValue = chiSquareTest.chiSquareTest(expected, lsbCounts.map { it.toLong() }.toLongArray())
            pValue shouldBeGreaterThanOrEqual 0.05
        }
    }

    "should handle upper case letters" {
        val text = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        for(pixels in imageData) {
            val encodedPixels = encodeText(text, pixels)
            decodeText(encodedPixels).joinToString("").lowercase() shouldBe text.lowercase()
        }
    }

    "should handle numbers" {
        val text = "0123456789"
        for(pixels in imageData) {
            val encodedPixels = encodeText(text, pixels)
            decodeText(encodedPixels).joinToString("") shouldBe text
        }
    }
})

fun getText(size: Int) : List<Char> {
    val chars = ('A'..'Z') + ('a'..'z') + ' '
    return List(size) { chars.random() }
}