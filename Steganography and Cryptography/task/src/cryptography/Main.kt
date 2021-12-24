package cryptography

fun main() {
    var task: Task?
    while (true) {
        task = askForTask() ?: continue
        task.executeTask()
        if (task == Task.EXIT) break
    }
}

fun hide() {
    val inputFileName = askForInputImageFileName()
    val image = tryToReadImage(inputFileName) ?: return
    val outputFileName = askForOutputImageFileName()
    val message = askForMessage()
    val password = askForPassword()
    val encryptedMessage = encryptMessage(message, password)
    val outputImage = tryToHideMessage(encryptedMessage, image) ?: return
    tryToWriteImage(outputImage, outputFileName)
}

fun show() {
    val fileName = askForInputImageFileName()
    val password = askForPassword()
    val image = tryToReadImage(fileName) ?: return
    val encryptedMessage = tryToReadMessage(image)?: return
    val message = decryptMessage(encryptedMessage, password)
    showMessage(message)
}

fun exit() = println("Bye!")