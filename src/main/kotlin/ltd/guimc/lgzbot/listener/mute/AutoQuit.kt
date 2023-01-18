package ltd.guimc.lgzbot.listener.mute

import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListenerHost
import net.mamoe.mirai.event.events.BotMuteEvent

object AutoQuit : ListenerHost {
    private const val autoQuitTime = 60 * 60 // 1 Hour

    @EventHandler
    suspend fun BotMuteEvent.onEvent() {
        if (this.durationSeconds >= autoQuitTime) {
            this.operator.sendMessage("[AutoQuit] 触发自动退群")
            this.group.quit()
        }
    }
}