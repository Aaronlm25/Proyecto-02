package steganography

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.assertions.throwables.shouldThrow
import java.io.File
import javax.imageio.ImageIO
import java.io.IOException
import java.awt.image.BufferedImage
import steganography.data.image.loadImagePNG
import steganography.data.image.saveImage

class ImageTest : StringSpec({

    val directory = "src/test/resources/images"
    lateinit var imagePaths: List<String>

    beforeSpec {
        imagePaths = listPngFiles(directory)
    }

    "should image pixels not be changed" {
        for (path in imagePaths) {
            val image = ImageIO.read(File(path))
            val imagePixels = loadImagePNG(path)
            var imageName = path.substringAfterLast("/images")
            imageName = imageName.substringBeforeLast(".png")
            val savePath = "$directory/${imageName}_saved.png"
            saveImage(imagePixels, savePath, "PNG") shouldBe true
            val savedImage = ImageIO.read(File(savePath))
            val savedPixels = loadImagePNG(savePath)
            savedPixels.getHeight() shouldBe imagePixels.getHeight()
            savedPixels.getWidth() shouldBe imagePixels.getWidth()
            for (y in 0 until imagePixels.height) {
                for (x in 0 until imagePixels.width) {
                    image.getRGB(x, y) shouldBe savedImage.getRGB(x, y)
                }
            }
        }
    }

    "should load an image from a valid file path" {
        val filePath = "$directory/random_noise_16x16.png"
        val image = loadImagePNG(filePath)
        image shouldNotBe emptyArray<IntArray>()
    }

    "should throw an IOException when trying to load from an invalid file path" {
        val filePath = "invalid/path/to/image.png"
        shouldThrow<IOException> {
            loadImagePNG(filePath)
        }
    }

    "should save an image to the specified file path" {
        val originalFilePath = "$directory/random_noise_16x16.png"
        val saveFilePath = "$directory/random_noise_16x16_saved.png"
        val pixels = loadImagePNG(originalFilePath)
        val result = saveImage(pixels, saveFilePath, "PNG")
        result shouldBe true
        val savedImageFile = File(saveFilePath)
        savedImageFile.exists() shouldBe true
        savedImageFile.delete()
    }

    "should return false when trying to save an image to an invalid path" {
        val originalFilePath = "$directory/random_noise_16x16.png"
        val invalidFilePath = "invalid/path/to/save_image.png"
        val pixels = loadImagePNG(originalFilePath)
        val result = saveImage(pixels, invalidFilePath, "PNG")
        result shouldBe false
    }

    afterSpec {
        val dir = File(directory)
        if (dir.exists() && dir.isDirectory) {
            dir.listFiles { file -> file.name.endsWith("_saved.png") }?.forEach { file ->
            file.delete()
            }
        }
    }
})

fun listPngFiles(directory: String): List<String> {
    val dir = File(directory)
    val pngFiles = mutableListOf<String>()
    if (dir.exists() && dir.isDirectory) {
        dir.listFiles { file -> file.extension == "png" }?.forEach { file ->
            pngFiles.add(file.absolutePath)
        }
    }
    return pngFiles
}
