package ltd.guimc.lgzbot.plugin.schedule

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ltd.guimc.lgzbot.plugin.utils.GithubUtils.getLatestCommit
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.PlainText
import org.kohsuke.github.GHRepository
import java.util.Timer
import java.util.TimerTask

class GithubSchedule {
    var repoListenList: MutableMap<GHRepository, MutableList<Group>> = mutableMapOf()
    var repoLastCommit: MutableMap<GHRepository, String> = mutableMapOf()

    @OptIn(DelicateCoroutinesApi::class)
    fun registerSchedule() {
        Timer().schedule(
            object: TimerTask() {
                override fun run() {
                    GlobalScope.async {
                        schedule()
                    }
                }
        }, 0, 60000)
    }

    private suspend fun schedule() {
        for (i in repoListenList) {
            val commit = i.key.getLatestCommit()
            if (commit.shA1 != repoLastCommit[i.key]) {
                repoLastCommit[i.key] = commit.shA1
                i.value.forEach {
                    it.sendMessage(
                        PlainText("仓库 ${i.key.fullName} 的最新 Commit 信息:\n") +
                            PlainText("Commit ID: ${commit.shA1.substring(0, 7)}\n") +
                            PlainText("Commit Author: ${commit.committer.name} (${commit.committer.email})\n") +
                            PlainText("Commit Date: ${commit.commitDate}\n") +
                            PlainText("Commit Message:\n ${commit.commitShortInfo.message ?: "No Message"}\n")
                    )
                }
            }
        }
        return
    }
}