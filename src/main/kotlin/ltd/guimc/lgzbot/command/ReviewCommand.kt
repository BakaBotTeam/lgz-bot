package ltd.guimc.lgzbot.command

import kotlinx.coroutines.launch
import ltd.guimc.lgzbot.PluginMain
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand

object ReviewCommand : SimpleCommand(
    owner = PluginMain,
    primaryName = "review",
    description = "处理Event ID用"
) {
    @Handler
    fun CommandSender.iiIiIIiII1i1I1i1I1i1II1i1I1i1I1(id: Long) = launch {
        // TODO
    }
}