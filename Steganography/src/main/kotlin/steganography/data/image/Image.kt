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
   if(pixels.colorModel.hasAlpha()==false){
       val image = BufferedImage(pixels.width, pixels.height, BufferedImage.TYPE_INT_ARGB)
       val g: Graphics2D = image.createGraphics()
       g.drawImage(pixels, 0, 0, null)
       g.dispose()
       return image
   }
   return pixels
}

/**
 * Converts a JPG image to a PNG BufferedImage.
 *
 * @param path The path of the JPG image file.
 * @return The converted BufferedImage in PNG format.
 * @throws IOException If an error occurs during reading or writing.
 */
private fun convertJPGtoPNG(path: String): BufferedImage {
    val jpgImage: BufferedImage = ImageIO.read(File(path))
    val tempFile = File.createTempFile("temp_image", ".png")
    ImageIO.write(jpgImage, "png", tempFile)
    val pngImage: BufferedImage = ImageIO.read(tempFile)
    tempFile.delete()
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
        return convertJPGtoPNG(path)
    throw IllegalStateException("La extension del archivo no es valida.")
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
    val type = path.substringAfterLast(".").lowercase()
    if (type != "png" && type != "jpg") {
        throw IllegalArgumentException("Tipo de archivo inválido: $type. Solo se admiten archivos de tipo 'png' y 'jpg'.")
    }
    val outputFile = File(path)

    if (!outputFile.parentFile.exists()) {
        throw IllegalArgumentException("El directorio ${outputFile.parent} no existe.")
    }

    try {
        val result = ImageIO.write(image, type, outputFile)
        if (!result) {
            throw IOException("Error al guardar la imagen: no se pudo escribir en el archivo.")
        }
    } catch (e: IOException) {
        throw IOException("Error al intentar guardar la imagen: ${e.message}", e)
    }
}