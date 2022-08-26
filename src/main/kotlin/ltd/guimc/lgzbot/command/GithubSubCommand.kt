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
    suspend fun CommandSender.ltd_guimc_command_git_add(url: String) {
        var repo = GithubUtils.findGitLink(url)
        if (repo == null) {
            sendMessage("Failed to get Repo Info")
            return
        } else {
            repo = GithubUtils.convert(url)
        }

        if (GithubSubConfig.subList.keys.indexOf(repo) == -1) {
            GithubSubConfig.subList[repo] = mutableListOf(getGroupOrNull()?: return)
        } else {
            if (GithubSubConfig.subList[repo]!!.indexOf(getGroupOrNull()?: return) != -1) {
                sendMessage("?")
                return
            }
            GithubSubConfig.subList[repo]!!.add(getGroupOrNull()?: return)
        }

        sendMessage("OK")
    }

    @SubCommand("del")
    @Description("Delete a repo from sub")
    suspend fun CommandSender.ltd_guimc_command_git_del(repo: String) {
        if (GithubSubConfig.subList.keys.indexOf(repo) == -1) {
            sendMessage("?")
            return
        }

        if (GithubSubConfig.subList[repo]!!.indexOf(getGroupOrNull()?: return) == -1) {
            sendMessage("?")
            return
        }

        GithubSubConfig.subList[repo]!!.remove(getGroupOrNull()?: return)
    }
}