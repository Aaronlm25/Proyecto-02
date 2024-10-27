package steganography.data.image

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Loads an image from the specified file path.
 *
 * @param filePath The path of the image file.
 * @return 2D arrays of the image pixels
 */
fun loadImage(path: String): Array<IntArray> {
    return arrayOf()
}

/**
 * Saves the modified image to the specified file path.
 *
 * @param pixels The 2D pixel array of the image.
 * @param filePath The path where the image should be saved.
 * @return True if the image was saved successfully, otherwise false.
 */
fun saveImage(pixels: Array<IntArray>, path: String): Boolean {
    return true
}
