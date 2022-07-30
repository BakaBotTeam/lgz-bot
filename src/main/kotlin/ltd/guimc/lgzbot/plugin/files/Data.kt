package ltd.guimc.lgzbot.plugin.files

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.contact.Group

object Data : AutoSavePluginData("github-sub-data") {
    var repoListenList: MutableMap<String, MutableList<Long>> by value(mutableMapOf())

    var repoLastCommit: MutableMap<String, String> by value(mutableMapOf())
}