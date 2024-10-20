package steganography

import steganography.data.image.loadImage
import steganography.data.image.saveImage
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImageTest : StringSpec({

    "should load an image from a valid file path" {
        val filePath = "src/test/resources/test_image.png"
        val image = loadImage(filePath)
        image shouldNotBe null
    }

    "should return null for an invalid file path" {
        val filePath = "invalid/path/to/image.png"
        val image = loadImage(filePath)
        image shouldBe null
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

    "should maintain the resolution of the loaded image" {
        val filePath = "src/test/resources/test_image.png"
        val expectedWidth = 360
        val expectedHeight = 360

        val image = loadImage(filePath)
        image!!.size shouldBe expectedWidth
        image[0].size shouldBe expectedHeight
    }
})