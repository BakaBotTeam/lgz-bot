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
import ltd.guimc.lgzbot.utils.HomoIntUtils
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand

object HomoIntCommand : SimpleCommand(
    owner = PluginMain,
    primaryName = "homoint",
    secondaryNames = arrayOf("homo"),
    description = "随处可见的Homo（恼"
) {
    @Handler
    fun CommandSender.onHandler(num: Long) = ltd_guimc_lgzbot_homoint(num)

    fun CommandSender.ltd_guimc_lgzbot_homoint(num: Long) = launch {
        sendMessage(HomoIntUtils.getInt(num))
    }
}