package ltd.guimc.lgzbot.command

import kotlinx.coroutines.launch
import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.PluginMain.logger
import ltd.guimc.lgzbot.files.ModuleStateConfig
import ltd.guimc.lgzbot.utils.MessageUtils.getPlainText
import ltd.guimc.lgzbot.utils.RequestUtils
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.MessageEvent

/*@Deprecated("This command is deprecated and will be removed in a future release.", level = DeprecationLevel.HIDDEN)*/
object ReviewCommand : SimpleCommand(
    owner = PluginMain,
    primaryName = "review",
    description = "处理Event ID用"
) {
    @Handler
    fun CommandSender.iiIiIIiII1i1I1i1I1i1II1i1I1i1I1(id: Long) = launch {
        try {
            if (bot == null) {
                throw IllegalAccessError("请勿在控制台运行")
            }

            if (ModuleStateConfig.invite) {
                sendMessage("该功能已禁用")
            }

            val realSource = user!!.id
            val realSubject = subject!!

            val event = RequestUtils.Group.find(id)
            sendMessage(
                "找到事件!\n" +
                    "发起人: ${event.invitorId}\n" +
                    "群聊: ${event.groupId}\n" +
                    "使用 同意/拒绝/取消"
            )
            GlobalEventChannel.filter { it is BotEvent && it.bot.id == bot!!.id }
                .subscribe<MessageEvent> {
                    if (it.subject == realSubject && it.sender.id == realSource) {
                        return@subscribe when (it.message.getPlainText()) {
                            "同意" -> {
                                event.accept()
                                RequestUtils.Group.remove(id)
                                sendMessage("已同意")
                                ListeningStatus.STOPPED
                            }

                            "拒绝" -> {
                                event.ignore()
                                RequestUtils.Group.remove(id)
                                sendMessage("已拒绝")
                                ListeningStatus.STOPPED
                            }

                            "取消" -> {
                                sendMessage("已取消")
                                ListeningStatus.STOPPED
                            }

                            else -> {
                                sendMessage("请发送 同意/拒绝/取消")
                                ListeningStatus.LISTENING
                            }
                        }
                    } else {
                        return@subscribe ListeningStatus.LISTENING
                    }
                }
        } catch (e: Throwable) {
            sendMessage("遇到问题: " + e.message)
            logger.warning(e)
        }
    }
}