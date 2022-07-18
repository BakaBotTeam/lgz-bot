package ltd.guimc.lgzbot.plugin.files

import net.mamoe.mirai.console.data.AutoSavePluginConfig

object Config : AutoSavePluginConfig("config") {
    var blackList = listOf<Long>()

    var muteTime = 600

    var spammerTimes = 3

    var repeaterSimilarity = 0.75

    var vlPunish = 100.0
}