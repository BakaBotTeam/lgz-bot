package ltd.guimc.lgzbot.plugin.command

import ltd.guimc.lgzbot.plugin.PluginMain
import ltd.guimc.lgzbot.plugin.utils.CloudBlackListUtils
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand

object CloudBlackListCommand: SimpleCommand(
    owner = PluginMain,
    primaryName = "check",
    description = "检查某人是否在云黑名单中"
) {
    @Handler
    suspend fun CommandSender.onHandle(target: Long) {
        if (bot == null || subject == null) {
            sendMessage("未登入")
            return
        }
        when (CloudBlackListUtils.check(target)) {
            0 -> sendMessage("该用户不在云黑名单中")
            1 -> {
                sendMessage("该用户在云黑名单中 (Type: 1), 正在获取其详细信息...")
                sendMessage(CloudBlackListUtils.baoziGetReason(target, bot!!, subject!!))
            }
            else -> sendMessage("Err: 获得了一个意料之外的结果")
        }
    }
}