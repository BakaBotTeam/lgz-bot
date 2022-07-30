package ltd.guimc.lgzbot.plugin.utils

import net.mamoe.mirai.message.data.*

object MessageUtils {
    fun MessageChain.getPlainText(): String {
        var pt: String = ""
        for (i in listIterator()) if (i is PlainText) pt += i.content
        return pt
    }

    fun MessageChain.getFullText(): String {
        var pt: String = ""
        for (i in listIterator()) {
            if (i is PlainText) {
                pt += i.content
            }

            if (i is ForwardMessage) {
                pt += i.getPlainText()
            }
        }
        return pt
    }

    fun MessageChain.getForwardMessage(): MessageChain {
        var msgcb = MessageChainBuilder()
        for (u in listIterator()) if (u is ForwardMessage) for (i in u.nodeList) for (k in i.messageChain.listIterator()) msgcb.add(k)
        return msgcb.build()
    }

    fun ForwardMessage.getPlainText(): String {
        var pt = ""
        for (i in nodeList) {
            for (k in i.messageChain.listIterator()) {
                if (k is PlainText) {
                    pt += k.content
                }

                if (k is ForwardMessage) {
                    pt += k.getPlainText()
                }
            }
        }
        return pt
    }
}