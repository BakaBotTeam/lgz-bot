/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.listener.message

import ltd.guimc.lgzbot.PluginMain.adRegex
import ltd.guimc.lgzbot.files.ModuleStateConfig
import ltd.guimc.lgzbot.utils.GithubUtils
import ltd.guimc.lgzbot.utils.MessageUtils.getPlainText
import ltd.guimc.lgzbot.utils.RegexUtils
import net.mamoe.mirai.event.events.GroupMessageEvent
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object GithubUrlListener {
    suspend fun onMessage(event: GroupMessageEvent) {
        if (ModuleStateConfig.githubquery) {
            val plain = event.message.getPlainText()
            val gitLink = GithubUtils.findGitLink(plain) ?: return
            // if (plain.indexOf("/gh-sub") != -1) return

            val info = GithubUtils.getGithubRepo(GithubUtils.convert(gitLink))
            val s = """[GitHub] (Preview Version)
                |Repo: ${info.repo}
                |Descriptor: ${RegexUtils.checkRisk(info.descriptor)}
                |Owner: ${info.author.name}
                |Default Branch: ${info.defaultBranch}
                |Language: ${info.language}
                |Last Commit: ${RegexUtils.checkRisk(info.lastCommitInfo.commitMessage)} (${info.lastCommitInfo.commitId})
                |Create Time: ${info.createdTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))}
                |Update Time: ${info.updateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))}""".trimMargin()

            event.group.sendMessage(s)
        }
    }
}
