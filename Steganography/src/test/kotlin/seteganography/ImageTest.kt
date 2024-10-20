package steganography

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
        val image = loadImage(originalFilePath) as BufferedImage

        val result = saveImage(image, saveFilePath)
        result shouldBe true

        // Verify the saved image exists
        val savedImageFile = File(saveFilePath)
        savedImageFile.exists() shouldBe true

        // Clean up
        savedImageFile.delete()
    }

    "should return false when trying to save an image to an invalid path" {
        val originalFilePath = "src/test/resources/test_image.png"
        val invalidFilePath = "invalid/path/to/save_image.png"
        val image = loadImage(originalFilePath) as BufferedImage

        val result = saveImage(image, invalidFilePath)
        result shouldBe false
    }
})