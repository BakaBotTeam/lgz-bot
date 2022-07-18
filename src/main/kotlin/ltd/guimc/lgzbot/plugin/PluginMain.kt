package ltd.guimc.lgzbot.plugin

import ltd.guimc.lgzbot.plugin.command.LGZBotCommand
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
        GlobalEventChannel.subscribeAlways<GroupMessageEvent> { event -> MessageFilter.filter(event) }
        logger.info("$name v$version Loaded")
    }

    private fun registerCommands() = CommandManager.run {
        registerCommand(LGZBotCommand)
    }
}