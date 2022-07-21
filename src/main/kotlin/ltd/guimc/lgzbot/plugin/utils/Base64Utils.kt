package ltd.guimc.lgzbot.plugin.utils

import java.util.*

object Base64Utils {
    fun encode(data: ByteArray): String {
        return Base64.getEncoder().encodeToString(data)
    }

    fun decode(data: String): ByteArray {
        return Base64.getDecoder().decode(data)
    }
}