package ltd.guimc.lgzbot.plugin.files

import net.mamoe.mirai.console.data.AutoSavePluginConfig

object Config : AutoSavePluginConfig("config") {
    var blackList = listOf<Long>()

    var muteTime = 600

    var vlPunish = 100.0

    var historyMessageLimit = 8

    var hypixelApikey: String? = null
}