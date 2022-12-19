package ltd.guimc.lgzbot.listener.mute

import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListenerHost
import net.mamoe.mirai.event.events.BotMuteEvent
import net.mamoe.mirai.event.events.MessageEvent

object AutoQuit : ListenerHost {
    private const val autoQuitTime = 60 * 60 * 24

    @EventHandler
    suspend fun BotMuteEvent.onEvent() {
        if (this.durationSeconds >= autoQuitTime) {
            this.operator.sendMessage("[AutoQuit] 自动退出本群 由于机器人被禁言时长大于1d")
            this.group.quit()
        }
    }

    @EventHandler
    suspend fun MessageEvent.onEvent() {
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