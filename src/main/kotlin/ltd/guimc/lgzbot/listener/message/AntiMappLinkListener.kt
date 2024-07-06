package ltd.guimc.lgzbot.listener.message

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.ForwardMessage
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.RichMessage

object AntiMappLinkListener {
    suspend fun filter(e: GroupMessageEvent) {
        if (check(e.message)) e.subject.sendMessage(QuoteReply(e.source) + "WARN: 本消息可能包含mqqapi链接 请注意安全")
    }

    fun check(m: MessageChain): Boolean {
        for (singleMessage in m) {
            if (singleMessage is RichMessage && (singleMessage.content.contains("mqqapi:\\/\\/") || singleMessage.content.contains(
                    "mqqapi://"
                ))
            ) {
                return true
            }

            if (singleMessage is ForwardMessage) {
                return checkForwarded(singleMessage)
            }
        }
        return false
    }

    fun checkForwarded(m: ForwardMessage): Boolean {
        for (singleNode in m.nodeList) {
            if (check(singleNode.messageChain)) return true
        }
        return false
    }
}