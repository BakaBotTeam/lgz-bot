/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.command

import kotlinx.coroutines.launch
import ltd.guimc.lgzbot.PluginMain
import net.mamoe.mirai.console.command.BuiltInCommands.HelpCommand.generateDefaultHelp
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.ConsoleCommandSender.permitteeId
import net.mamoe.mirai.console.command.ConsoleCommandSender.sendMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.isConsole

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
            require(bot != null)
            sendMessage("请稍等")
            for (i in PluginMain.helpMessages!!) {
                sendMessage(i)
            }
        }
    }
}