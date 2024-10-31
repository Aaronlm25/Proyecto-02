package steganography

import steganography.encodeText
import steganography.decodeText
import steganography.data.image.loadImagePNG
import steganography.data.image.saveImage
import steganography.data.image.loadImageJPEG
import steganography.data.text.readFile
import steganography.data.text.replaceAlphabet
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import java.io.File
import java.awt.image.BufferedImage
import org.apache.commons.math3.stat.inference.ChiSquareTest

class SteganographyTest : StringSpec ({
    lateinit var imageData: MutableList<BufferedImage>

    beforeSpec {
        imageData = mutableListOf()
        val pngFiles = File("src/test/resources/images").listFiles { _, name -> name.endsWith(".png") } ?: arrayOf()
        pngFiles.forEach { file: File ->
            val pixels = loadImagePNG(file.path)
            imageData.add(pixels)
        }
    }

    "should throw IllegalStateException if text is too large for a given pixel array" {
        for(pixels in imageData) {
            val width = pixels.getHeight()
            val height = pixels.getWidth()
            val text = CharArray(width * height * 3) { 'A' }.concatToString()
            shouldThrow<IllegalStateException> {
                encodeText(text.toList(), pixels)
            }
        }
    }

    "should throw IllegalStateException when text contains invalid characters during encoding" {
        val path = "src/test/resources/text/special_characters.txt"
        val file = File(path)
        val text = file.readText()
        shouldThrow<IllegalStateException> {
            val pixels = loadImagePNG("src/test/resources/images/random_noise_1440x900.png")
            encodeText(text.toList(), pixels)
        }
    }

    "should not handle multiple encodings" {
        val text1 = "First encoding"
        val text2 = "Second encoding"
        val pixels = imageData.get(0)
        val encodedImage1 = encodeText(text1.toList(), pixels)
        val encodedImage2 = encodeText(text2.toList(), encodedImage1)
        val decodedText = decodeText(encodedImage2)
        decodedText shouldBe text2.toList()
    }

    

    "should encode text correctly into the pixel array" {
        val text = readFile("src/test/resources/text/short.txt")
        for(pixels in imageData) {
            val encodedPixels = encodeText(text, pixels)
            encodedPixels shouldNotBe pixels
        }
    }

    "should decode text correctly from the pixel array" {
        val text = readFile("src/test/resources/text/short.txt")
        for(pixels in imageData) {
            val encodedPixels = encodeText(text, pixels)
            decodeText(encodedPixels) shouldBe text
        }
    }

    "should return empty list when decoding empty pixel array" {
        val image = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
        val decodedText = decodeText(image)
        decodedText shouldBe emptyList() 
    }
    
    "should handle some common special characters during encoding and decoding" {
        val text = ("!.+-?¿àèìòù¡¿/!").toList()
        for(pixels in imageData) {
            val encodedPixels = encodeText(text, pixels)
            decodeText(encodedPixels) shouldBe text
        }
    }

    "should changes not be apparent" {
        for(pixels in imageData) {
            val lsbCounts = IntArray(2)
            for (y in 0 until pixels.getHeight()) {
                for (x in 0 until pixels.getWidth()) {
                    val lsb = pixels.getRGB(y, x) and 1
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
            val encodedPixels = encodeText(text.toList(), pixels)
            decodeText(encodedPixels).joinToString("").lowercase() shouldBe text.lowercase()
        }
    }

    "should handle numbers" {
        val text = "0123456789"
        for(pixels in imageData) {
            val encodedPixels = encodeText(text.toList(), pixels)
            decodeText(encodedPixels).joinToString("") shouldBe text
        }
    }

    "should save the seeds in the top right corner even after encoding" {
        val pixels = loadImagePNG("src/test/resources/images/random_noise_3500x5000.png")
        val seed = pixels.getRGB(0, 0)
        val text = readFile("src/test/resources/text/large.txt")
        val encoded = encodeText(text, pixels)
        saveImage(encoded, "src/test/resources/images/seed_encoded.png", "PNG")
        val encodedPixels = loadImagePNG("src/test/resources/images/seed_encoded.png") 
        encodedPixels.getRGB(0, 0) shouldBe seed
    }

    "should throw IllegalStateException if no sedd is found on the upper right corner" {
        val image = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
        shouldThrow<IllegalStateException> {
            decodeText(image)
        }

})

fun getText(size: Int) : List<Char> {
    val chars = ('A'..'Z') + ('a'..'z') + ' '
    return List(size) { chars.random() }
}