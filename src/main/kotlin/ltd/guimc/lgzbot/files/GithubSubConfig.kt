package ltd.guimc.lgzbot.files

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object GithubSubConfig : AutoSavePluginConfig("githubsub") {
    var key by value("")
    var subList by value(mutableMapOf<String, MutableList<Long>>())
}