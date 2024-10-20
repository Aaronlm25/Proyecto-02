package steganography

import kotlin.text.trim
import steganography.data.text.toFile
import steganography.data.text.readFile
import steganography.data.image.loadImage
import steganography.data.image.saveImage

fun main() {
    var option: String?
    var active = true
    do {
        println("Esteganografia.\n" + 
                "(h) Ocultar texto en imagen.\n" +
                "(u) Develar texto de imagen.\n" +
                "(x) Salir."
        )
        option = readLine()?.trim()
        try {
            when (option) {
                "h" -> {
                    println("Proporcione la ruta del archivo con el texto a ocultar.")
                    val textPath = readNonNullInput()
                    println("Proporcione la ruta de la imagen donde se ocultarà el texto.")
                    val imagePath = readNonNullInput()
                    println("Proporcione la ruta de la imagen resultante.")
                    val resultPath = readNonNullInput()
                    val text = readFile(textPath)
                    val pixels = loadImage(imagePath)
                    val encoded = encodeText(text, pixels)
                    saveImage(encoded, resultPath)
                    println("Se ha guardado la imagen con el mensaje en : " + resultPath)
                }
                "u" -> {
                    println("Propocione la ruta de la imagen que contiene los datos ocultos.")
                    val imagePath = readNonNullInput()
                    println("Proporcione el nombre del archivo en el que se guardarà el texto develado.")
                    val resultPath = readNonNullInput()
                    val pixels = loadImage(imagePath)
                    val text = decodeText(pixels)
                    toFile(text, resultPath)
                    println("Se ha decodificado el mensaje en la imagen en : " + resultPath)
                }
                "x" -> {
                    active = false
                    println("El programa ha terminado.")
                }
            }
        } catch(iae : IllegalArgumentException) {
            println(iae.message)
        }
    } while(active)
}

fun readNonNullInput(): String {
    return readLine()?.trim() ?: throw IllegalArgumentException("Todos los parametros son necesarios")
}