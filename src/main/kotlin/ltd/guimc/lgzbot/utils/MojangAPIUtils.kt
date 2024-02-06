package ltd.guimc.lgzbot.utils

import ltd.guimc.lgzbot.utils.TextUtils.convert2UUID
import java.util.*

object MojangAPIUtils {
    fun getUUIDByName(name: String): String {
        return HttpUtils.getJsonObject("https://api.mojang.com/users/profiles/minecraft/$name").getString("id")
    }

    fun String.unFormatted(): String {
        return this.replace("§0", "")
            .replace("§1", "")
            .replace("§2", "")
            .replace("§3", "")
            .replace("§4", "")
            .replace("§5", "")
            .replace("§6", "")
            .replace("§7", "")
            .replace("§8", "")
            .replace("§9", "")
            .replace("§a", "")
            .replace("§b", "")
            .replace("§c", "")
            .replace("§d", "")
            .replace("§e", "")
            .replace("§f", "")
            .replace("§g", "")
            .replace("§h", "")
            .replace("§i", "")
            .replace("§j", "")
            .replace("§n", "")
            .replace("§m", "")
            .replace("§q", "")
            .replace("§p", "")
            .replace("§s", "")
            .replace("§t", "")
            .replace("§u", "")
            .replace("§o", "")
            .replace("§r", "")
            .replace("§k", "")
    }
}