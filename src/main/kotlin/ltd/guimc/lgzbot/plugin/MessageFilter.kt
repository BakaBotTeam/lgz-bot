package ltd.guimc.lgzbot.plugin

import ltd.guimc.lgzbot.plugin.PluginMain.logger
import ltd.guimc.lgzbot.plugin.files.Config
import ltd.guimc.lgzbot.plugin.files.Data
import ltd.guimc.lgzbot.plugin.utils.RegexUtils
import ltd.guimc.lgzbot.plugin.utils.TextUtils.findSimilarity
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.contact.isOwner
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import java.lang.Thread.sleep
import java.time.Instant

object MessageFilter {
    var spammerFucker = mutableMapOf<Long, Int>()
    var spammerFucker2 = mutableMapOf<Long, Long>()
    var repeaterFucker = mutableMapOf<Long, String>()
    var memberVl = mutableMapOf<Long, Double>()
    var historyMessage = mutableMapOf<Long, MutableList<MessageChain>>()

    suspend fun filter(e: GroupMessageEvent) {
        // 检查权限
        if (e.sender.isOwner() || !e.group.botAsMember.isOperator() ||
            e.sender.permission == e.group.botPermission) return

        if (RegexUtils.matchRegex(Data.regex, e.message.content) && e.message.content.length >= 35) {
            e.group.sendMessage(At(e.sender) + PlainText("你好像发送了广告... 检查一下你的消息吧~"))
            e.message.recall()
            e.sender.mute(Config.muteTime)
        }

        if (memberVl[e.sender.id] == null) {
            clearVl(e.sender.id)
        }

        if (historyMessage[e.sender.id] == null) {
            historyMessage[e.sender.id] = mutableListOf()
        } else if (historyMessage[e.sender.id]!!.size >= Config.historyMessageLimit) {
            historyMessage[e.sender.id]!!.removeAt(0)
        }

        historyMessage[e.sender.id]!!.add(e.message)

        // Anti Spammers
        val timestamp = Instant.now()

        if (spammerFucker[e.sender.id] == null) {
            spammerFucker[e.sender.id] = 1
            spammerFucker2[e.sender.id] = timestamp.epochSecond
        } else if (timestamp.epochSecond - spammerFucker2[e.sender.id]!! >= 1) {
            spammerFucker[e.sender.id] = 1
            spammerFucker2[e.sender.id] = timestamp.epochSecond
        } else {
            spammerFucker[e.sender.id] = spammerFucker[e.sender.id]!! + 1
        }

        if (spammerFucker[e.sender.id]!! >= 3) {
            addVl(e.sender.id, 30.0)
        }

        // 重复内容过滤
        if (repeaterFucker[e.sender.id] == null) {
            repeaterFucker[e.sender.id] = e.message.content
        } else {
            val slimer = findSimilarity(repeaterFucker[e.sender.id]!!, e.message.content)
            if (slimer >= 0.75) {
                addVl(e.sender.id, slimer * 25)
            } else {
                addVl(e.sender.id, -20.0)
            }
            repeaterFucker[e.sender.id] = e.message.content
        }

        // VL处罚
        if (memberVl[e.sender.id]!! >= Config.vlPunish) {
            e.group.sendMessage(At(e.sender) + PlainText("你的VL已经超过了${Config.vlPunish}了!! 你的嘴现在被我黏上了~~"))
            e.sender.mute(Config.muteTime)
            e.message.recall()
            historyMessage[e.sender.id]?.forEach {
                it.recall()
                sleep(50)
            }
            historyMessage[e.sender.id]?.clear()
            memberVl[e.sender.id] = .0
        }

        // VL小于0时, 将其置为0
        if (memberVl[e.sender.id]!! < 0) {
            memberVl[e.sender.id] = .0
        }
    }

    fun addVl(id: Long, vl: Double) {
        if (memberVl[id] == null) {
            memberVl[id] = .0
        }
        var tempValue = memberVl[id]!!
        memberVl[id] = memberVl[id]!! + vl
        if (memberVl[id]!! >= 0.0 || tempValue > 0.0) {
            logger.info("$id 的VL增加了 $vl, 现在是 ${memberVl[id]}")
        }
    }

    fun clearVl(id: Long) {
        memberVl[id] = .0
        logger.info("$id 的VL清零了")
    }
}