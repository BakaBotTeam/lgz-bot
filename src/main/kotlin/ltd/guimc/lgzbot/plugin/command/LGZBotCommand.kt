package ltd.guimc.lgzbot.plugin.command

import ltd.guimc.lgzbot.plugin.PluginMain
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand

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
}