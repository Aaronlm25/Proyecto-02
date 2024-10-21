package steganography

/**
 * Encodes the text into an image represented by an integer array of the pixels.
 *
 * @param text The text to be encoded.
 * @param path The 2D pixel array of the image.
 * @returns 2D pixel array with the message encoded.
 */
fun encodeText(text: List<Char>, pixels : Array<IntArray>): Array<IntArray> {
    // Implementation
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
}
