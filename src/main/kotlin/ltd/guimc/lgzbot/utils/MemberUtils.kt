/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.utils

import ltd.guimc.lgzbot.command.LGZBotCommand.mute
import ltd.guimc.lgzbot.command.LGZBotCommand.unmute
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.contact.Member

object MemberUtils {
    suspend fun Member.mute(durationSeconds: Int, reason: String) {
        ConsoleCommandSender.mute(this, durationSeconds, reason)
    }

    suspend fun Member.unmute() {
        ConsoleCommandSender.unmute(this)
    }
}