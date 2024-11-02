package steganography

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldNotThrowAny
import java.io.File
import javax.imageio.ImageIO
import java.io.IOException
import java.awt.image.BufferedImage
import steganography.data.image.loadImage
import steganography.data.image.saveImage
import kotlin.collections.emptyList
import kotlin.collections.MutableList

class ImageTest : StringSpec({
    val imageDirectory = "src/test/resources/images"
    val pngDirectory = "$imageDirectory/png"
    val jpgDirectory = "$imageDirectory/jpg"
    lateinit var images: MutableMap<String, MutableList<String>>

    beforeSpec {
        images = mutableMapOf(
            pngDirectory to mutableListOf(),
            jpgDirectory to mutableListOf()
        )
        File(pngDirectory).listFiles { file -> file.extension == "png" }?.forEach { file ->
            images[pngDirectory]!!.add(file.absolutePath)
        }
        File(jpgDirectory).listFiles { file -> file.extension == "jpg" }?.forEach { file ->
            images[jpgDirectory]!!.add(file.absolutePath)
        }
    }

    "should image pixels not be changed" {
        for ((directory, imagePaths) in images) {
            for (path in imagePaths) {
                val originalImage = ImageIO.read(File(path))
                val image = loadImage(path)
                val extension = path.substringAfterLast(".")
                var imageName = path.substringAfterLast("/images")
                imageName = imageName.substringBeforeLast(".")
                val savePath = "$directory/${imageName}_saved.$extension"
                saveImage(image, savePath)
                val savedImage = ImageIO.read(File(savePath))
                savedImage.width shouldBe image.width
                savedImage.height shouldBe image.height
                for (y in 0 until image.height) {
                    for (x in 0 until image.width) {
                        originalImage.getRGB(x, y) shouldBe savedImage.getRGB(x, y)
                    }
                }
            }
        }
    }

    "should load an image from a valid file path" {
        for ((directory, imagePaths) in images) {
            for (path in imagePaths) {
                val image = loadImage(path)
                image shouldNotBe null
            }
        }
    }

    "should throw an IOException when trying to load from an invalid file path" {
        val invalidPath = "invalid/path/to/image.png"
        val invalidFormat = "$pngDirectory/image.jpeg"
        shouldThrow<IOException> {
            loadImage(invalidPath)
        }
        shouldThrow<IllegalStateException> {
            loadImage(invalidFormat)
        }
        
    }

    "should save an image to the specified file path" {
        for ((directory, imagePaths) in images) {
            for (path in imagePaths) {
                val extension = path.substringAfterLast(".")
                val saveFilePath = "$directory/saved_image.$extension"
                val image = loadImage(path)
                saveImage(image, saveFilePath)
                val savedImageFile = File(saveFilePath)
                savedImageFile.exists() shouldBe true
                savedImageFile.delete()
            }
        }
    }

    "should return false when trying to save an image to an invalid path" {
        for ((directory, imagePaths) in images) {
            for (path in imagePaths) {
                val invalidFilePath = "invalid/path/to/save_image.png"
                val image = loadImage(path)
                shouldThrow<IOException> {
                    saveImage(image, invalidFilePath)
                }
            }
        }
    }

    "should throw IllegalArgumentException if extension is not supported" {
        val invalidFilePaths = listOf(
            "saved_image.jpeg",  
            "saved_image.",                 
            "saved_image.gif",   
            "saved_image.svg",   
            "saved_image.bmp",   
            "saved_image.heic",    
            "saved_image.tiff",  
            "saved_image.raw",    
            "saved_image.psd",    
            "saved_image.ai",     
            "saved_image.webp",   
            "saved_image.dng",    
            "saved_image.cr2",       
            ".",                  
            " ",                  
            "",      
        )
        val path = images[pngDirectory]!!.get(0)
        val image = loadImage(path)
        for(invalidFilePath in invalidFilePaths) {
            shouldThrow<IllegalArgumentException> {
                saveImage(image, invalidFilePath)
            }
            File(invalidFilePath).delete()
        }
        for ((directory, imagePaths) in images) {
            shouldNotThrowAny {
                val extension = path.substringAfterLast(".")
                val saveFilePath = "$directory/saved_image.$extension"
                saveImage(image, saveFilePath)
            }
        }
    }

    afterSpec {
        File(pngDirectory).listFiles { file -> file.extension == "_saved.png" }?.forEach { file ->
            file.delete()
        }
        File(jpgDirectory).listFiles { file -> file.extension == "_saved.jpg" }?.forEach { file ->
            file.delete()
        }
    }
})
