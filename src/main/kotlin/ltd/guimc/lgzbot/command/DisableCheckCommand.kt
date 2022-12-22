package ltd.guimc.lgzbot.command

import ltd.guimc.lgzbot.PluginMain
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.console.permission.PermissionService.Companion.cancel
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.contact.getMemberOrFail
import net.mamoe.mirai.contact.isOperator

object DisableCheckCommand : CompositeCommand(
    owner = PluginMain,
    primaryName = "disablecheck",
    description = "关闭检测"
) {
    @SubCommand("spam")
    @Description("开/关刷屏检查")
    suspend fun CommandSender.iI1I1i11ii1I1i1I1i() {
        if (getGroupOrNull() == null) {
            sendMessage("请在群聊中执行该命令")
        }

        val group = getGroupOrNull()!!
        val member = group.getMemberOrFail(user!!.id)

        if (member.permission.isOperator()) {
            if (group.permitteeId.hasPermission(PluginMain.disableSpamCheck)) {
                group.permitteeId.cancel(PluginMain.disableSpamCheck, false)
                sendMessage("已重新开启本群的刷屏检测")
            } else {
                group.permitteeId.permit(PluginMain.disableSpamCheck)
                sendMessage("已关闭本群的刷屏检测")
            }
        } else {
            sendMessage("只有管理才能使用这个指令的说..")
        }
    }

    @SubCommand("ad")
    @Description("开/关广告检测")
    suspend fun CommandSender.iiI11i1I1i1II1i1I1ii1I1i1I1() {
        if (getGroupOrNull() == null) {
            sendMessage("请在群聊中执行该命令")
        }

        val group = getGroupOrNull()!!
        val member = group.getMemberOrFail(user!!.id)

        if (member.permission.isOperator()) {
            if (group.permitteeId.hasPermission(PluginMain.disableADCheck)) {
                group.permitteeId.cancel(PluginMain.disableADCheck, false)
                sendMessage("已重新开启本群的广告检测")
            } else {
                group.permitteeId.permit(PluginMain.disableADCheck)
                sendMessage("已关闭本群的广告检测")
            }
        } else {
            sendMessage("只有管理才能使用这个指令的说..")
        }
    }
}