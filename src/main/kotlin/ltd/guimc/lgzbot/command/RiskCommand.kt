/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.command

import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.listener.message.MessageFilter.riskList
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