package ltd.guimc.lgzbot


import ltd.guimc.lgzbot.command.*
import ltd.guimc.lgzbot.files.Config
import ltd.guimc.lgzbot.files.GithubSubConfig
import ltd.guimc.lgzbot.listener.github.CommitListener
import ltd.guimc.lgzbot.listener.message.GithubUrlListener
import ltd.guimc.lgzbot.listener.message.MessageFilter
import ltd.guimc.lgzbot.utils.RegexUtils.getDefaultRegex
import net.mamoe.mirai.console.command.BuiltInCommands
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.console.plugin.author
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.plugin.version
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.ForwardMessage
import net.mamoe.mirai.message.data.ForwardMessageBuilder
import net.mamoe.mirai.message.data.PlainText
import kotlin.math.round

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        "ltd.guimc.lgzbot.plugin",
        "0.1.2",
        "LgzBot",
    ){
        author("汐洛 & YounKoo & 笨蛋们")
    }
) {
    lateinit var notMuteMessagePush: Permission
    lateinit var notTalkativeMessagePush: Permission
    lateinit var bypassMute: Permission
    lateinit var blocked: Permission
    lateinit var commitListener: CommitListener
    lateinit var adRegex: Array<Regex>
    var helpMessage: ForwardMessage? = null


    override fun onEnable() {
        logger.info("$name v$version by $author Loading")
        adRegex = getDefaultRegex()
        commitListener = CommitListener()

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

        commitListener.isQuitting = true
    }

    private fun registerPerms() = PermissionService.INSTANCE.run {
        notMuteMessagePush = register(PermissionId("lgzbot.event.notpush", "mute"), "不推送禁言权限 (仅适用于群聊)")
        notTalkativeMessagePush = register(PermissionId("lgzbot.event.notpush", "talkative"), "不推送新龙王权限 (仅适用于群聊)")
        bypassMute = register(PermissionId("lgz.plugin", "bypassmute"), "让某个笨蛋绕过广告禁言")
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
            if (helpMessage != null) return@subscribeAlways
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
