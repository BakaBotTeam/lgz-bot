/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.listener.message

import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.PluginMain.adPinyinRegex
import ltd.guimc.lgzbot.PluginMain.adRegex
import ltd.guimc.lgzbot.PluginMain.bypassMute
import ltd.guimc.lgzbot.PluginMain.disableADCheck
import ltd.guimc.lgzbot.PluginMain.disableSpamCheck
import ltd.guimc.lgzbot.PluginMain.logger
import ltd.guimc.lgzbot.files.Config
import ltd.guimc.lgzbot.utils.MemberUtils.mute
import ltd.guimc.lgzbot.utils.MessageUtils.getFullText
import ltd.guimc.lgzbot.utils.MessageUtils.getPlainText
import ltd.guimc.lgzbot.utils.RegexUtils
import ltd.guimc.lgzbot.utils.TextUtils.findSimilarity
import ltd.guimc.lgzbot.utils.TextUtils.removeNonVisible
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.ForwardMessage
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import net.mamoe.mirai.message.data.content
import java.lang.Thread.sleep
import java.time.Instant

object MessageFilter {
    private var spammerFucker = mutableMapOf<Long, Int>()
    private var spammerFucker2 = mutableMapOf<Long, Long>()
    private var repeaterFucker = mutableMapOf<Long, String>()
    private var historyMessage = mutableMapOf<Long, MutableList<MessageChain>>()
    private var memberVl = mutableMapOf<Long, Double>()

    private var messagesHandled = 0
    var riskList = ArrayList<Member>()
    suspend fun filter(e: GroupMessageEvent) {
        var muted = false
        // 检查权限

        val textMessage = e.message.getPlainText()
            .removeNonVisible()
        val stringLength = if (e.sender in riskList) {
            10
        } else {
            35
        }

        if (textMessage.isEmpty() && e.message.content.isEmpty()) return

        if (e.sender.permission.level >= e.group.botPermission.level) return

        if (!e.group.permitteeId.hasPermission(disableADCheck)) {
            if (RegexUtils.matchRegex(adRegex, textMessage) && textMessage.length >= stringLength) {
                try {
                    e.message.recall()
                    e.group.mute(e.sender, "非法发言内容")
                    muted = true
                } catch (_: Exception) {}
                riskList.add(e.sender)
                setVl(e.sender.id, 99.0)
                messagesHandled++
            }

            // 合并转发消息提取
            var forwardMessage: ForwardMessage? = null
            e.message.iterator().forEach {
                if (it is ForwardMessage) {
                    forwardMessage = it as ForwardMessage
                }
            }

            // 合并转发消息检测
            if (!muted && forwardMessage != null) {
                forwardMessage!!.nodeList.forEach {
                    val forwardMessageItemString = it.messageChain.getPlainText()
                    if (!muted && RegexUtils.matchRegex(adRegex, forwardMessageItemString) && forwardMessageItemString.length >= stringLength) {
                        try {
                            e.message.recall()
                            e.group.mute(e.sender, "非法发言内容 (在合并转发消息内)")
                            muted = true
                        } catch (_: Exception) {}
                        riskList.add(e.sender)
                        setVl(e.sender.id, 99.0)
                        messagesHandled++
                    }
                }
            }

            // 拼音检查发言
            if (!muted && riskList.indexOf(e.sender) != -1 && RegexUtils.matchRegexPinyin(adPinyinRegex, textMessage)) {
                try {
                    e.message.recall()
                    e.group.mute(e.sender, "非法发言内容")
                    muted = true
                } catch (_: Exception) {
                }
                setVl(e.sender.id, 99.0)
                messagesHandled++
            }
        }

        if (!e.group.permitteeId.hasPermission(disableSpamCheck)) {
            if (memberVl[e.sender.id] == null) {
                clearVl(e.sender.id)
                riskList.remove(e.sender)
            }

            if (memberVl[e.sender.id]!! <= 25 && e.sender in riskList) {
                riskList.remove(e.sender)
                //e.group.sendMessage(PlainText("你已经被移出了风险管控，请不要再发送广告了~")
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
                repeaterFucker[e.sender.id] = textMessage
            } else {
                val slimer = findSimilarity(repeaterFucker[e.sender.id]!!, textMessage)
                if (slimer >= 0.75) {
                    addVl(e.sender.id, slimer * 25)
                } else {
                    addVl(e.sender.id, -20.0)
                }
                repeaterFucker[e.sender.id] = e.message.content
            }

            // VL处罚
            if (memberVl[e.sender.id]!! >= Config.vlPunish && !muted) {
                if (e.sender.id == 1242788764L) return
                e.group.mute(e.sender, "不允许的速度/发言内容重复")
                muted = true
                e.message.recall()
                try {
                    historyMessage[e.sender.id]?.forEach {
                        it.recall()
                        sleep(100)
                    }
                } catch (_: Exception) {
                }
                historyMessage[e.sender.id]?.clear()
                memberVl[e.sender.id] = .0
                messagesHandled++
            }

            // VL小于0时, 将其置为0
            if (memberVl[e.sender.id]!! < 0) {
                memberVl[e.sender.id] = .0
            }
        }

        // Permission block
        if ((e.sender.permitteeId.hasPermission(PluginMain.blocked) && !e.sender.permitteeId.hasPermission(bypassMute)) ||
            (e.group.permitteeId.hasPermission(PluginMain.blocked) && !e.sender.permitteeId.hasPermission(bypassMute))
        ) {
            muted = true
        }

        // Cancel Event
        if (muted) e.intercept()
    }

    fun addVl(id: Long, vl: Double) {
        if (memberVl[id] == null) {
            memberVl[id] = .0
        }
        val tempValue = memberVl[id]!!
        memberVl[id] = memberVl[id]!! + vl
        if (memberVl[id]!! >= 0.0 || tempValue > 0.0) {
            logger.info("$id 的VL增加了 $vl, 现在是 ${memberVl[id]}")
        }
    }

    fun clearVl(id: Long) {
        memberVl[id] = .0
        logger.info("$id 的VL清零了")
    }

    fun setVl(id: Long, vl: Double) {
        memberVl[id] = vl
        logger.info("$id 的VL设置为 $vl")
    }

    private suspend fun Group.mute(mem: Member, reason: String) {
        mem.mute(
            if (mem.permitteeId.hasPermission(bypassMute)) 1
            else if (riskList.indexOf(mem) != -1) 1200
            else 600,
            "Message Filter: $reason"
        )
    }
}
