package steganography

import kotlin.text.trim
import steganography.data.text.toFile
import steganography.data.text.readFile

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
        when (option) {
            "h" -> {
                println("Proporcione la ruta del archivo con el texto a ocultar.")
                val textPath = readLine()?.trim()
                println("Proporcione la ruta de la imagen donde se ocultarà el texto.")
                val imagePath = readLine()?.trim()
                println("Proporcione la ruta de la imagen resultante.")
                val resultPath = readLine()?.trim()
            }
            "u" -> {
                println("Propocione la ruta de la imagen que contiene los datos ocultos.")
                val imagePath = readLine()?.trim()
                println("Proporcione el nombre del archivo en el que se guardarà el texto develado.")
                val resultPath = readLine()?.trim()
            }
            "x" -> {
                active = false
                println("El programa ha terminado.")
            }
        }
    } while(active)
}