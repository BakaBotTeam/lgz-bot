package ltd.guimc.lgzbot.plugin

import ltd.guimc.lgzbot.plugin.PluginMain.adRegex
import ltd.guimc.lgzbot.plugin.PluginMain.isSuperUser
import ltd.guimc.lgzbot.plugin.PluginMain.logger
import ltd.guimc.lgzbot.plugin.files.Config
import ltd.guimc.lgzbot.plugin.utils.MessageUtils.getForwardMessage
import ltd.guimc.lgzbot.plugin.utils.MessageUtils.getPlainText
import ltd.guimc.lgzbot.plugin.utils.RegexUtils
import ltd.guimc.lgzbot.plugin.utils.TextUtils.findSimilarity
import ltd.guimc.lgzbot.plugin.utils.TextUtils.removeNonVisible
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.contact.isOwner
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import java.lang.Thread.sleep
import java.time.Instant

object MessageFilter {
    private var spammerFucker = mutableMapOf<Long, Int>()
    private var spammerFucker2 = mutableMapOf<Long, Long>()
    private var repeaterFucker = mutableMapOf<Long, String>()
    private var memberVl = mutableMapOf<Long, Double>()
    private var historyMessage = mutableMapOf<Long, MutableList<MessageChain>>()

    private var messagesHandled = 0
    var riskList = ArrayList<Member>()

    suspend fun filter(e: GroupMessageEvent) {
        // 检查权限
        if (e.sender.isOwner() || !e.group.botAsMember.isOperator() ||
            e.sender.permission == e.group.botPermission) return

        val textMessage = e.message.getPlainText()
                                .removeNonVisible()
        val stringLength = if (e.sender in riskList) 10 else 35
        if (RegexUtils.matchRegex(adRegex, textMessage) && textMessage.length >= stringLength) {
            try {
                e.group.sendMessage(At(e.sender) + PlainText("你好像发送了广告... 检查一下你的消息吧~\n您已被添加到风险管控名单 并且Vl设置为99"))
                e.message.recall()
                if (!e.sender.permitteeId.hasPermission(isSuperUser)) {
                    e.sender.mute(Config.muteTime)
                }
            }
            catch (_: Exception) {}
            riskList.add(e.sender)
            setVl(e.sender.id, 99.0)
            e.cancel()
            messagesHandled++
        }

        if (memberVl[e.sender.id] == null) {
            clearVl(e.sender.id)
            riskList.remove(e.sender)
        }

        if (memberVl[e.sender.id]!! <= 25 && e.sender in riskList) {
            riskList.remove(e.sender)
            e.group.sendMessage(
                At(e.sender)+
                    PlainText("你已经被移出了风险管控，请不要再发送广告了~")
            )
        }

        if (historyMessage[e.sender.id] == null) {
            historyMessage[e.sender.id] = mutableListOf()
        } else if (historyMessage[e.sender.id]!!.size >= Config.historyMessageLimit) {
            historyMessage[e.sender.id]?.removeAt(0)
        }

        historyMessage[e.sender.id]?.add(e.message)

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

        // 过滤合并转发消息广告
        val forwardText = e.message.getForwardMessage()
                                .getPlainText()
                                .removeNonVisible()
        if (RegexUtils.matchRegex(adRegex, forwardText)) {
            try {
                e.group.sendMessage(At(e.sender) + PlainText("你好像发送了广告... 检查一下你的消息吧~\n" +
                    "您已被添加到风险管控名单 并且Vl设置为99"))
                e.message.recall()
            }
            catch (_: Exception) {}
            riskList.add(e.sender)
            setVl(e.sender.id, 99.0)
            e.cancel()
            messagesHandled++
        }

        // VL处罚
        if (memberVl[e.sender.id]!! >= Config.vlPunish) {
            e.group.sendMessage(At(e.sender) + PlainText("你的VL已经超过了${Config.vlPunish}了!! 你的嘴现在被我黏上了~~"))
            e.sender.mute(Config.muteTime)
            e.message.recall()
            riskList.add(e.sender)
            try {
                historyMessage[e.sender.id]?.forEach {
                    it.recall()
                    sleep(50)
                }
            } catch (_: Exception) {}
            historyMessage[e.sender.id]?.clear()
            memberVl[e.sender.id] = .0
            messagesHandled++
        }

        // VL小于0时, 将其置为0
        if (memberVl[e.sender.id]!! < 0) {
            memberVl[e.sender.id] = .0
        }
    }

    private fun addVl(id: Long, vl: Double) {
        if (memberVl[id] == null) {
            memberVl[id] = .0
        }
        val tempValue = memberVl[id]!!
        memberVl[id] = memberVl[id]!! + vl
        if (memberVl[id]!! >= 0.0 || tempValue > 0.0) {
            logger.info("$id 的VL增加了 $vl, 现在是 ${memberVl[id]}")
        }
    }

    private fun clearVl(id: Long) {
        memberVl[id] = .0
        logger.info("$id 的VL清零了")
    }

    private fun setVl(id: Long, vl: Double) {
        memberVl[id] = vl
        logger.info("$id 的VL设置为 $vl")
    }
}