package ltd.guimc.lgzbot.listener.github

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.files.GithubSubConfig
import ltd.guimc.lgzbot.utils.GithubUtils
import ltd.guimc.lgzbot.utils.RegexUtils
import net.mamoe.mirai.Bot

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
                        val commit = repo.lastCommitInfo
                        for (i2 in GithubSubConfig.subList.get(i)!!) {
                            val group = bot.getGroup(i2) ?: continue
                            val s = "[GitHub] (Preview Version)\n" +
                                "$i has a new commit!\n" +
                                "CommitId: ${commit.commitId}\n" +
                                "CommitMessage: ${commit.commitMessage}"

                            if (RegexUtils.matchRegex(PluginMain.adRegex, s)) continue
                            GlobalScope.async { group.sendMessage(s) }
                        }
                    }
                }
            }
        }.start()

        logger.info("Github Sub is Running!")
    }
}