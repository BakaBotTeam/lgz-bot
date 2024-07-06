package ltd.guimc.lgzbot.listener.message

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.RichMessage

object AntiMappLinkListener {
    suspend fun filter(e: GroupMessageEvent) {
        for (singleMessage in e.message) {
            if (singleMessage is RichMessage && singleMessage.content.contains("mqqapi:\\/\\/")) {
                e.subject.sendMessage(QuoteReply(e.source) + "WARN: 本可能消息包含mqqapi链接 请注意安全")
            }
        }
    }
}