package ltd.guimc.lgzbot.plugin.command

import ltd.guimc.lgzbot.plugin.PluginMain
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand

object MusicCommand: CompositeCommand(
    owner = PluginMain,
    primaryName = "music",
    description = "音乐助手"
) {
    @SubCommand("点歌", "dg")
    @Description("点首歌听吧~")
    suspend fun CommandSender.lgzbot_music_dg(name: String) {

    }
}