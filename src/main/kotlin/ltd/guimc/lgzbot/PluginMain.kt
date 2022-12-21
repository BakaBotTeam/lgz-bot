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
import ltd.guimc.lgzbot.listener.multi.BakaListener
import ltd.guimc.lgzbot.listener.mute.AutoQuit
import ltd.guimc.lgzbot.listener.nudge.AntiNudgeSpam
import ltd.guimc.lgzbot.utils.RegexUtils.getDefaultPinyinRegex
import ltd.guimc.lgzbot.utils.RegexUtils.getDefaultRegex
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
        "ltd.guimc.lgzbot.plugin",
        "0.2.2",
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
    lateinit var adRegex: Array<Regex>
    lateinit var adPinyinRegex: Array<Regex>

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
        nudgeMute = register(PermissionId("lgzbot", "nudgemute"), "戳一戳禁言")
        disableSpamCheck = register(PermissionId("lgzbot", "disablespamcheck"), "关闭群聊刷屏检查")
        disableADCheck = register(PermissionId("lgzbot", "disablespamcheck"), "关闭群聊广告检查")
    }

    private fun registerCommands() = CommandManager.run {
        registerCommand(LGZBotCommand)
        registerCommand(MusicCommand)
        registerCommand(ACGCommand)
        registerCommand(RiskCommand)
        registerCommand(HttpCatCommand)
        registerCommand(GithubSubCommand)
        registerCommand(DisableCheckCommand)
    }

    private fun registerEvents() = GlobalEventChannel.run {
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
        }

        // Anti NudgeSpam
        subscribeAlways<NudgeEvent>(priority = EventPriority.HIGHEST) { e -> AntiNudgeSpam.onNudge(e) }

        // BakaListener
        registerListenerHost(BakaListener)
        registerListenerHost(AutoQuit)
    }
}
