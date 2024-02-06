/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.utils

import net.mamoe.mirai.message.data.ForwardMessage
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText

object MessageUtils {
    fun MessageChain.getPlainText(): String {
        var pt = ""
        for (i in listIterator()) if (i is PlainText) pt += i.content
        return pt
    }

    fun MessageChain.getFullText(): String {
        var pt = ""
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
        val msgcb = MessageChainBuilder()
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