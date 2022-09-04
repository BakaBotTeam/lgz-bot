package ltd.guimc.lgzbot.listener.github

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.files.GithubSubConfig
import ltd.guimc.lgzbot.utils.GithubUtils
import ltd.guimc.lgzbot.utils.RegexUtils
import net.mamoe.mirai.Bot
import java.lang.Thread.sleep
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(DelicateCoroutinesApi::class)
class CommitListener {
    var isQuitting = false
    val logger = PluginMain.logger

    init {
        for (i in GithubSubConfig.subList.keys) {
            val repo = GithubUtils.getGithubRepo(i)
            repo.checkUpdate()
        }

        Thread {
            while (!isQuitting) {
                if (Bot.instances.size > 1) {
                    logger.error("This module doesn't support multi account.")
                    throw Exception("This module doesn't support multi account.")
                }
                if (Bot.instances.isEmpty()) {
                    continue
                }

                val bot = Bot.instances[0]

                for (i in GithubSubConfig.subList.keys) {
                    val repo = GithubUtils.getGithubRepo(i)
                    if (repo.checkUpdate()) {
                        val commit = repo.commit
                        for (i2 in GithubSubConfig.subList.get(i)!!) {
                            val group = bot.getGroup(i2) ?: continue
                            val s = "[GitHub] (Preview Version)\n" +
                                "$i has a new commit!\n" +
                                "CommitId: ${commit.commitId}\n" +
                                "CommitMessage: ${commit.commitMessage}\n" +
                                "Author: ${commit.author.name} (${commit.author.email})" +
                                "Time: ${commit.commitTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))}"

                            if (RegexUtils.matchRegex(PluginMain.adRegex, s)) continue
                            GlobalScope.async { group.sendMessage(s) }
                        }
                    }
                }

                sleep(1000*60*60)
            }
        }.start()

        logger.info("Github Sub is Running!")
    }
}