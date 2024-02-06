package ltd.guimc.lgzbot.utils.webhook

import ltd.guimc.lgzbot.PluginMain.logger
import java.nio.charset.StandardCharsets
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object SecurityUtils {
    fun calcSignature(data: String, sharedSecret: String): ByteArray? {
        try {
            val hmac: Mac = Mac.getInstance("HmacSHA256")
            hmac.init(SecretKeySpec(sharedSecret.toByteArray(StandardCharsets.UTF_8), "HmacSHA256"))
            return hmac.doFinal(data.toByteArray(StandardCharsets.UTF_8))

        } catch (e: Exception) {
            logger.error("verifySignature had the following error: ", e)
        }
        return null
    }
}