package steganography.data.image

import steganography.data.image.loadImage
import steganography.data.image.saveImage
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.imageio.IIOException

class ImageTest : StringSpec({

    "should load an image from a valid file path" {
        val filePath = "src/test/resources/test_image.png"
        val image = loadImage(filePath)
        image shouldNotBe null
    }

    "should throw an IIOException for an invalid file path" {
        val filePath = "invalid/path/to/image.png"
        val exception = shouldThrow<IIOException> {
            loadImage(filePath)
        }
        exception.message shouldContain "Can't read input file!"
    }

    "should save an image to the specified file path" {
        val originalFilePath = "src/test/resources/test_image.png"
        val saveFilePath = "src/test/resources/saved_image.png"
        val pixels = loadImage(originalFilePath)

        val result = saveImage(pixels, saveFilePath)
        result shouldBe true

        val savedImageFile = File(saveFilePath)
        savedImageFile.exists() shouldBe true

        savedImageFile.delete()
    }

    "should return false when trying to save an image to an invalid path" {
        val originalFilePath = "src/test/resources/test_image.png"
        val invalidFilePath = "invalid/path/to/save_image.png"
        val pixels = loadImage(originalFilePath)
        val result = saveImage(pixels, invalidFilePath)
        result shouldBe false
    }

    "should maintain the resolution of the test image" {
        val filePath = "src/test/resources/test_image.png"
        val expectedWidth = 360
        val expectedHeight = 360

        testImageResolution(filePath, expectedWidth, expectedHeight)
    }

    "should maintain the resolution of black image" {
        val filePath = "src/test/resources/black-370118_1280.png"
        val expectedWidth = 1280
        val expectedHeight = 822

        testImageResolution(filePath, expectedWidth, expectedHeight)
    }

    "should maintain the resolution of gradient image" {
        val filePath = "src/test/resources/gradient_black_white_144x144.png"
        val expectedWidth = 144
        val expectedHeight = 144

        testImageResolution(filePath, expectedWidth, expectedHeight)
    }

    "should maintain the resolution of penguin image" {
        val filePath = "src/test/resources/images.png"
        val expectedWidth = 290
        val expectedHeight = 174

        testImageResolution(filePath, expectedWidth, expectedHeight)
    }
})

fun testImageResolution(filePath: String, expectedWidth: Int, expectedHeight: Int) {
    val image = loadImage(filePath)
    print(image.size)
    print(image[0].size)
    image!!.size shouldBe expectedHeight
    image[0].size shouldBe expectedWidth
}