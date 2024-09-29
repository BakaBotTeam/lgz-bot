package ltd.guimc.lgzbot.listener.message

import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.utils.RegexUtils
import ltd.guimc.lgzbot.utils.RegexUtils.replaceRegex
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.ListenerHost
import net.mamoe.mirai.event.events.MessagePreSendEvent
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain

object SelfMessageListener : ListenerHost {
    @EventHandler(priority = EventPriority.HIGHEST)
    suspend fun MessagePreSendEvent.onEvent() {
        val newMessageChain = MessageChainBuilder()
        this.message.toMessageChain().forEach { it ->
            if (it is PlainText && RegexUtils.matchRegex(PluginMain.seriousRegex, it.content)) {
                this.cancel()
                newMessageChain.add(it.content.replaceRegex(PluginMain.seriousRegex))
            } else {
                newMessageChain.add(it)
            }
        }
        if (this.isCancelled) {
            newMessageChain.add(" [Self-protect System]")
            this.target.sendMessage(newMessageChain.asMessageChain())
        }
    }
}