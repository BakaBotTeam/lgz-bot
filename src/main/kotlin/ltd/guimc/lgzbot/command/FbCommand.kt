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
import ltd.guimc.lgzbot.PluginMain.fbValue
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText

object FbCommand: SimpleCommand(
    owner = PluginMain,
    primaryName = "fb",
    description = "嘿嘿...❤"
) {
    @Handler
    fun CommandSender.onHandler() = ltd_guimc_lgzbot_fb()

    fun CommandSender.ltd_guimc_lgzbot_fb() = launch {
        requireNotNull(user) { "我不能对着控制台发情" }
        var msg: Message = PlainText("")

        val context = fbValue.random().replace("\\n","\n")
        if (context.startsWith("$")) {
            msg += At(user!!)
        }
        for (s in context.split("$")) {
            msg += PlainText(s)
            msg += At(user!!)
        }

        sendMessage(msg)
    }
}