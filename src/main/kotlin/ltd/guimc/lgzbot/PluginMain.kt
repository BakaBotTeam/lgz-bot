/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot


import ltd.guimc.lgzbot.command.*
import ltd.guimc.lgzbot.files.Config
import ltd.guimc.lgzbot.files.GithubSubConfig
import ltd.guimc.lgzbot.listener.message.GithubUrlListener
import ltd.guimc.lgzbot.listener.message.MessageFilter
import ltd.guimc.lgzbot.utils.MessageUtils.getPlainText
import ltd.guimc.lgzbot.utils.RegexUtils.getDefaultPinyinRegex
import ltd.guimc.lgzbot.utils.RegexUtils.getDefaultRegex
import net.mamoe.mirai.console.command.BuiltInCommands
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.plugin.author
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.plugin.version
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.ForwardMessage
import net.mamoe.mirai.message.data.ForwardMessageBuilder
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        "ltd.guimc.lgzbot.plugin",
        "0.1.2",
        "LgzBot",
    ){
        author("汐洛 & YounKoo & 笨蛋们")
    }
) {
    lateinit var bypassMute: Permission
    lateinit var blocked: Permission
    lateinit var adRegex: Array<Regex>
    lateinit var adPinyinRegex: Array<Regex>
    var helpMessages: Array<ForwardMessage>? = null

    val iI1I1i1I1i1: Regex = Regex("@.*#.* with <.*>")

    override fun onEnable() {
        logger.info("$name v$version by $author Loading")
        adRegex = getDefaultRegex()
        adPinyinRegex = getDefaultPinyinRegex()

        registerPerms()
        registerCommands()
        registerEvents()
        Config.reload()
        GithubSubConfig.reload()
        logger.info("$name v$version by $author Loaded")
    }

    override fun onDisable() {
        logger.info("$name v$version by $author Disabling")
        Config.save()
        GithubSubConfig.save()
    }

    private fun registerPerms() = PermissionService.INSTANCE.run {
        bypassMute = register(PermissionId("lgzbot", "bypassmute"), "让某个笨蛋绕过广告禁言")
        blocked = register(PermissionId("lgzbot", "blocked"), "坏蛋专属权限!")
    }

    private fun registerCommands() = CommandManager.run {
        registerCommand(LGZBotCommand)
        registerCommand(MusicCommand)
        registerCommand(ACGCommand)
        registerCommand(RiskCommand)
        registerCommand(HttpCatCommand)
        registerCommand(HelpCommand)
        registerCommand(GithubSubCommand)
    }

    private fun registerEvents() = GlobalEventChannel.run {
        subscribeAlways<BotOnlineEvent> {
            if (helpMessages == null) {
                val _helpMessages = mutableListOf<ForwardMessage>()
                var helpMessage = ForwardMessageBuilder(it.bot.asFriend)
                var length = 0
                BuiltInCommands.HelpCommand
                    .generateDefaultHelp(AbstractPermitteeId.Console)
                    .split("\n")
                    .forEach { str ->
                        if (length >= 60) {
                            _helpMessages.add(helpMessage.build())
                            helpMessage = ForwardMessageBuilder(it.bot.asFriend)
                            length = 0
                        }

                        length++
                        helpMessage.add(it.bot, PlainText(str))
                    }
                _helpMessages.add(helpMessage.build())
                helpMessages = _helpMessages.toTypedArray()
            }
        }

        subscribeAlways<GroupMessageEvent>(priority = EventPriority.HIGHEST) { event -> MessageFilter.filter(event) }

        subscribeAlways<GroupMessageEvent> { event -> GithubUrlListener.onMessage(event) }

        subscribeAlways<BotInvitedJoinGroupRequestEvent> {
            // it.accept()
            require(it.invitor != null) { "Must have a invitor in BotInvitedJoinGroupRequestEvent" }

            it.invitor!!.sendMessage(
                "貌似你很喜欢使用这个机器人呢！\n" +
                    "但是为了安全考虑 我们关闭了快速通过机器人受邀进群..\n" +
                    "您可以带着 Event ID 联系超管来让机器人进群!\n" +
                    "Event ID: ${it.eventId}"
            )
        }

        subscribeAlways<NewFriendRequestEvent> {
            it.accept()
            it.bot.getFriendOrFail(it.fromId).sendMessage(PlainText("你好呀 大笨蛋!"))
        }

        subscribeAlways<MessagePreSendEvent> { e ->
            if (e.target is Friend) if (iI1I1i1I1i1.containsMatchIn(
                    e.message.toMessageChain().getPlainText()
                )
            ) e.intercept()
        }
    }
}
