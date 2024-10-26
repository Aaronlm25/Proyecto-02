package steganography.data.image

import steganography.data.image.loadImage
import steganography.data.image.saveImage
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.file.emptyFile
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.nio.file.Files
import kotlin.collections.listOf
import kotlin.collections.emptyList
import javax.imageio.ImageIO

class ImageTest : StringSpec({

    lateinit var imagePaths: MutableList<String>
    val directory: String = "src/test/resources/images"
    val directoryToTest: String = "random_noise"

    beforeSpec {
        val dirPath = Paths.get(directory + directoryToTest)
        imagePaths = mutableListOf<String>()
        Files.newDirectoryStream(dirPath, "*.png").use { stream ->
            stream.forEach { path ->
                imagePaths.add(path.toString())
            }
        }
    }

    "should image size not be changed" {
        for(path in imagePaths) {
            val image = ImageIO.read(File(path))
            val imagePixels = loadImage(path)
            imagePixels[0].size shouldBe image.getHeight()
            imagePixels.size shouldBe image.getWidth()
        }
    }

    "should image pixels not be changed" {
        for(path in imagePaths) {
            val image = ImageIO.read(File(path))
            val imagePixels = loadImage(path)
            val imageName = path.substringAfterLast(directoryToTest + "/")
            val savePath = directory + imageName + ".png"
            saveImage(imagePixels, savePath) shouldBe true
            val savedImage = ImageIO.read(File(savePath))
            val imageSavedPixels = loadImage(savePath)
            imageSavedPixels[0].size shouldBe imagePixels[0].size
            imageSavedPixels.size shouldBe imagePixels.size
            for(y in 0..imagePixels.size) {
                for(x in 0..imagePixels[0].size) {
                    image.getRGB(x, y) shouldBe savedImage.getRGB(x, y)
                }
            }
        }
    }

    "should load an image from a valid file path" {
        val filePath = directory + directoryToTest + "/random_noise_16x16.png"
        val image = loadImage(filePath)
        image shouldNotBe emptyArray<IntArray>()
    }

    "should throw an IOException when trying to load from an invalid file path" {
        val filePath = "invalid/path/to/image.png"
        shouldThrow<IOException> {
            loadImage(filePath)
        }
    }

    "should save an image to the specified file path" {
        val originalFilePath = directory + directoryToTest + "/random_noise_16x16.png"
        val saveFilePath = directory + "random_noise_saved_16x16.png"
        val pixels = loadImage(originalFilePath)
        val result = saveImage(pixels, saveFilePath)
        result shouldBe true
        val savedImageFile = File(saveFilePath)
        savedImageFile.exists() shouldBe true
        savedImageFile.delete()
    }

    "should return false when trying to save an image to an invalid path" {
        val originalFilePath = directory + directoryToTest + "/random_noise_16x16.png"
        val invalidFilePath = "invalid/path/to/save_image.png"
        val pixels = loadImage(originalFilePath)
        val result = saveImage(pixels, invalidFilePath)
        result shouldBe false
    }

    afterSpec {
        val dir = File(directory)
        if (dir.exists() && dir.isDirectory) {
            dir.listFiles { file -> file.extension == "png" }?.forEach { file ->
                file.delete()
            }
        }
    }
})