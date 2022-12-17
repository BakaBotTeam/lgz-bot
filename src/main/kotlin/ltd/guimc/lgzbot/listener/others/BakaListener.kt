/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */
package ltd.guimc.lgzbot.listener.others

import ltd.guimc.lgzbot.utils.RandomUtils
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText
import kotlin.random.Random

object BakaListener {
    // 使用 % 分割
    // 发送者: source, 目标(可能没有): target
    private val NUDGE: Array<String> =
        arrayOf("唔...", "喵! (咬)", "不要动我! (气鼓鼓)", "source%是大坏蛋!", "打！", ">_<")
    private val RECALL: Array<String> = arrayOf("不用再撤回啦~ 我都看到了! %source")
    private val QUIT: Array<String> = arrayOf("source% 悄悄退群了")
    private val KICK: Array<String> = arrayOf("[神权提示]%target% were kicked by %source%!")
    private val MUTE: Array<String> = arrayOf("[神权提示]%source% 禁言了 %target%!")
    private val UNMUTE: Array<String> = arrayOf("[神权提示]%source% 解除禁言 %target%!")
    private val NEW_MEMBER: Array<String> = arrayOf("欢迎新笨蛋 %source%!", "本群的新RBQ %source% 来啦!")
    private val MUTE_TO_BOT: Array<String> = arrayOf("为什么禁言我...呜呜 ┭┮﹏┭┮")
    private val UNMUTE_TO_BOT: Array<String> = arrayOf("我...我能说话了! 谢谢大笨蛋%source%!")
    private val BOT_JOIN_GROUP: Array<String> = arrayOf("锵锵! 本笨蛋姬器人来啦!")

    private val rand = Random(RandomUtils.randomLong())

    suspend fun nudge(e: NudgeEvent) {
        if (e.target == e.bot) {
            e.subject.sendMessage(format(NUDGE.random(), e.target.id, e.from.id))
        }
    }

    suspend fun recall(e: MessageRecallEvent.GroupRecall) {
        if (e.operator == null) return
        if (e.authorId != e.operator!!.id) return
        if (rand.nextDouble() <= 0.4) {
            e.group.sendMessage(format(RECALL.random(), e.operator!!.id))
        }
    }

    suspend fun kick(e: MemberLeaveEvent) {
        if (e.member == e.bot) return
        if (e !is MemberLeaveEvent.Kick) {
            e.group.sendMessage(format(QUIT.random(), e.member.id))
            return
        }
        if (e.operator == null) return
        e.group.sendMessage(format(KICK.random(), e.member.id, e.operator!!.id))
    }

    suspend fun mute(e: MemberMuteEvent) {
        if (e.operator == null) return
        e.group.sendMessage(format(MUTE.random(), e.member.id, e.operator!!.id))
    }

    suspend fun unmute(e: MemberUnmuteEvent) {
        if (e.operator == null) return
        e.group.sendMessage(format(UNMUTE.random(), e.member.id, e.operator!!.id))
    }

    suspend fun newMember(e: MemberJoinEvent) = e.group.sendMessage(format(NEW_MEMBER.random(), e.member.id))
    suspend fun muteBot(e: BotMuteEvent) = e.operator.sendMessage(format(MUTE_TO_BOT.random()))
    suspend fun unmuteBot(e: BotUnmuteEvent) = e.group.sendMessage(format(UNMUTE_TO_BOT.random(), e.operator.id))
    suspend fun botJoinGroup(e: BotJoinGroupEvent) = e.group.sendMessage(format(BOT_JOIN_GROUP.random()))

    private fun format(str: String, target: Long, source: Long): MessageChain {
        val messages = MessageChainBuilder()
        for (i in str.split("%")) {
            messages += when (i) {
                "source" -> if (source != -1L) At(source) else PlainText("source")
                "target" -> if (target != -1L) At(target) else PlainText("target")
                else -> PlainText(i)
            }
        }
        return messages.build()
    }

    private fun format(str: String): MessageChain = format(str, -1L, -1L)
    private fun format(str: String, source: Long): MessageChain = format(str, -1L, source)
}