/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.command

import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.counter.VLManager
import ltd.guimc.lgzbot.files.ModuleStateConfig
import ltd.guimc.lgzbot.listener.message.MessageFilter
import ltd.guimc.lgzbot.utils.LL4JUtils
import ltd.guimc.lgzbot.utils.MessageUtils.getPlainText
import ltd.guimc.lgzbot.utils.OverflowUtils
import ltd.guimc.lgzbot.word.WordUtils
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.message.data.*
import top.mrxiaom.overflow.contact.Updatable
import xyz.cssxsh.mirai.hibernate.MiraiHibernateRecorder
import kotlin.math.roundToInt
import kotlin.time.Duration

object LGZBotCommand : CompositeCommand(
    owner = PluginMain, primaryName = "lgzbot", description = "LGZBot插件的主命令"
) {
    @SubCommand("ping")
    @Description("看看机器人是否在线吧")
    suspend fun CommandSender.ping() {
        sendMessage("Pong!")
    }

    @SubCommand("word")
    @Description("词频统计")
    suspend fun CommandSender.word(user: Member,time: String) {
        try {
            sendMessage("他的词频信息:\n${WordUtils.hashMapToString(WordUtils.sortAndTrim(VLManager.getCounter(user).wordFrequency,Integer.parseInt(time)))}")
        } catch (e: Exception) {
            sendMessage("Oops! Something went wrong! ${e.message}")
        }
    }

    @SubCommand("mute")
    @Description("把某人的嘴巴用胶布粘上")
    suspend fun CommandSender.mute(user: Member, time: String, reason: String) {
        try {
            val second: Long = Duration.parse(time).inWholeSeconds

            user.mute(second.toInt())
            if (ModuleStateConfig.slientmute) return
            user.group.sendMessage(
                PlainText("[滥权小助手] ") + At(user) + PlainText(" 获得了来自 ${if (isConsole()) "CONSOLE" else name} 的禁言\n") + PlainText(
                    "时长: ${(second / 60.0 * 100.0).roundToInt().toDouble() / 100.0} 分钟\n"
                ) + PlainText("理由: $reason")
            )
        } catch (e: Exception) {
            sendMessage("Oops! Something went wrong! ${e.message}")
        }
    }

    @SubCommand("unmute")
    @Description("把胶布从某人的嘴巴上撕下来")
    suspend fun CommandSender.unmute(user: Member) {
        try {
            (user as NormalMember).unmute()
            if (ModuleStateConfig.slientmute) return
            user.group.sendMessage(
                PlainText("[滥权小助手] ") + At(user) + PlainText(" 获得了来自 ${if (isConsole()) "CONSOLE" else name} 的解除禁言")
            )
        } catch (e: Exception) {
            sendMessage("Oops! Something went wrong! ${e.message}")
        }
    }

    @SubCommand("clear")
    @Description("清除某人的状态")
    suspend fun CommandSender.I1Ii1I1(member: Member) {
        try {
            MessageFilter.riskList.remove(member)
            MessageFilter.clearVl(member.id)
            sendMessage(
                PlainText("清除了") + At(member) + PlainText("的VL, 并移出在本群的风险管控")
            )
        } catch (e: Exception) {
            sendMessage("Oops! 在尝试执行操作的时候发生了一些错误!")
            e.printStackTrace()
        }
    }

    @SubCommand("debug")
    @Description("获取Debug信息")
    suspend fun CommandSender.i1I1i1II1i1I1i() {
        val messageChain = MessageChainBuilder()
        messageChain.add("c=${MessageFilter.allCheckedMessage}, d=${MessageFilter.recalledMessage}, r=${(MessageFilter.recalledMessage / (MessageFilter.allCheckedMessage * 10000)).toDouble() / 100.0}\n")
        messageChain.add("o=${if (OverflowUtils.checkOverflowCore()) "true" else "false"}")
        if (OverflowUtils.checkOverflowCore()) messageChain.add(", on=${OverflowUtils.getOnebotServiceProviderName()}, ov=${OverflowUtils.getOnebotServiceProviderVersion()}, oc=${OverflowUtils.getOnebotConnection()}")
        sendMessage(messageChain.build())
    }

    @SubCommand("update")
    @Description("清理本群群成员缓存")
    suspend fun CommandSender.iI1Ii1I1i1I1i1I1() {
        if (!OverflowUtils.checkOverflowCore()) {
            sendMessage("该指令仅Overflow adaption可用")
            return
        }
        requireNotNull(getGroupOrNull())
        (getGroupOrNull()!! as Updatable).queryUpdate()
        sendMessage("ok.")
    }

    @SubCommand("check")
    @Description("使用模型检测一段文本是否合规")
    suspend fun CommandSenderOnMessage<*>.iI1I1i1I1i1I(string: String) {
        var str = string
        if (string == "reply") {
            val quote = fromEvent.message.findIsInstance<QuoteReply>() ?: return
            val raw = MiraiHibernateRecorder[quote.source] as MessageChain
            str = raw.getPlainText()
        }
        if (LL4JUtils.predictAllResult(str).let { it[1] > it[0] }) {
            sendMessage("不合规")
        } else {
            sendMessage("合规")
        }
    }

    @SubCommand("learn")
    @Description("让模型学习一段文本")
    suspend fun CommandSender.iI1I1i1iIi1I(type: Int, string: String) {
        LL4JUtils.learn(type, string)
        sendMessage("Done.")
    }

    @SubCommand("downloadModel")
    @Description("从ADDetector仓库下载模型")
    suspend fun CommandSender.llIIllIIllI() {
        sendMessage("Downloading (current: ${LL4JUtils.version})")
        LL4JUtils.downloadModel()
    }
}