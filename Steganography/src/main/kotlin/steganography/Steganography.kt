package steganography

/**
 * Encodes the text into an image represented by an integer array of the pixels.
 *
 * @param text The text to be encoded.
 * @param path The 2D pixel array of the image.
 * @param The encoded text in the 2D pixel array of the image.
 */
fun encodeText(text: List<Char>, pixels : Array<IntArray>): Array<IntArray> {
    // Implementation
    return pixels
}

/**
 * Decodes an image represented by an integer array of the pixels into the 
 * original text.
 *
 * @param pixels The 2D pixel array of the image.
 * @return The decoded text.
 */
fun decodeText(pixels : Array<IntArray>): List<Char> {
    // Implementation
    return emptyList()
}