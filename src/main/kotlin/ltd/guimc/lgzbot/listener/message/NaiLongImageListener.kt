package ltd.guimc.lgzbot.listener.message

import ltd.guimc.dlm.DLModel
import ltd.guimc.dlm.ImageProcessor
import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.utils.HttpUtils
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.event.ListenerHost
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import net.mamoe.mirai.message.data.QuoteReply

object NaiLongImageListener : ListenerHost {
    suspend fun GroupMessageEvent.onEvent() {
        if (this.group.permitteeId.hasPermission(PluginMain.checkNailong)) {
            for (msg in this.message) {
                if (msg !is Image) continue
                try {
                    val imageUrl = msg.queryUrl()
                    val imageRaw = HttpUtils.getBytesResponse(imageUrl) ?: continue
                    val mat = ImageProcessor.processImage(imageRaw)
                    val scannerResult = DLModel.checkImage(mat)
                    if (scannerResult) {
                        this.cancel()
                        this.group.sendMessage(QuoteReply(this.source) + "啾咪啊！这里有人发奶龙图片啊！快来人管管啊！")
                        if (this.group.botPermission.level > this.sender.permission.level) {
                            this.message.recall()
                        }
                        break
                    }
                } catch (e: Exception) {
                    PluginMain.logger.warning("Failed to recognize image")
                }
            }
        }
    }
}