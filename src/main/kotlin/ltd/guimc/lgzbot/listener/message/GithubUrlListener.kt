package ltd.guimc.lgzbot.listener.message

import ltd.guimc.lgzbot.PluginMain.adRegex
import ltd.guimc.lgzbot.utils.GithubUtils
import ltd.guimc.lgzbot.utils.MessageUtils.getPlainText
import ltd.guimc.lgzbot.utils.RegexUtils
import net.mamoe.mirai.event.events.GroupMessageEvent
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object GithubUrlListener {
    suspend fun onMessage(event: GroupMessageEvent) {
        val plain = event.message.getPlainText()
        val gitLink = GithubUtils.findGitLink(plain) ?: return
        if (plain.indexOf("/gh-sub") != -1) return

        val info = GithubUtils.getGithubRepo(GithubUtils.convert(gitLink))
        val s = """
            [GitHub] (Preview Version)
            Repo: ${info.repo}
            Descriptor: ${info.descriptor}
            Owner: ${info.author.name}
            Default Branch: ${info.defaultBranch}
            Language: ${info.language}
            Last Commit: ${info.lastCommitInfo.commitMessage} (${info.lastCommitInfo.commitId})
            Create Time: ${info.createdTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))}
            Update Time: ${info.updateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))}
        """.trimIndent()

        if (RegexUtils.matchRegex(adRegex, s)) return
        event.group.sendMessage(s)
    }
}