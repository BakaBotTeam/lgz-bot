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
import ltd.guimc.lgzbot.utils.ImageUtils
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand

object ACGCommand: SimpleCommand (
    owner = PluginMain,
    primaryName = "acg",
    description = "二次元图片"
) {
    @Handler
    fun CommandSender.onHandler() = ltd_guimc_command_acg()

    fun CommandSender.ltd_guimc_command_acg() = launch {
        try {
            sendMessage(ImageUtils.url2imageMessage("https://www.dmoe.cc/random.php", bot!!, subject!!))
        } catch (ignore: Throwable) {
            sendMessage("Oops, something went wrong.")
        }
    }
}