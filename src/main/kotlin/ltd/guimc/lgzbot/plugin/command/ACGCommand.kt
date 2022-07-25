package ltd.guimc.lgzbot.plugin.command

import ltd.guimc.lgzbot.plugin.PluginMain
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.utils.ExternalResource
import org.apache.http.impl.client.HttpClients

object ACGCommand: SimpleCommand (
    owner = PluginMain,
    primaryName = "acg",
    description = "啊哈哈哈哈 acg图片来咯"
) {
    suspend fun CommandSender.ltd_guimc_command_acg() {
        if (subject == null) {
            throw Exception("subject is null")
        }
        val httpclients = HttpClients.createDefault()
    }
}