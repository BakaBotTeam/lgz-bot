package ltd.guimc.lgzbot.utils

object RandomUtils {
    fun randomText(length: Int): String {
        val charset = "QWERTYUIOPASDDFGHJKKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567980"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }
}