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
import ltd.guimc.lgzbot.listener.message.FunListener
import ltd.guimc.lgzbot.listener.message.GithubUrlListener
import ltd.guimc.lgzbot.listener.message.MessageFilter
import ltd.guimc.lgzbot.listener.multi.BakaListener
import ltd.guimc.lgzbot.listener.mute.AutoQuit
import ltd.guimc.lgzbot.listener.nudge.AntiNudgeSpam
import ltd.guimc.lgzbot.utils.FbUtils.getFbValue
import ltd.guimc.lgzbot.utils.RegexUtils.getDefaultPinyinRegex
import ltd.guimc.lgzbot.utils.RegexUtils.getDefaultRegex
import ltd.guimc.lgzbot.utils.RequestUtils
import net.mamoe.mirai.console.command.CommandManager
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
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.event.events.NudgeEvent

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        "ltd.guimc.lgzbot",
        "0.3.1",
        "LgzBot",
    ){
        author("BakaBotTeam")
    }
) {
    lateinit var bypassMute: Permission
    lateinit var blocked: Permission
    lateinit var nudgeMute: Permission
    lateinit var disableSpamCheck: Permission
    lateinit var disableADCheck: Permission
    lateinit var root: Permission
    lateinit var disableRoot: Permission
    lateinit var adRegex: Array<Regex>
    lateinit var adPinyinRegex: Array<Regex>
    lateinit var fbValue: Array<String>
    var isRunning = false

    override fun onEnable() {
        logger.info("$name v$version by $author Loading")
        Config.reload()
        GithubSubConfig.reload()

        adRegex = getDefaultRegex()
        adPinyinRegex = getDefaultPinyinRegex()
        fbValue = getFbValue()

        registerPerms()
        registerCommands()
        registerEvents()
        isRunning = true
        logger.info("$name v$version by $author Loaded")
    }

    override fun onDisable() {
        logger.info("$name v$version by $author Disabling")
        isRunning = false
        Config.save()
        GithubSubConfig.save()
    }

    private fun registerPerms() = PermissionService.INSTANCE.run {
        root = register(PermissionId("lgzbot", "*"), "The root permission")
        bypassMute = register(PermissionId("lgzbot", "bypassmute"), "让某个笨蛋绕过广告禁言", root)
        blocked = register(PermissionId("lgzbot", "blocked"), "坏蛋专属权限!", root)
        nudgeMute = register(PermissionId("lgzbot", "nudgemute"), "戳一戳禁言", root)

        disableRoot = register(PermissionId("lgzbot.disable", "*"), "The root permission", root)
        disableSpamCheck = register(PermissionId("lgzbot.disable", "spamcheck"), "关闭群聊刷屏检查", disableRoot)
        disableADCheck = register(PermissionId("lgzbot.disable", "adcheck"), "关闭群聊广告检查", disableRoot)
    }

    private fun registerCommands() = CommandManager.run {
        registerCommand(LGZBotCommand)
        registerCommand(ACGCommand)
        registerCommand(HttpCatCommand)
        registerCommand(ToggleCheckCommand)
        registerCommand(ReviewCommand)
        registerCommand(HypixelCommand)
        registerCommand(FbCommand)
    }

    private fun registerEvents() = GlobalEventChannel.run {
        subscribeAlways<GroupMessageEvent>(priority = EventPriority.HIGHEST) { event -> MessageFilter.filter(event) }

        subscribeAlways<GroupMessageEvent> { event -> GithubUrlListener.onMessage(event); FunListener.onMessage(event) }

        subscribeAlways<BotInvitedJoinGroupRequestEvent> {
            // it.accept()
            require(it.invitor != null) { "Must have a invitor in BotInvitedJoinGroupRequestEvent" }

            it.invitor!!.sendMessage(
                "Event ID: ${it.eventId}"
            )
            it.invitor!!.sendMessage(
                "请将上面的消息发送给机器人所有者/机器人所有者所授权的人来通过此次邀请进群\n" +
                    "注意: 请不要截图发送 而是把上面一条消息复制发送给指定的人!"
            )
            RequestUtils.Group.add(it)
        }

        subscribeAlways<NewFriendRequestEvent> {
            it.accept()
        }

        // Anti NudgeSpam
        subscribeAlways<NudgeEvent>(priority = EventPriority.HIGHEST) { e -> AntiNudgeSpam.onNudge(e) }

        registerListenerHost(BakaListener)
        registerListenerHost(AutoQuit)
    }
}
