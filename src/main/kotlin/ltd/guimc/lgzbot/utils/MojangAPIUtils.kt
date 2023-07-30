package ltd.guimc.lgzbot.utils

import ltd.guimc.lgzbot.utils.TextUtils.convert2UUID
import java.util.*

object MojangAPIUtils {
    fun getUUIDByName(name: String): String {
        return HttpUtils.getJsonObject("https://api.mojang.com/users/profiles/minecraft/$name").getString("id")
    }
}