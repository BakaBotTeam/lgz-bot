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
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.BotOnlineEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.message.data.ForwardMessage
import net.mamoe.mirai.message.data.ForwardMessageBuilder
import net.mamoe.mirai.message.data.PlainText

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
    var helpMessage: ForwardMessage? = null


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
            require(helpMessage != null)
            helpMessage = ForwardMessageBuilder(it.bot.asFriend)
                .add(bot, PlainText(
                    BuiltInCommands.HelpCommand.generateDefaultHelp(AbstractPermitteeId.Console)
                ))
                .build()
        }

        subscribeAlways<GroupMessageEvent>(priority = EventPriority.HIGHEST) { event -> MessageFilter.filter(event) }

        subscribeAlways<GroupMessageEvent> { event -> GithubUrlListener.onMessage(event) }

        subscribeAlways<BotInvitedJoinGroupRequestEvent> { it.accept() }
        subscribeAlways<NewFriendRequestEvent> { it.accept() }
    }
}
