package steganography

/**
 * Encodes the text into an image represented by an integer array of the pixels.
 *
 * @param text The text to be encoded.
 * @param pixels The 2D pixel array of the image.
 * @return 2D pixel array with the message encoded.
 * @throws IllegalStateException if the text is too large for the pixels array.
 */
fun encodeText(text: List<Any>, pixels : Array<IntArray>): Array<IntArray> {
    // Implementation
    return pixels
}

/** 
 * Decodes an image represented by an integer array of the pixels into the 
 * original text.
 * Handles letters (a-z) and (A-Z) numbers (0-9) and some special characters :
 * (!.+-?¿àèìòù¡¿/!)
 *
 * @param pixels The 2D pixel array of the image.
 * @return The decoded text.
 * @throws IllegalStateException if no key is found on the pixels array.
 */
@Throws(IllegalStateException::class)
fun decodeText(pixels: Array<IntArray>): List<Char> {
    // Implementation
    return emptyList()
}
