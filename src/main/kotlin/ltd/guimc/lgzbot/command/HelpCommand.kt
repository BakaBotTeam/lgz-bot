package ltd.guimc.lgzbot.command

import kotlinx.coroutines.launch
import ltd.guimc.lgzbot.PluginMain
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.console.command.BuiltInCommands.HelpCommand.generateDefaultHelp
import net.mamoe.mirai.console.command.ConsoleCommandSender.sendMessage
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.message.data.ForwardMessageBuilder
import net.mamoe.mirai.message.data.PlainText

object HelpCommand: SimpleCommand (
    owner = PluginMain,
    primaryName = "h",
    description = "."
) {
    @Handler
    fun CommandSender.onHandler() = ltd_guimc_command_help()

    fun CommandSender.ltd_guimc_command_help() = launch{
        if (isConsole()) {
            sendMessage(generateDefaultHelp(permitteeId))
        } else {
            sendMessage("请稍等")
            if (bot == null) throw Exception("bot == null")
            sendMessage(PluginMain.helpMessage!!)
        }
    }
}