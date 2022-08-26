package ltd.guimc.lgzbot.files

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.contact.Group

object GithubSubConfig : AutoSavePluginConfig("githubsub") {
    var subList by value(mutableMapOf<String, MutableList<Group>>())
}