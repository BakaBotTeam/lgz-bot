/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runInterruptible
import ltd.guimc.lgzbot.command.*
import ltd.guimc.lgzbot.files.Config
import ltd.guimc.lgzbot.files.GithubSubConfig
import ltd.guimc.lgzbot.files.GithubWebhookSubData
import ltd.guimc.lgzbot.files.ModuleStateConfig
import ltd.guimc.lgzbot.listener.message.FunListener
import ltd.guimc.lgzbot.listener.message.GithubUrlListener
import ltd.guimc.lgzbot.listener.message.MessageFilter
import ltd.guimc.lgzbot.listener.multi.BakaListener
import ltd.guimc.lgzbot.listener.mute.AutoQuit
import ltd.guimc.lgzbot.listener.nudge.AntiNudgeSpam
import ltd.guimc.lgzbot.listener.nudge.NudgeMute
import ltd.guimc.lgzbot.utils.FbUtils.getFbValue
import ltd.guimc.lgzbot.utils.LL4JUtils
import ltd.guimc.lgzbot.utils.RegexUtils.getDefaultPinyinRegex
import ltd.guimc.lgzbot.utils.RegexUtils.getDefaultRegex
import ltd.guimc.lgzbot.utils.RequestUtils
import ltd.guimc.lgzbot.webhook.GithubWebHookReciver
import ltd.guimc.lgzbot.webhook.WebHookService
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionId
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.plugin.version
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.event.events.NudgeEvent
import net.mamoe.mirai.event.globalEventChannel
import java.io.IOException
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import kotlin.concurrent.thread
import kotlin.io.path.name

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        "ltd.guimc.lgzbot",
        "0.3.4",
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
    lateinit var webHookService: WebHookService
    lateinit var configReloadThread: Thread
    lateinit var configReloadJob: Job
    var isRunning = false

    override fun onEnable() {
        logger.info("$name v$version 正在加载喵")
        Config.reload()
        GithubSubConfig.reload()
        ModuleStateConfig.reload()
        GithubWebhookSubData.reload()

        adRegex = getDefaultRegex()
        adPinyinRegex = getDefaultPinyinRegex()
        fbValue = getFbValue()
        webHookService = WebHookService(GithubWebHookReciver())

        registerPerms()
        registerCommands()
        registerEvents()
        isRunning = true
        configReloadJob = launch {
            val watchedPath = configFolderPath
            val watcher = try {
                runInterruptible(Dispatchers.IO) {
                    watchedPath.fileSystem.newWatchService()
                }
            } catch (_: IOException) {
                logger.warning("注册文件监听器失败, 这可能会导致配置文件无法及时更新")
                return@launch
            }

            runInterruptible(Dispatchers.IO) {
                watchedPath.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY)
            }

            var lastModify = 0L
            var changedFlag = false

            configReloadThread = thread {
                while (isRunning) {
                    if (changedFlag && System.currentTimeMillis() - lastModify >= 3000) {
                        Config.reload()
                        GithubSubConfig.reload()
                        ModuleStateConfig.reload()
                        logger.info("Reloaded Config")
                        changedFlag = false
                    }
                    Thread.sleep(1000)
                }
            }

            while (isRunning) {
                val watchkey = runInterruptible(Dispatchers.IO, watcher::take)

                for (event in watchkey.pollEvents()) {
                    val path = event.context() as? Path ?: continue
                    if (path.name.endsWith(".yml")) {
                        lastModify = System.currentTimeMillis()
                        changedFlag = true
                    }
                }

                if (!watchkey.reset()) {
                    watchkey.cancel()
                    watcher.close()
                    break
                }
            }
            logger.warning("文件监听器已退出")
        }
        webHookService.start()
        logger.info("正在初始化 LL4J")
        LL4JUtils.init()
        logger.info("$name v$version 加载好了喵")
    }

    override fun onDisable() {
        logger.info("$name v$version 正在卸载喵")
        GithubWebhookSubData.save()
        webHookService.stop()
        isRunning = false
        configReloadThread.join()
        logger.info("$name v$version 卸载好了喵")
    }

    private fun registerPerms() = PermissionService.INSTANCE.run {
        root = register(PermissionId("lgzbot", "*"), "The root permission")
        bypassMute = register(PermissionId("lgzbot", "bypassmute"), "消息过滤器禁言豁免", root)
        blocked = register(PermissionId("lgzbot", "blocked"), "完全屏蔽", root)
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
        registerCommand(GithubWebhookSubCommand)
        registerCommand(HomoIntCommand)
    }

    private fun registerEvents() = PluginMain.globalEventChannel().run {
        subscribeAlways<GroupMessageEvent>(priority = EventPriority.HIGHEST) { event -> MessageFilter.filter(event) }

        subscribeAlways<GroupMessageEvent> { event -> GithubUrlListener.onMessage(event); FunListener.onMessage(event) }

        if (ModuleStateConfig.invite) {
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
                it.invitor!!.sendMessage(
                    "本机器人的所有者: ${Config.BotOwner}"
                )
                RequestUtils.Group.add(it)
            }
        }

        subscribeAlways<NewFriendRequestEvent> {
            it.accept()
        }

        // Anti NudgeSpam
        subscribeAlways<NudgeEvent>(priority = EventPriority.HIGHEST) { e ->
            AntiNudgeSpam.onNudge(e)
            NudgeMute.onNudge(e)
        }

        registerListenerHost(BakaListener)
        registerListenerHost(AutoQuit)
    }
}
