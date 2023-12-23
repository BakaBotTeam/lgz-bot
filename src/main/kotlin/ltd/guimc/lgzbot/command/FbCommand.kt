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
import ltd.guimc.lgzbot.utils.CooldownUtils
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
    val cooldown = CooldownUtils(30000)

    @Handler
    fun CommandSender.onHandler() = ltd_guimc_lgzbot_fb()

    fun CommandSender.ltd_guimc_lgzbot_fb() = launch {
        requireNotNull(user) { "请在聊天环境中使用该指令" }
        if (cooldown.isTimePassed(user!!)) {
            if (cooldown.shouldSendCooldownNotice(user!!)) sendMessage("你可以在 ${cooldown.getLeftTime(user!!) / 1000} 秒后继续使用该指令")
            return@launch
        }
        cooldown.flag(user!!)

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