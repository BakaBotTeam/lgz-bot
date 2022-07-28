package ltd.guimc.lgzbot.plugin.utils

import net.mamoe.mirai.message.data.ForwardMessage
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText

object MessageUtils {
    fun MessageChain.getPlainText(): String {
        var pt: String = ""
        for (i in listIterator()) if (i is PlainText) pt += i.content
        return pt
    }

    fun MessageChain.getForwardMessage(): MessageChain {
        var msgcb = MessageChainBuilder()
        for (u in listIterator()) if (u is ForwardMessage) for (i in u.nodeList) for (k in i.messageChain.listIterator()) msgcb.add(k)
        return msgcb.build()
    }
}