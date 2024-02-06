package ltd.guimc.lgzbot.files

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object GithubWebhookSubData : AutoSavePluginData("githubWebhookSub") {
    val sub by value(mutableMapOf<Long, MutableMap<String, MutableList<Long>>>())

    val ignore by value(mutableMapOf<Long, MutableMap<String, MutableList<String>>>())
}