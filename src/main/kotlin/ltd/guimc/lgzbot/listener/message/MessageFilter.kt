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
import ltd.guimc.lgzbot.counter.VLManager
import ltd.guimc.lgzbot.files.Config
import ltd.guimc.lgzbot.files.ModuleStateConfig
import ltd.guimc.lgzbot.utils.LL4JUtils
import ltd.guimc.lgzbot.utils.MemberUtils.mute
import ltd.guimc.lgzbot.utils.MessageUtils.getPlainText
import ltd.guimc.lgzbot.utils.RegexUtils
import ltd.guimc.lgzbot.utils.TextUtils.findSimilarity
import ltd.guimc.lgzbot.utils.TextUtils.removeNonVisible
import ltd.guimc.lgzbot.word.WordUtils
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.ForwardMessage
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import net.mamoe.mirai.message.data.MessageSource.Key.recallIn
import net.mamoe.mirai.message.data.content
import java.lang.Thread.sleep
import java.time.Instant
import kotlin.math.abs

object MessageFilter {
    var allCheckedMessage = 0
    var recalledMessage = 0
    private var spammerFucker = mutableMapOf<Long, Int>()
    private var spammerFucker2 = mutableMapOf<Long, Long>()
    private var repeaterFucker = mutableMapOf<Long, String>()
    private var historyMessage = mutableMapOf<Long, MutableList<MessageChain>>()
    private var memberVl = mutableMapOf<Long, Double>()

    private var messagesHandled = 0
    var riskList = ArrayList<Member>()
    suspend fun filter(e: GroupMessageEvent) {
        if (!ModuleStateConfig.messageFilter) return
        var muted = false
        // 检查权限

        val textMessage = e.message.getPlainText().removeNonVisible()
        val stringLength = if (e.sender in riskList) {
            10
        } else {
            35
        }

        if (textMessage.isEmpty() && e.message.content.isEmpty()) return

        if (!(e.sender.permission.level < e.group.botPermission.level || ModuleStateConfig.slientmute)) return
        if (textMessage.startsWith("/lgzbot") && e.sender.permitteeId.hasPermission(bypassMute)) return

        allCheckedMessage++
        if (!e.group.permitteeId.hasPermission(disableADCheck)) {
            val predictedResult = LL4JUtils.predictAllResult(textMessage)
            val predicted = predictedResult[1] > predictedResult[0]
            if (RegexUtils.matchRegex(adRegex, textMessage) && textMessage.length >= stringLength) {
                try {
                    recalledMessage++
                    e.message.recall()
                    if (predicted) {
                        e.group.mute(e.sender, "非法发言内容 (模型证实)")
                        riskList.add(e.sender)
                        setVl(e.sender.id, 99.0)
                    } else {
                        e.sender.mute(60, "非法发言内容")
                    }
                    muted = true
                } catch (_: Exception) {
                }
                messagesHandled++
            }

            // 合并转发消息提取
            var forwardMessage: ForwardMessage? = null
            e.message.iterator().forEach {
                if (it is ForwardMessage) {
                    forwardMessage = it
                }
            }

            // 合并转发消息检测
            if (!muted && forwardMessage != null) {
                forwardMessage!!.nodeList.forEach {
                    val forwardMessageItemString = it.messageChain.getPlainText()
                    if (!muted && RegexUtils.matchRegex(
                            adRegex,
                            forwardMessageItemString
                        ) && forwardMessageItemString.length >= stringLength
                    ) {
                        try {
                            recalledMessage++
                            e.message.recall()
                            if (predicted) {
                                e.group.mute(e.sender, "非法发言内容 (在合并转发消息内) (模型证实)")
                                riskList.add(e.sender)
                                setVl(e.sender.id, 99.0)
                            } else {
                                e.sender.mute(60, "非法发言内容 (在合并转发消息内)")
                            }
                            muted = true
                        } catch (_: Exception) {
                        }
                        messagesHandled++
                    }
                }
            }

            // 拼音检查发言
            if (!muted && riskList.indexOf(e.sender) != -1 && RegexUtils.matchRegexPinyin(adPinyinRegex, textMessage)) {
                try {
                    recalledMessage++
                    e.message.recall()
                    e.group.mute(e.sender, "非法发言内容")
                    muted = true
                } catch (_: Exception) {
                }
                setVl(e.sender.id, 99.0)
                messagesHandled++
            }

            if (!muted && predicted) {
                if (textMessage.length >= stringLength) {
                    if (RegexUtils.matchRegexPinyin(adPinyinRegex, textMessage)) {
                        e.group.mute(e.sender, "非法发言内容 (模型预测, 强检查证实)")
                        riskList.add(e.sender)
                        e.message.recall()
                        setVl(e.sender.id, 99.0)
                        muted = true
                    } else if (predictedResult[1] - predictedResult[0] >= 0.22) {
                        e.sender.mute(120, "非法发言内容 (启发式检查)")
                        e.message.recallIn(500L)
                        riskList.add(e.sender)
                        setVl(e.sender.id, 99.0)
                        muted = true
                    } else if (abs(predictedResult[1] / predictedResult[0]) >= 2.5) {
                        e.message.recallIn(500L)
                        e.sender.mute(60, "非法发言内容 (启发式猜测)")
                        setVl(e.sender.id, 60.0)
                        muted = true
                    } else {
                        // 长消息误判率较低，除非过长
                        addVl(e.sender.id, 49.0 * (predictedResult[1] - predictedResult[0]), "启发式长期分析")
                        riskList.add(e.sender)
                    }
                }
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
                val similar = findSimilarity(repeaterFucker[e.sender.id]!!, textMessage)
                if (similar >= 0.75) {
                    addVl(e.sender.id, similar * 25)
                } else {
                    addVl(e.sender.id, -20.0)
                }
                repeaterFucker[e.sender.id] = e.message.content
            }

            // VL处罚
            if (memberVl[e.sender.id]!! >= Config.vlPunish && !muted) {
                // 对线完了吧大概
//                if (e.sender.id == 1242788764L) return
                e.group.mute(e.sender, "不允许的速度/发言内容重复")
                muted = true
                recalledMessage++
                e.message.recall()
                try {
                    historyMessage[e.sender.id]?.forEach {
                        recalledMessage++
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

        val counter = VLManager.getCounter(e.sender)
        WordUtils.filter(e.message.content).forEach {
            if (counter.wordFrequency[it] != null) {
                counter.wordFrequency[it] = counter.wordFrequency[it]!! + 1
            } else {
                counter.wordFrequency[it] = 0
            }
        }
        // Permission block
        if ((e.sender.permitteeId.hasPermission(PluginMain.blocked) && !e.sender.permitteeId.hasPermission(bypassMute)) || (e.group.permitteeId.hasPermission(
                PluginMain.blocked
            ) && !e.sender.permitteeId.hasPermission(bypassMute))
        ) {
            muted = true
        }

        // Cancel Event
        if (muted) e.intercept()
    }

    fun addVl(id: Long, vl: Double, info: String? = null) {
        if (memberVl[id] == null) {
            memberVl[id] = .0
        }
        val tempValue = memberVl[id]!!
        memberVl[id] = memberVl[id]!! + vl
        if (memberVl[id]!! >= 0.0 || tempValue > 0.0) {
            val infoStr = info?.let { "因为$it" } ?: ""
            logger.info("$id 的VL${infoStr}增加了 $vl, 现在是 ${memberVl[id]}")
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
            else 600, "Message Filter: $reason"
        )
    }
}
