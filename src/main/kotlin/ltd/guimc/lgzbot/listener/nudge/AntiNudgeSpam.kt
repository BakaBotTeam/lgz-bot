package ltd.guimc.lgzbot.listener.nudge

import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.events.NudgeEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import java.time.Instant

object AntiNudgeSpam {
    private val nudgeTimes: MutableMap<User, Int> = mutableMapOf()
    private val lastNudgeTime: MutableMap<User, Long> = mutableMapOf()
    private val blockedUser: MutableMap<User, Long> = mutableMapOf()

    suspend fun onNudge(e: NudgeEvent) {
        val from = e.from as User
        val timestamp = Instant.now().epochSecond

        if (e.target != e.bot) return    // 只处理对机器人的戳一戳
        if (e.from == e.bot) return      // 机器人自己戳自己? 雾

        // 检查是否已经被屏蔽了
        if (blockedUser[from] != null && blockedUser[from]!! >= timestamp) {
            e.intercept()
            return
        }

        if (nudgeTimes[from] == null) {  // 检查是否已初始化过戳一戳发起者
            nudgeTimes[from] = 1
            lastNudgeTime[from] = timestamp
        } else if (timestamp - lastNudgeTime[from]!! >= 3) {
            // ta已经好久没有戳一戳了 重置次数
            nudgeTimes[from] = 1
            lastNudgeTime[from] = timestamp
        } else {
            // 次数+1
            nudgeTimes[from] = nudgeTimes[from]!! + 1
        }

        // 检查次数
        if (nudgeTimes[from]!! >= 3) {
            blockedUser[from] = timestamp + 3600L
            try {
                e.subject.sendMessage(
                    At(from) + PlainText(" 你...你怎么能戳这么快！\n我生气了！免疫你的戳一戳1小时！")
                )
            } catch (ignore: Throwable) {
            }
        }
    }
}