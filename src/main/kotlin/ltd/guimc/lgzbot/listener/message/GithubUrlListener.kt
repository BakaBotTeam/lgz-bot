package ltd.guimc.lgzbot.listener.message

import ltd.guimc.lgzbot.PluginMain.adRegex
import ltd.guimc.lgzbot.PluginMain.logger
import ltd.guimc.lgzbot.utils.GithubUtils
import ltd.guimc.lgzbot.utils.MessageUtils.getPlainText
import ltd.guimc.lgzbot.utils.RegexUtils
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object GithubUrlListener {
    suspend fun onMessage(event: GroupMessageEvent) {
        if (event.message.any { i -> i !is PlainText }) return
        val gitLink = GithubUtils.findGitLink(event.message.getPlainText())

        if (gitLink != null) {
            logger.info("Find Git Link! $gitLink")
        } else {
            return
        }

        val info = GithubUtils.getGithubRepo(GithubUtils.convert(gitLink))
        val s = """
            [GitHub]
            Repo: ${info.repo}
            Descriptor: ${info.descriptor}
            Owner: ${info.author.name}
            Default Branch: ${info.defaultBranch}
            Language: ${info.language}
            Last Commit: ${info.lastCommitInfo.commitMessage} (${info.lastCommitInfo.commitId})
            Create Time: ${info.createdTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))}
            Update Time: ${info.updateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))}
        """.trimIndent()

        event.group.sendMessage(s)
    }
}