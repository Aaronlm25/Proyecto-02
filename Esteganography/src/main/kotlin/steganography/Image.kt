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
}