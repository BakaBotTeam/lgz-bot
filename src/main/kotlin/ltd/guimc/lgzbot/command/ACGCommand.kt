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
import ltd.guimc.lgzbot.utils.CooldownUtils
import ltd.guimc.lgzbot.utils.ImageUtils
import ltd.guimc.lgzbot.utils.OverflowUtils
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.getGroupOrNull
import top.mrxiaom.overflow.contact.RemoteBot

object ACGCommand: SimpleCommand (
    owner = PluginMain,
    primaryName = "acg",
    description = "二次元图片"
) {
    val cooldown = CooldownUtils(10000)

    @Handler
    fun CommandSender.onHandler() = ltd_guimc_command_acg()

    fun CommandSender.ltd_guimc_command_acg() = launch {
        requireNotNull(user) { "请在聊天环境中使用该指令" }
        val group = getGroupOrNull()
        if (!cooldown.isTimePassed(user!!)) {
            if (cooldown.shouldSendCooldownNotice(user!!)) sendMessage("你可以在 ${ACGCommand.cooldown.getLeftTime(user!!) / 1000} 秒后继续使用该指令")
            return@launch
        }
        cooldown.flag(user!!)
        try {
            if (!OverflowUtils.checkOverflowCore()) {
                sendMessage(ImageUtils.url2imageMessage("https://www.dmoe.cc/random.php", bot!!, subject!!))
            } else {
                if (group != null) {
                    (bot as RemoteBot).executeAction("send_group_msg", "{\"group_id\": ${group.id}, \"message\": \"[CQ:image,url=https://www.dmoe.cc/random.php]\n\"}")
                }
            }
        } catch (ignore: Throwable) {
            sendMessage("Oops, something went wrong.")
            cooldown.addLeftTime(user!!, -10000L)
        }
    }
}