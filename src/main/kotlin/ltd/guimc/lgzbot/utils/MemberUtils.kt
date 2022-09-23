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