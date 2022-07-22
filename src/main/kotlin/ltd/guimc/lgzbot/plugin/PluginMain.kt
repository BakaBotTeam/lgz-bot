package ltd.guimc.lgzbot.plugin

import ltd.guimc.lgzbot.plugin.command.HypixelCommand
import ltd.guimc.lgzbot.plugin.command.LGZBotCommand
import ltd.guimc.lgzbot.plugin.command.MusicCommand
import ltd.guimc.lgzbot.plugin.files.Config
import ltd.guimc.lgzbot.plugin.files.Data
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.plugin.version
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        "ltd.guimc.lgzbot.plugin",
        "0.1.1",
        "LgzBot",
    ){
        author("Guimc")
    }
) {
    override fun onEnable() {
        logger.info("$name v$version Loading")
        registerCommands()
        registerEvents()
        Config.reload()
        Data.reload()
        logger.info("$name v$version Loaded")
    }

    override fun onDisable() {
        Config.save()
        Data.save()
    }

    private fun registerCommands() = CommandManager.run {
        registerCommand(LGZBotCommand)
        registerCommand(MusicCommand)
        registerCommand(HypixelCommand)
    }

    private fun registerEvents() = GlobalEventChannel.run {
        subscribeAlways<GroupMessageEvent> { event -> MessageFilter.filter(event) }
    }
}