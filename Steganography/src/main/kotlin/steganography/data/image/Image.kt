package steganography.data.image

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import java.awt.Graphics2D

/**
 * Loads a PNG image from the specified file path.
 *
 * @param filePath The path of the image file.
 * @return The loaded BufferedImage.
 * @throws IOException If an error occurs during reading.
 */
fun loadImagePNG(filePath: String): BufferedImage {
   val pixels = ImageIO.read(File(filePath))
   return pixels
}

/**
 * Loads a JPEG image from the specified file path and converts it to a PNG image.
 *
 * @param filePath The path of the image file.
 * @return The converted BufferedImage.
 * @throws IOException If an error occurs during reading.
 * @throws IllegalArgumentException If the provided parameters are invalid.
 */
fun loadImageJPEG(inputPath: String): BufferedImage {
    val jpgImage: BufferedImage = ImageIO.read(File(inputPath))

    val pngImage = BufferedImage(jpgImage.width, jpgImage.height, BufferedImage.TYPE_INT_ARGB)

    val g: Graphics2D = pngImage.createGraphics()
    g.drawImage(jpgImage, 0, 0, null)
    g.dispose()

    return pngImage
}

/**
 * Saves the modified image to the specified file path.
 *
 * @param pixels The BufferedImage to save.
 * @param filePath The path where the image should be saved.
 * @param imageType The type of the image (e.g., "png", "jpg").
 * @throws IOException If an error occurs during writing.
 * @throws IllegalArgumentException If the provided parameters are invalid.
 */
fun saveImage(pixels: BufferedImage, filePath: String, imageType: String) {
        ImageIO.write(pixels, imageType, File(filePath))
}