package ltd.guimc.lgzbot.plugin.command.github

import ltd.guimc.lgzbot.plugin.PluginMain
import ltd.guimc.lgzbot.plugin.utils.GithubUtils
import ltd.guimc.lgzbot.plugin.utils.GithubUtils.getLatestCommit
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.message.data.PlainText

object RepoCommand: CompositeCommand(
    owner = PluginMain,
    primaryName = "gh-repo",
    description = "Github Repo"
) {
    @SubCommand("test")
    @Description("测试仓库地址")
    suspend fun CommandSender.gh_test_repo(repo: String) {
        try {
            val commit = GithubUtils.getRepo(repo).getLatestCommit()
            sendMessage(
                PlainText("仓库 $repo 的最新 Commit 信息:\n") +
                    PlainText("Commit ID: ${commit.shA1.substring(0, 7)}\n") +
                    PlainText("Commit Author: ${commit.committer.name} (${commit.committer.email})\n") +
                    PlainText("Commit Date: ${commit.commitDate}\n") +
                    PlainText("Commit Message:\n ${commit.commitShortInfo.message ?: "No Message"}\n")
            )
        } catch (e: Exception) {
            sendMessage(PlainText("获取仓库信息失败: ${e.message}"))
        }
    }
}