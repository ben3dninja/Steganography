package cryptography

fun encryptMessage(message: String, password: String): ByteArray {
    val messageArray = message.encodeToByteArray()
    val passwordArray = matchSize(password.encodeToByteArray(), messageArray.size)
    return xorArrays(messageArray, passwordArray) + 0 + 0 + 3
}

fun decryptMessage(encryptedMessage: ByteArray, password: String): String {
    val passwordArray = matchSize(password.encodeToByteArray(), encryptedMessage.size)
    return xorArrays(encryptedMessage, passwordArray).decodeToString()
}

private fun xorArrays(a: ByteArray, b: ByteArray): ByteArray {
    var c = byteArrayOf()
    for (i in a.indices) {
        c += (a[i].toInt() xor b[i].toInt()).toByte()
    }
    return c
}

fun matchSize(password: ByteArray, length: Int): ByteArray {
    var matched: ByteArray = password
    while (matched.size < length) {
        matched += password
    }
    matched = matched.slice(0 until length).toByteArray()
    return matched
}