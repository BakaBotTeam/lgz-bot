package ltd.guimc.lgzbot.plugin

import ltd.guimc.lgzbot.plugin.PluginMain.logger
import ltd.guimc.lgzbot.plugin.files.Config
import ltd.guimc.lgzbot.plugin.files.Data
import ltd.guimc.lgzbot.plugin.utils.RegexUtils
import ltd.guimc.lgzbot.plugin.utils.TextUtils.findSimilarity
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.contact.isOwner
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.content
import java.time.Instant

object MessageFilter {
    var spammerFucker = mutableMapOf<Long, Int>()
    var spammerFucker2 = mutableMapOf<Long, Long>()
    var repeaterFucker = mutableMapOf<Long, String>()
    var memberVl = mutableMapOf<Long, Double>()

    suspend fun filter(e: GroupMessageEvent) {
        if (e.message.content.length <= 35) return
        if (e.sender.isOwner() || !e.group.botAsMember.isOperator() ||
            e.sender.permission == e.group.botPermission) return

        if (RegexUtils.matchRegex(Data.regex, e.message.content)) {
            e.group.sendMessage(At(e.sender) + PlainText("你好像发送了广告... 检查一下你的消息吧~"))
            e.message.recall()
            try {
                e.sender.mute(Config.muteTime)
            } catch (f: Exception) {
                logger.warning("无法禁言 ${e.sender.id}, 可能是权限不足")
            }
        }

        if (memberVl[e.sender.id] == null) {
            memberVl[e.sender.id] = .0
        }

        // Anti Spammers
        val timestamp = Instant.now()

        if (spammerFucker[e.sender.id] == null) {
            spammerFucker[e.sender.id] = 1
            spammerFucker2[e.sender.id] = timestamp.epochSecond
        } else if (spammerFucker2[e.sender.id]!! - timestamp.epochSecond >= 1) {
            spammerFucker[e.sender.id] = 1
            spammerFucker2[e.sender.id] = timestamp.epochSecond
        } else {
            spammerFucker[e.sender.id] = spammerFucker[e.sender.id]!! + 1
        }

        if (spammerFucker[e.sender.id]!! >= Config.spammerTimes) {
            memberVl[e.sender.id] = memberVl[e.sender.id]!! + 30
        }

        // 重复内容过滤
        if (repeaterFucker[e.sender.id] == null) {
            repeaterFucker[e.sender.id] = e.message.content
        } else {
            val slimer = findSimilarity(repeaterFucker[e.sender.id]!!, e.message.content)
            if (slimer >= Config.repeaterSimilarity) {
                memberVl[e.sender.id] = memberVl[e.sender.id]!! + slimer * 25
            } else {
                memberVl[e.sender.id] = memberVl[e.sender.id]!! - 20
            }
            repeaterFucker[e.sender.id] = e.message.content
        }

        // VL处罚
        if (memberVl[e.sender.id]!! >= Config.vlPunish) {
            e.group.sendMessage(At(e.sender) + PlainText("你的VL已经超过了${Config.vlPunish}了!! 你的嘴现在被我黏上了~~"))
            e.sender.mute(Config.muteTime)
            memberVl[e.sender.id] = .0
        }

        // VL小于0时, 将其置为0
        if (memberVl[e.sender.id]!! < 0) {
            memberVl[e.sender.id] = .0
        }
    }
}