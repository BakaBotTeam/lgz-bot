/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */
package ltd.guimc.lgzbot.listener.multi

import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.files.ModuleStateConfig
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.contact.UserOrBot
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListenerHost
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText

object BakaListener : ListenerHost {
    // 使用 % 分割
    // 发送者: source, 目标(可能没有): target
    private val NUDGE: Array<String> =
        arrayOf("唔...", "喵! (咬)", "不要动我! (气鼓鼓)", "source%是大坏蛋!", "打！", ">_<")

    // private val RECALL: Array<String> = arrayOf("不用再撤回啦~ 我都看到了! %source")
    private val QUIT: Array<String> = arrayOf("[GroupListener] %source% left the group.")
    private val KICK: Array<String> = arrayOf("[GroupListener] %target% have been removed from your group.")
    private val MUTE: Array<String> = arrayOf("[GroupListener] %source% muted %target%.")
    private val UNMUTE: Array<String> = arrayOf("[GroupListener] %source% unmuted %target%.")
    private val NEW_MEMBER: Array<String> = arrayOf("[GroupListener] New Member! %source%!")
    private val MUTE_TO_BOT: Array<String> = arrayOf("为什么禁言我...呜呜 ┭┮﹏┭┮")
    private val UNMUTE_TO_BOT: Array<String> = arrayOf("我...我能说话了! 谢谢%source%!")

    // private val rand = Random(RandomUtils.randomLong())

    @EventHandler
    suspend fun NudgeEvent.nudge() {
        if (!ModuleStateConfig.nudge) return
        if (this.target == this.bot) {
            this.subject.sendMessage(format(NUDGE.random(), this.target, this.from))
        }
    }

    // @EventHandler
    // suspend fun MessageRecallEvent.GroupRecall.recall() {
    //     if (!ModuleStateConfig.grouplistener) return
    //     if (this.operator == null) return
    //     if (this.authorId != this.operator!!.id) return
    //     if (rand.nextDouble() >= 0.9) {
    //         this.group.sendMessage(format(RECALL.random(), this.operator!!))
    //     }
    // }

    @EventHandler
    suspend fun MemberLeaveEvent.kick() {
        if (!ModuleStateConfig.grouplistener) return
        if (this.member == this.bot) return
        if (this.group.permitteeId.hasPermission(PluginMain.quiet)) return
        if (this !is MemberLeaveEvent.Kick) {
            this.group.sendMessage(format(QUIT.random(), this.member))
            return
        }
        if (this.operator == null) return
        this.group.sendMessage(format(KICK.random(), this.member, this.operator!!))
    }

    @EventHandler
    suspend fun MemberMuteEvent.mute() {
        if (!ModuleStateConfig.grouplistener) return
        if (this.group.permitteeId.hasPermission(PluginMain.quiet)) return
        if (this.operator == null) return
        this.group.sendMessage(format(MUTE.random(), this.member, this.operator!!))
    }

    @EventHandler
    suspend fun MemberUnmuteEvent.unmute() {
        if (!ModuleStateConfig.grouplistener) return
        if (this.group.permitteeId.hasPermission(PluginMain.quiet)) return
        if (this.operator == null) return
        this.group.sendMessage(format(UNMUTE.random(), this.member, this.operator!!))
    }

    @EventHandler
    suspend fun MemberJoinEvent.newMember() {
        if (!ModuleStateConfig.grouplistener) return
        if (this.group.permitteeId.hasPermission(PluginMain.quiet)) return
        this.group.sendMessage(format(NEW_MEMBER.random(), this.member))
    }

    @EventHandler
    suspend fun BotMuteEvent.muteBot() {
        if (!ModuleStateConfig.grouplistener) return
        if (this.group.permitteeId.hasPermission(PluginMain.quiet)) return
        this.operator.sendMessage(format(MUTE_TO_BOT.random()))
    }

    @EventHandler
    suspend fun BotUnmuteEvent.unmuteBot() {
        if (!ModuleStateConfig.grouplistener) return
        if (this.group.permitteeId.hasPermission(PluginMain.quiet)) return
        this.group.sendMessage(format(UNMUTE_TO_BOT.random(), this.operator))
    }

    private fun format(str: String, target: UserOrBot?, source: UserOrBot?): MessageChain {
        val messages = MessageChainBuilder()
        for (i in str.split("%")) {
            messages += when (i) {
                "source" -> if (source != null) PlainText("${source.nameCardOrNick}(${source.id})") else PlainText("source")
                "target" -> if (target != null) PlainText("${target.nameCardOrNick}(${target.id}") else PlainText("target")
                else -> PlainText(i)
            }
        }
        return messages.build()
    }

    private fun format(str: String): MessageChain = format(str, null, null)
    private fun format(str: String, source: UserOrBot?): MessageChain = format(str, null, source)
}