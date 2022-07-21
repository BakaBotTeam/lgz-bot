package ltd.guimc.lgzbot.plugin.command

import kotlinx.coroutines.delay
import ltd.guimc.lgzbot.plugin.PluginMain
import ltd.guimc.lgzbot.plugin.utils.RandomUtils
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import java.util.*
import kotlin.math.round

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

    @SubCommand("atspam")
    @Description("F**k You!")
    suspend fun CommandSender.atspam(target: Member, times: Int, sleepTime: Double) {
        var sleepTime1 = sleepTime
        if (sleepTime <= .0) {
            sleepTime1 = 1.0
        }
        sendMessage("Ok! Processing...")
        repeat(times) {
            delay(round(sleepTime1 * 1000L).toLong())
            var lastMesg = sendMessage(At(target) + PlainText(RandomUtils.randomText(6)))
            // delay(100L)
            lastMesg?.recall()
        }
    }
}