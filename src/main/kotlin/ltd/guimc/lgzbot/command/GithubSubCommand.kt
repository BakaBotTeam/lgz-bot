package ltd.guimc.lgzbot.command

import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.files.GithubSubConfig
import ltd.guimc.lgzbot.utils.GithubUtils
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.getGroupOrNull

object GithubSubCommand: CompositeCommand(
    owner = PluginMain,
    primaryName = "gh-sub",
    description = "Github Sub"
) {
    @SubCommand("add")
    @Description("Add a new sub")
    suspend fun CommandSender.iI1I1I1i1I1I1iI1i1I1Ii1I1i1I1I(url: String) {
        var repo = GithubUtils.findGitLink(url)
        val groupId = (getGroupOrNull()?: return).id
        if (repo == null) {
            sendMessage("Failed to get Repo Info")
            return
        } else {
            repo = GithubUtils.convert(url)
        }

        if (GithubSubConfig.subList.keys.indexOf(repo) == -1) {
            GithubSubConfig.subList[repo] = mutableListOf(groupId)
        } else {
            if (GithubSubConfig.subList[repo]!!.indexOf(groupId) != -1) {
                sendMessage("?")
                return
            }
            GithubSubConfig.subList[repo]!!.add(groupId)
        }

        sendMessage("OK")
    }

    @SubCommand("del")
    @Description("Delete a repo from sub")
    suspend fun CommandSender.iI1I1I1I1i1I1Ii1I1i1I1I1I1Ii1I1(repo: String) {
        val groupId = (getGroupOrNull()?: return).id
        if (GithubSubConfig.subList.keys.indexOf(repo) == -1) {
            sendMessage("?")
            return
        }

        if (GithubSubConfig.subList[repo]!!.indexOf(groupId) == -1) {
            sendMessage("?")
            return
        }

        GithubSubConfig.subList[repo]!!.remove(groupId)
    }

    @SubCommand("key")
    @Description("Set a Github Key")
    suspend fun CommandSender.III11Ii1I1i1IIL1IL1il1IL1(key: String) {
        GithubSubConfig.key = key
        sendMessage("OK")
    }
}