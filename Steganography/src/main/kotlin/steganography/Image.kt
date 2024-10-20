package steganography

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Loads an image from the specified file path.
 *
 * @param filePath The path of the image file.
 * @return 2D arrays of the image pixels, or null if the file path is invalid.
 */
fun loadImage(filePath: String): Array<IntArray>? {
    return try {
        val image: BufferedImage = ImageIO.read(File(filePath))
        val width = image.width
        val height = image.height
        val pixels = Array(height) { IntArray(width) }

        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[y][x] = image.getRGB(x, y)
            }
        }

        pixels
    } catch (e: Exception) {
        null
    }
}

/**
 * Saves the modified image to the specified file path.
 *
 * @param pixels The 2D pixel array of the image.
 * @param filePath The path where the image should be saved.
 * @return True if the image was saved successfully, otherwise false.
 */
fun saveImage(pixels: Array<IntArray>?, filePath: String): Boolean {
    if (pixels == null || pixels.isEmpty()) return false

    val height = pixels.size
    val width = pixels[0].size
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    for (y in 0 until height) {
        for (x in 0 until width) {
            image.setRGB(x, y, pixels[y][x])
        }
    }

    return try {
        ImageIO.write(image, "png", File(filePath))
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}   