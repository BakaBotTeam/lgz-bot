package ltd.guimc.lgzbot.plugin.command

import kotlinx.coroutines.delay
import ltd.guimc.lgzbot.plugin.PluginMain
import ltd.guimc.lgzbot.plugin.command.LGZBotCommand.unmute
import ltd.guimc.lgzbot.plugin.files.Config
import ltd.guimc.lgzbot.plugin.utils.RandomUtils
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import java.util.*
import kotlin.math.round
import kotlin.math.roundToInt

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
    suspend fun CommandSender.mute(user: Member, second: Int, reason: String) {
        try {
            user.mute(second)
            user.group.sendMessage(
                PlainText("[滥权小助手] ")+
                    At(user)+
                    PlainText(" 获得了来自 ${if (isConsole()) "CONSOLE" else name} 的禁言\n")+
                    PlainText("时长: ${(second/60.0*100.0).roundToInt().toDouble()/100.0} 分钟\n")+
                    PlainText("理由: $reason")
            )
        } catch (e: Exception) {
            sendMessage("好像...出了点问题... ${e.message}")
        }
    }

    @SubCommand("unmute")
    @Description("把某人嘴上的胶布撕下来")
    suspend fun CommandSender.unmute(user: Member) {
        try {
            unmute(user)
        }
        catch (e: Exception) {
            sendMessage("好像...出了点问题... ${e.message}")
        }
    }

    @SubCommand("unmute")
    @Description("把某人嘴上的胶布撕下来")
    suspend fun CommandSender.unmute(user: Member, group: Group) {
        try {
            unmute(user, group)
        }
        catch (e: Exception) {
            sendMessage("好像...出了点问题... ${e.message}")
        }
    }

     @SubCommand("atspam")
     @Description("F**k You!")
     suspend fun CommandSender.atspam(target: Member, times: Int, sleepTime: Double) {
         var sleepTime1 = sleepTime
         var times1 = times
         if (sleepTime <= .0) {
             sleepTime1 = 1.0
         }
         if (times1 > 10) {
             times1 = 10
         }
         sendMessage("Ok! Processing...")
         repeat(times1) {
             delay(round(sleepTime1 * 1000L).toLong())
             var lastMesg = sendMessage(At(target) + PlainText(RandomUtils.randomText(6)))
             // delay(100L)
             lastMesg?.recall()
         }
     }
}