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
import ltd.guimc.lgzbot.listener.message.MessageFilter
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import kotlin.math.roundToInt
import kotlin.time.Duration

object LGZBotCommand: CompositeCommand (
    owner = PluginMain,
    primaryName = "lgzbot",
    description = "LGZBot插件的主命令"
) {
    @SubCommand("ping")
    @Description("看看机器人是否在线吧")
    suspend fun CommandSender.ping() {
        sendMessage("Pong!")
    }

    @SubCommand("mute")
    @Description("把某人的嘴巴用胶布粘上")
    suspend fun CommandSender.mute(user: Member, time: String, reason: String) {
        try {
            val second: Long = Duration.parse(time).inWholeSeconds

            user.mute(second.toInt())
            user.group.sendMessage(
                PlainText("[滥权小助手] ")+
                    At(user)+
                    PlainText(" 获得了来自 ${if (isConsole()) "CONSOLE" else name} 的禁言\n")+
                    PlainText("时长: ${(second/60.0*100.0).roundToInt().toDouble()/100.0} 分钟\n")+
                    PlainText("理由: $reason")
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
            user.group.sendMessage(
                PlainText("[滥权小助手] ")+
                    At(user)+
                    PlainText(" 获得了来自 ${if (isConsole()) "CONSOLE" else name} 的解除禁言")
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
                PlainText("清除了")+
                    At(member)+
                    PlainText("的VL, 并移出在本群的风险管控")
            )
        } catch (e: Exception) {
            sendMessage("Oops! 在尝试执行操作的时候发生了一些错误!")
            e.printStackTrace()
        }
    }
}