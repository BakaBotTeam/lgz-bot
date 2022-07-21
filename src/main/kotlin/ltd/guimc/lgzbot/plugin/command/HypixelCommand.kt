package ltd.guimc.lgzbot.plugin.command

import ltd.guimc.lgzbot.plugin.PluginMain
import ltd.guimc.lgzbot.plugin.files.Config
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand

object HypixelCommand: CompositeCommand(
    owner = PluginMain,
    primaryName = "hypixel",
    description = "Hypixel实用工具"
) {
    @SubCommand("find")
    @Description("查找玩家信息")
    suspend fun CommandSender.guimc_plugin_hypixel_find(name: String) {
        if (Config.hypixelApikey == null) {
            sendMessage("请先设置Hypixel API Key")
            return
        }
        sendMessage("还没完工呢~ 嘤~")
    }

    @SubCommand("set")
    @Description("设置Hypixel API Key")
    suspend fun CommandSender.guimc_plugin_hypixel_set(key: String) {
        Config.hypixelApikey = key
        sendMessage("Hypixel API Key已设置")
    }
}