package ltd.guimc.lgzbot.command

import ltd.guimc.lgzbot.PluginMain
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.isConsole
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
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


//     @SubCommand("atspam")
//     @Description("F**k You!")
//     suspend fun CommandSender.atspam(target: Member, times: Int, sleepTime: Double) {
//         var sleepTime1 = sleepTime
//         if (target.permission.level > 0) {
//             sendMessage("你不能这么做")
//             return
//         }
//         if (sleepTime <= .0) {
//             sleepTime1 = 1.0
//         }
//         sendMessage("Ok! Processing...")
//         repeat(times) {
//             delay(round(sleepTime1 * 1000L).toLong())
//             var lastMesg = sendMessage(At(target) + PlainText(RandomUtils.randomText(6)))
//             // delay(100L)
//             lastMesg?.recall()
//         }
//     }
}