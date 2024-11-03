package steganography.data.image

import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import java.awt.Graphics2D

/**
 * Loads a PNG image from the specified file path.
 *
 * @param filePath The path of the image file.
 * @return The loaded BufferedImage.
 * @throws IOException If an error occurs during reading.
 */
private fun loadImagePNG(path: String): BufferedImage {
   val pixels = ImageIO.read(File(path))
   return pixels
}

/**
 * Loads a JPG image from the specified file path and converts it to a PNG image.
 *
 * @param path The path of the image file.
 * @return The converted BufferedImage.
 * @throws IOException If an error occurs during reading.
 * @throws IllegalArgumentException If the provided parameters are invalid.
 */
private fun loadImageJPG(path: String): BufferedImage {
    val jpgImage: BufferedImage = ImageIO.read(File(path))
    val pngImage = BufferedImage(jpgImage.width, jpgImage.height, BufferedImage.TYPE_INT_ARGB)
    val g: Graphics2D = pngImage.createGraphics()
    g.drawImage(jpgImage, 0, 0, null)
    g.dispose()
    return pngImage
}

/**
 * Loads an image from the specified file path.
 * 
 * Handles PNG and JPG.
 *
 * @param path The path of the image file.
 * @return A BufferedImage object representing the loaded image.
 * @throws IllegalStateException If the provided image is not png or jpg.
 */
fun loadImage(path: String): BufferedImage {
    val type = path.substringAfterLast(".")
    if(type == "png")
        return loadImagePNG(path)
    else if(type == "jpg")
        return loadImageJPG(path)
    throw IllegalStateException("Tipo de archivo inválido.")
}


/**
 * Saves the modified image to the specified file path.
 *
 * @param image as buffered image.
 * @param path The path where the image should be saved.
 * @throws IOException If an error occurs during writing.
 * @throws IllegalArgumentException If the provided parameters are invalid.
 */
fun saveImage(image: BufferedImage, path: String) {
    val type = path.substringAfterLast(".")
    ImageIO.write(image, type, File(path))
}