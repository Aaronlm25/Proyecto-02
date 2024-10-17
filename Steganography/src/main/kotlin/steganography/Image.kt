package steganography

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
/**
 * Image
 *
 * This class process an image in format png and ___.
 *
 * @property image The image to be processed.
 */
class ImageHandler(private val imagePath : String) {
    private val image : BufferedImage = ImageIO.read(File(imagePath))

    /**
     * Loads an image from the specified file path.
     *
     * @param filePath The path of the image file.
     * @return The loaded image object.
     */
    fun loadImage(filePath: String): Image {
        // Implementation
    }

    /**
     * Saves the modified image to the specified file path.
     *
     * @param image The image to be saved.
     * @param filePath The path where the image should be saved.
     * @return True if the image was saved successfully, otherwise false.
     */
    fun saveImage(image: Image, filePath: String): Boolean {
        // Implementation
    }
    
}