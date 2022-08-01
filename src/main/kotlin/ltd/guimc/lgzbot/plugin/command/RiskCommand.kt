package ltd.guimc.lgzbot.plugin.command

import ltd.guimc.lgzbot.plugin.MessageFilter.riskList
import ltd.guimc.lgzbot.plugin.PluginMain
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.contact.Member

object RiskCommand : CompositeCommand(
    owner = PluginMain,
    primaryName = "risk",
    description = "风控"
) {

    @SubCommand("list")
    @Description("获取所有被风控/禁言的名单")
    suspend fun CommandSender.list() {
        sendMessage(riskList.toString())
    }

    @SubCommand("remove")
    @Description("将一个人从风控列表中移除")
    suspend fun CommandSender.remove(target: Member) {
        riskList.remove(target)
        sendMessage("已将$target 移出风控列表")
    }

    @SubCommand("add")
    @Description("将一个人添加到风控列表")
    suspend fun CommandSender.add(target: Member) {
        riskList.add(target)
        sendMessage("已将$target 加入风控列表")
    }
}