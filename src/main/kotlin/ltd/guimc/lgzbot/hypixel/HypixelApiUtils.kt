package ltd.guimc.lgzbot.hypixel

import ltd.guimc.lgzbot.files.Config
import net.hypixel.api.util.ILeveling
import org.json.JSONObject
import kotlin.math.floor
import kotlin.math.sqrt

object HypixelApiUtils {
    val BASE = 10000.0
    val GROWTH = 2500.0

    /* Constants to generate the total amount of XP to complete a level */
    val HALF_GROWTH = 0.5 * GROWTH

    /* Constants to look up the level from the total amount of XP */
    val REVERSE_PQ_PREFIX = -(BASE - 0.5 * GROWTH) / GROWTH
    val REVERSE_CONST = REVERSE_PQ_PREFIX * REVERSE_PQ_PREFIX
    val GROWTH_DIVIDES_2 = 2 / GROWTH

    fun request(url: String): JSONObject {
        val connection = java.net.URL("https://api.hypixel.net$url").openConnection() as java.net.HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("API-Key", Config.hypixelApiKey)
        connection.connect()
        // 转换到json对象
        val raw = connection.inputStream.bufferedReader().readText()
        val jsonObject = JSONObject(raw)
        if (!jsonObject.getBoolean("success")) {
            throw ApiFailedException("Request failed with message \"${jsonObject.getString("cause")}\"")
        }
        return jsonObject
    }
}