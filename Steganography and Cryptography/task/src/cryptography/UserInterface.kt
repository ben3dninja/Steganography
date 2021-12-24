package cryptography

import java.lang.IllegalArgumentException

fun askForTask(): Task? {
    println("Task (${Task.joinedNames}):")
    val name = readSafe()
    return try {
        Task.valueOf(name.uppercase())
    } catch (_: IllegalArgumentException) {
        println("Wrong task: $name")
        null
    }
}

fun askForOutputImageFileName(): String {
    println("Output image file:")
    return readSafe()
}

fun askForInputImageFileName(): String {
    println("Input image file:")
    return readSafe()
}

fun askForMessage(): String {
    println("Message to hide:")
    return readSafe()
}

fun askForPassword(): String {
    println("Password:")
    return readSafe()
}

fun readSafe(): String {
    var s: String? = null
    while (s == null) {
        s = readLine()
    }
    return s
}

fun showMessage(message: String) {
    println("Message:")
    println(message)
}