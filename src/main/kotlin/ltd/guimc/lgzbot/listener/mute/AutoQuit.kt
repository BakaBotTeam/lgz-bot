package ltd.guimc.lgzbot.listener.mute

import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListenerHost
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.BotMuteEvent

object AutoQuit : ListenerHost {
    private const val autoQuitTime = 60 * 60 * 24 // 1Day

    @EventHandler
    suspend fun BotMuteEvent.onEvent() {
        if (this.durationSeconds >= autoQuitTime) {
            this.operator.sendMessage("[AutoQuit] 触发自动退群")
            this.group.quit()
        }
    }

    @EventHandler
    suspend fun BotEvent.onEvent() {
        try {
            this.bot.groups.forEach {
                if (it.botMuteRemaining >= autoQuitTime) {
                    it.quit()
                }
            }
        } catch (ignore: Throwable) {
            ;
        }
    }
}