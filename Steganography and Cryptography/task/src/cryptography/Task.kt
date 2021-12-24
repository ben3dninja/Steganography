package cryptography

enum class Task(val executeTask: () -> Unit) {
    HIDE(::hide),
    SHOW(::show),
    EXIT(::exit);

    companion object {
        val joinedNames = values().joinToString().lowercase()
    }
}