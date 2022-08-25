package ltd.guimc.lgzbot.files

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {
    var muteTime by value(600)

    var vlPunish by value(100.0)

    var historyMessageLimit by value(8)
}