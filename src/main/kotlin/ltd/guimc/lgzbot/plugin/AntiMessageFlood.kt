package ltd.guimc.lgzbot.plugin

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.contact.isOwner
import net.mamoe.mirai.event.events.GroupMessageEvent
import java.util.*

object AntiMessageFlood {
    val floodGroupList = mutableListOf<Group>()
    val groupMessageLogger = mutableMapOf<Group, Int>()
    val groupMessageLoggerTime = mutableMapOf<Group, Long>()
    val timer = Timer()

    suspend fun floodChecker(e: GroupMessageEvent) {
        // 检查权限
        if (!e.sender.isOwner() && e.group.botAsMember.isOperator() &&
            e.sender.permission != e.group.botPermission) return

        if (groupMessageLogger[e.group] == null || System.currentTimeMillis() - groupMessageLoggerTime[e.group]!! > 1000) {
            groupMessageLogger[e.group] = 0
            groupMessageLoggerTime[e.group] = System.currentTimeMillis()
        }

        groupMessageLogger[e.group] = groupMessageLogger[e.group]!! + 1
        if (groupMessageLogger[e.group]!! > 10) {
            e.group.sendMessage("本群消息数量异常, 将会禁用机器人一段时间")
            groupMessageLogger[e.group] = 0
            floodGroupList.add(e.group)
            timer.schedule(object : TimerTask() {
                override fun run() {
                    floodGroupList.remove(e.group)
                    groupMessageLogger.remove(e.group)
                    groupMessageLoggerTime.remove(e.group)
                }
            }, 1000 * 60)
        }
    }
}