package ltd.guimc.lgzbot.listener.message

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*

object AntiMappLinkListener {
    suspend fun filter(e: GroupMessageEvent) {
        if (check(e.message)) e.subject.sendMessage(QuoteReply(e.source) + "WARN: 本消息可能包含mqqapi链接 请注意安全")
    }

    fun check(m: MessageChain): Boolean {
        for (singleMessage in m) {
            if (singleMessage is RichMessage && singleMessage.content.contains("mqqapi:\\/\\/")) {
                return true
            }

            if (singleMessage is ForwardMessage) {
                return check(singleMessage.toMessageChain())
            }
        }
        return false
    }
}