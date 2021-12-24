package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

fun tryToReadImage(inputFile: String): BufferedImage? {
    return try {
        ImageIO.read(File(inputFile))
    } catch (e: IOException) {
        println("Can't read input file!")
        null
    }
}

fun tryToHideMessage(message: ByteArray, image: BufferedImage): BufferedImage? {
    return if (!isImageBigEnough(message, image)){
        println("The input image is not large enough to hold this message.")
        null
    } else hideMessageInImage(message, image)
}

private fun isImageBigEnough(message: ByteArray, image: BufferedImage): Boolean {
    return image.width * image.height >= message.size * 8
}

private fun hideMessageInImage(message: ByteArray, image: BufferedImage): BufferedImage {
    var bitCounter = 0
    var byteCounter = 0
    var isProcessingDone = false
    val output = BufferedImage(image.width, image.height, image.type)
    loop@for (y in 0 until output.height) {
        for (x in 0 until output.width) {
            val color = Color(image.getRGB(x, y))
            if (isProcessingDone) {
                output.setRGB(x, y, color.rgb)
            } else {
                val processedColor = processPixel(color, message, byteCounter, bitCounter)
                output.setRGB(x, y, processedColor.rgb)
                bitCounter++
                if (bitCounter == 8) {
                    bitCounter = 0
                    byteCounter++
                }
                if (byteCounter == message.size) isProcessingDone = true
            }
        }
    }
    return output
}

private fun processPixel(color: Color, byteArray: ByteArray, byteCounter: Int, bitCounter: Int): Color {
    val r = color.red
    val g = color.green
    var b = color.blue
    val bit = getBitAt(byteArray[byteCounter], bitCounter)
    b = setLastBit(b, bit)
    return Color(r, g, b)
}

private fun writeImage(image: BufferedImage, fileName: String) {
    ImageIO.write(image, "png", File(fileName))
}

fun tryToWriteImage(image: BufferedImage, fileName: String) {
    try {
        writeImage(image, fileName)
        println("Message saved in $fileName image.")
    } catch (e: IOException) {
        println("Can't write output file.")
    }
}

fun tryToReadMessage(image: BufferedImage): ByteArray? {
    val message = readMessage(image)
    return if (message == null) {
        println("Can't read message (end sequence not found).")
        null
    } else {
        message
    }
}

private fun readMessage(image: BufferedImage): ByteArray? {
    var message = byteArrayOf()
    var byteCounter = 0
    var bitCounter = 0
    var currentByte = 0
    for (y in 0 until image.height) {
        for (x in 0 until image.width) {
            val bit = readPixel(Color(image.getRGB(x, y))) shl 7 - bitCounter
            currentByte += bit
            bitCounter++
            if (bitCounter == 8) {
                bitCounter = 0
                byteCounter += 1
                message += currentByte.toByte()
                currentByte = 0
                if (message.size > 3) {
                    if (getLast3Bytes(message).contentEquals(byteArrayOf(0, 0, 3))) {
                        return removeLast3Bytes(message)
                    }
                }
            }
        }
    }
    return null
}

private fun readPixel(color: Color): Int {
    val b = color.blue
    return getBitAt(b.toByte(), 7)
}

private fun getBitAt(byte: Byte, position: Int): Int {
    return byte.toInt() shr 7 - position and 0b1
}

private fun setLastBit(i: Int, bit: Int): Int {
    return if (bit == 0) {
        i and 0b11111110
    } else {
        i or 0b00000001
    }
}

fun getLast3Bytes(array: ByteArray): ByteArray {
    return byteArrayOf(array[array.lastIndex - 2], array[array.lastIndex - 1], array.last())
}

fun removeLast3Bytes(array: ByteArray): ByteArray {
    var emptyArray = byteArrayOf()
    for (i in array.indices) {
        if (i == array.lastIndex - 2) break
        emptyArray += array[i]
    }
    return emptyArray
}