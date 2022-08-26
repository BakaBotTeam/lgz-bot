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
        author("Guimc")
    }
) {
    lateinit var notMuteMessagePush: Permission
    lateinit var notTalkativeMessagePush: Permission
    lateinit var bypassMute: Permission
    lateinit var commitListener: CommitListener
    lateinit var adRegex: Array<Regex>
    var helpMessage: ForwardMessage? = null


    override fun onEnable() {
        logger.info("$name v$version Loading")
        adRegex = getDefaultRegex()
        commitListener = CommitListener()

        registerPerms()
        registerCommands()
        registerEvents()
        Config.reload()
        GithubSubConfig.reload()
        logger.info("$name v$version Loaded")
    }

    override fun onDisable() {
        Config.save()
        GithubSubConfig.save()
    }

    private fun registerPerms() = PermissionService.INSTANCE.run {
        notMuteMessagePush = register(PermissionId("lgzbot.event.notpush", "mute"), "不推送禁言权限 (仅适用于群聊)")
        notTalkativeMessagePush = register(PermissionId("lgzbot.event.notpush", "talkative"), "不推送新龙王权限 (仅适用于群聊)")
        bypassMute = register(PermissionId("lgz.plugin", "bypassmute"), "Bypass Mute")

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

        subscribeAlways<MemberMuteEvent> {
            if (!it.group.permitteeId.hasPermission(notMuteMessagePush)) {
                if (it.operator != null) {
                    it.group.sendMessage(
                        PlainText("[禁言推送] ") +
                            At(it.operatorOrBot) +
                            PlainText(" 禁言了 ") +
                            At(it.member) +
                            PlainText(" ,时长: ${round(it.durationSeconds / 60.0)} 分钟.")
                    )
                }
            }
        }
        subscribeAlways<MemberUnmuteEvent> {
            if (!it.group.permitteeId.hasPermission(notMuteMessagePush)) {
                if (it.operator != null) {
                    it.group.sendMessage(
                        PlainText("[禁言推送] ") +
                            At(it.operatorOrBot) +
                            PlainText(" 解禁了 ") +
                            At(it.member)
                    )
                }
            }
        }

        subscribeAlways<BotInvitedJoinGroupRequestEvent> { it.accept() }
        subscribeAlways<NewFriendRequestEvent> { it.accept() }
    }
}