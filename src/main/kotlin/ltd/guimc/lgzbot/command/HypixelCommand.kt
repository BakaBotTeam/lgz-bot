package ltd.guimc.lgzbot.command

import kotlinx.coroutines.launch
import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.PluginMain.hypixelApi
import ltd.guimc.lgzbot.utils.MojangAPIUtils
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.message.data.ForwardMessageBuilder
import net.mamoe.mirai.message.data.PlainText
import java.time.format.DateTimeFormatter

object HypixelCommand: SimpleCommand(
    owner = PluginMain,
    primaryName = "hypixel",
    secondaryNames = arrayOf("hyp"),
    description = "Hypixel Player Information"
) {
    @Handler
    fun CommandSender.onHandler(name: String) = ltd_guimc_command_hypixel(name)

    fun CommandSender.ltd_guimc_command_hypixel(name: String) = launch {
        try {
            requireNotNull(bot) { "Must have bot to use it" }
            val outputMessage = ForwardMessageBuilder(bot!!.asFriend)
            val uuid = MojangAPIUtils.getUUIDByName(name)

            val playerResponse = hypixelApi.getPlayerByUuid(uuid).get()
            requireNotNull(playerResponse)
            val player = playerResponse.player

            val onlineStatusResponse = hypixelApi.getStatus(uuid).get()
            requireNotNull(onlineStatusResponse)
            val onlineStatus = onlineStatusResponse.session

            outputMessage.add(
                bot!!, PlainText(
                    "Hypixel 玩家信息:\n" +
                        "玩家名称: ${if (player.hasRank()) "[${player.highestRank}]" else ""}${player.name}\n" +
                        "等级: ${player.networkLevel}\n" +
                        "Karma: ${player.karma}\n" +
                        "在线状态: ${if (onlineStatus.isOnline) "在线: ${onlineStatus.mode} - ${onlineStatus.map}" else "离线"}\n" +
                        "上次登入时间: ${player.lastLoginDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}\n" +
                        "上次登出时间: ${player.lastLogoutDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}\n" +
                        "上次登入所使用的 Minecraft 版本: ${player.lastKnownMinecraftVersion}"
                )
            )

            // 不会写 因为我不玩SkyBlock
            // val skyblockProfiles = hypixelApi.getSkyBlockProfiles(player.uuid).get().profiles
            // if (!skyblockProfiles.isEmpty) {
            //     skyblockProfiles.forEach {
            //         outputMessage.add(bot!!, PlainText("Skyblock 信息:\n" +
            //             "成员: \n${}"))
            //     }
            // }

            sendMessage(outputMessage.build())
        } catch (e: Throwable) {
            sendMessage("失败\n$e")
            e.printStackTrace()
        }
    }
}