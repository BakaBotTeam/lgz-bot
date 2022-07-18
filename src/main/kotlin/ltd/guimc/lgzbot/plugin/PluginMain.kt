package ltd.guimc.lgzbot.plugin

import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.plugin.version
import ltd.guimc.lgzbot.plugin.command.*

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        "ltd.guimc.lgzbot.plugin.main",
        "0.1.1",
        "LgzBot",
    ){
        author("Guimc")
    }
) {
    override fun onEnable() {
        registerCommands()
        logger.info("${name} v${version} Loaded")
    }

    private fun registerCommands() = CommandManager.run {
        registerCommands(LGZBotCommand)
    }
}