package ltd.guimc.lgzbot.plugin.schedule

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ltd.guimc.lgzbot.plugin.PluginMain.logger
import ltd.guimc.lgzbot.plugin.files.Data.repoLastCommit
import ltd.guimc.lgzbot.plugin.files.Data.repoListenList
import ltd.guimc.lgzbot.plugin.utils.GithubUtils.getLatestCommit
import ltd.guimc.lgzbot.plugin.utils.GithubUtils.getRepo
import net.mamoe.mirai.message.data.PlainText
import java.util.*

object GithubSchedule {
    @OptIn(DelicateCoroutinesApi::class)
    fun registerSchedule() {
        Timer().schedule(
            object: TimerTask() {
                override fun run() {
                    GlobalScope.async {
                        schedule()
                    }.start()
                }
        }, 30000, 60000)
    }

    private suspend fun schedule() {
        println("Run Github Schedule")
        for (i in repoListenList) {
            val commit = getRepo(i.key).getLatestCommit()
            if (commit.shA1 != repoLastCommit[i.key]) {
                repoLastCommit[i.key] = commit.shA1
                i.value.forEach {
                    it.sendMessage(
                        PlainText("仓库 ${i.key} 的最新 Commit 信息:\n") +
                            PlainText("Commit ID: ${commit.shA1.substring(0, 7)}\n") +
                            PlainText("Commit Author: ${commit.committer.name} (${commit.committer.email})\n") +
                            PlainText("Commit Date: ${commit.commitDate}\n") +
                            PlainText("Commit Message:\n ${commit.commitShortInfo.message ?: "No Message"}\n")
                    )
                }
            }
        }
    }
}