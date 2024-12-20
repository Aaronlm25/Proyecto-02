package steganography

import steganography.encodeText
import steganography.decodeText
import steganography.data.image.loadImage
import steganography.data.image.saveImage
import steganography.data.text.readFile
import kotlin.math.floor
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import java.io.File
import java.awt.image.BufferedImage

class SteganographyTest : StringSpec ({
    lateinit var imageData: MutableList<BufferedImage>
    val testImage: String = "images.png"

    beforeSpec {
        imageData = mutableListOf()
        val pngFiles = File("src/test/resources/images/png").listFiles { _, name -> name.endsWith(".png") } ?: arrayOf()
        pngFiles.forEach { file: File ->
            val image = loadImage(file.path)
            imageData.add(image)
        }
        val jpgFiles = File("src/test/resources/images/jpg").listFiles { _, name -> name.endsWith(".jpg") } ?: arrayOf()
        jpgFiles.forEach { file: File ->
            val image = loadImage(file.path)
            imageData.add(image)
        }
    }

    "should throw IllegalStateException if text is too large for a given image" {
        for(image in imageData) {
            val chars = ('A'..'Z') + ('a'..'z') + ' '
            val size = (getLimit(image))
            val text = List(size) { chars.random() } 
            shouldThrow<IllegalStateException> {
                encodeText(text.toList(), image)
            }
        }
    }

    "should throw IllegalStateException when text contains invalid characters during encoding" {
        val path = "src/test/resources/text/special_characters.txt"
        val file = File(path)
        val text = file.readText()
        shouldThrow<IllegalStateException> {
            val image = loadImage("src/test/resources/images/png/$testImage")
            encodeText(text.toList(), image)
        }
    }

    "should not handle multiple encodings" {
        val text1 = "First encoding"
        val text2 = "Second encoding"
        val image = imageData.get(3)
        val encodedImage1 = encodeText(text1.toList(), image)
        val encodedImage2 = encodeText(text2.toList(), encodedImage1)
        val decodedText = decodeText(encodedImage2)
        decodedText shouldBe text2.toList()
    }

    "should encode text correctly in image" {
        val text = readFile("src/test/resources/text/short.txt")
        for(image in imageData) {
            if(text.size >= getLimit(image))
                continue
            val encodedImage = encodeText(text, image)
            encodedImage shouldNotBeSameInstanceAs image
            encodedImage shouldNotBe image
        }
    }

    "should decode text correctly from image" {
        val text = readFile("src/test/resources/text/short.txt")
        for(image in imageData) {
            if(text.size >= getLimit(image))
                continue
            val encodedImage = encodeText(text, image)
            decodeText(encodedImage) shouldBe text
        }
    }
    
    "should handle some common special characters during encoding and decoding" {
       val text = ("!.-?áéíóú!").toList()
        for(image in imageData) {
            if(text.size >= getLimit(image))
                continue
            val encodedImage = encodeText(text, image)
            decodeText(encodedImage) shouldBe text
        }
    }

    "should changes not be apparent" {
        val text = readFile("src/test/resources/text/short.txt")
        for(image in imageData) {
            if(text.size >= getLimit(image))
                continue
            val encodedImage = encodeText(text, image)
            val originalHistorgram = getLSBHistogram(image)
            val encodedHistogram = getLSBHistogram(encodedImage)
            val channels = originalHistorgram.keys
            for(channel in channels) {
                val origalLSB = originalHistorgram[channel]!!
                val originalBalance = (origalLSB[0]!!.toDouble() / (origalLSB .values.sum())) * 100
                val encodedLSB = encodedHistogram[channel]!!
                val encodedBalance = (encodedLSB[0]!!.toDouble() / (encodedLSB.values.sum())) * 100
                Math.abs(originalBalance - encodedBalance) shouldBeLessThanOrEqual 15.0
            }
        }
    }

    "should handle upper case letters" {
        val text = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        for(image in imageData) {
            if(text.length >= getLimit(image))
                continue
            val encodedImage = encodeText(text.toList(), image)
            decodeText(encodedImage) shouldBe text.toList()
        }
    }

    "should handle numbers" {
        val text = "0123456789".toList()
        for(image in imageData) {
            if(text.size >= getLimit(image))
                continue
            val encodedImage = encodeText(text, image)
            decodeText(encodedImage) shouldBe text
        }
    }
})

private fun getLimit(image : BufferedImage): Int {
    return floor(image.width * image.height / 6.5).toInt() - 10
}

private fun getLSBHistogram(image : BufferedImage): Map<String, Map<Int, Int>> {
    val histogram = mutableMapOf(
        "Red" to mutableMapOf(0 to 0, 1 to 0),
        "Green" to mutableMapOf(0 to 0, 1 to 0),
        "Blue" to mutableMapOf(0 to 0, 1 to 0),
        "Alpha" to mutableMapOf(0 to 0, 1 to 0)
    )
    for (x in 0 until image.width) {
        for (y in 0 until image.height) {
            val pixel = image.getRGB(x, y)
            val alphaLSB = (pixel shr 24) and 1
            val redLSB = (pixel shr 16) and 1
            val greenLSB = (pixel shr 8) and 1
            val blueLSB = pixel and 1
            histogram["Red"]!![redLSB] = histogram["Red"]!![redLSB]!! + 1
            histogram["Green"]!![greenLSB] = histogram["Green"]!![greenLSB]!! + 1
            histogram["Blue"]!![blueLSB] = histogram["Blue"]!![blueLSB]!! + 1
            histogram["Alpha"]!![alphaLSB] = histogram["Alpha"]!![alphaLSB]!! + 1
        }
    }
    return histogram
}

private fun modifyLSB(value: Int, bit: Int): Int {
    return (value and 0xFE) or bit
}
