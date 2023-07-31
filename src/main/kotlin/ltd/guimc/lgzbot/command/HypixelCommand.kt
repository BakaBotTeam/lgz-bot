/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.command

import kotlinx.coroutines.launch
import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.hypixel.HypixelApiUtils
import ltd.guimc.lgzbot.utils.MojangAPIUtils
import ltd.guimc.lgzbot.utils.TimeUtils
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.message.data.ForwardMessageBuilder
import net.mamoe.mirai.message.data.PlainText
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.math.roundToInt

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

            val playerInfo = HypixelApiUtils.request("/player?uuid=${uuid}").getJSONObject("player")
            val rank: String? = if (playerInfo.has("rank") && playerInfo.getString("rank") != "NORMAL") {
                HypixelApiUtils.resolveRank(playerInfo.getString("rank"))
            } else if (playerInfo.has("monthlyPackageRank") && playerInfo.getString("monthlyPackageRank") != "NONE") {
                HypixelApiUtils.resolveRank(playerInfo.getString("monthlyPackageRank"))
            } else if (playerInfo.has("newPackageRank") && playerInfo.getString("newPackageRank") != "NONE") {
                HypixelApiUtils.resolveRank(playerInfo.getString("newPackageRank"))
            } else {
                null
            }
            val level = (HypixelApiUtils.getExactLevel(playerInfo.getLong("networkExp"))*100).roundToInt() / 100
            val firstLogin = try {
                TimeUtils.convertDate(playerInfo.getLong("firstLogin"))
            } catch (e: Throwable) {
                e.printStackTrace()
                "无法获取"
            }
            val lastLogin = try {
                TimeUtils.convertDate(playerInfo.getLong("lastLogin"))
            } catch (e: Throwable) {
                e.printStackTrace()
                "无法获取"
            }
            val lastLogout = try {
                TimeUtils.convertDate(playerInfo.getLong("lastLogout"))
            } catch (e: Throwable) {
                e.printStackTrace()
                "无法获取"
            }

            val statusInfo = HypixelApiUtils.request("/status?uuid=${uuid}").getJSONObject("session")
            val stringOnlineStatus = if (!statusInfo.getBoolean("online")) {
                "离线"
            } else {
                "在线 -> ${ try { HypixelApiUtils.resolveGameType(statusInfo.getString("gameType")) } catch (_: Exception) { "Lobby?" } }"
            }

            outputMessage.add(bot!!, PlainText("Hypixel 玩家数据:\n" +
                "玩家名: ${ if (rank != null) "[$rank]" else "" }$name\n" +
                "等级: $level\n" +
                "Karma: ${ try { playerInfo.getInt("karma") } catch (_: Exception) { "查询失败" } }\n" +
                "玩家使用语言: ${ try { playerInfo.getString("userLanguage") } catch (_: Exception) { "查询失败" } }\n" +
                "首次登入: $firstLogin\n" +
                "上次登入: $lastLogin\n" +
                "上次登出: $lastLogout\n" +
                "当前状态: $stringOnlineStatus"))

            if (playerInfo.has("stats")) {
                val playerStats = playerInfo.getJSONObject("stats")
                try {
                    if (playerStats.has("Bedwars")) {
                        val bwStats = playerStats.getJSONObject("Bedwars")
                        outputMessage.add(
                            bot!!, PlainText(
                                "Bedwars 信息:\n" +
                                    "硬币: ${bwStats.getInt("coins")}\n" +
                                    "毁床数: ${bwStats.getInt("beds_broken_bedwars")}\n" +
                                    "总游戏数: ${bwStats.getInt("games_played_bedwars")}\n" +
                                    "胜利/失败: ${bwStats.getInt("wins_bedwars")}/${bwStats.getInt("losses_bedwars")}\n" +
                                    "Kill/Death: ${bwStats.getInt("kills_bedwars")}/${bwStats.getInt("deaths_bedwars")}\n" +
                                    "最终击杀数: ${bwStats.getInt("final_kills_bedwars")}"
                            )
                        )
                    }
                } catch (_: Exception) {}
                try {
                    if (playerStats.has("SkyWars")) {
                        val swStats = playerStats.getJSONObject("SkyWars")
                        outputMessage.add(bot!!, PlainText("Skywars 信息:\n" +
                            "硬币: ${swStats.getInt("coins")}\n" +
                            "灵魂数量: ${swStats.getInt("souls")}\n" +
                            "总游戏数: ${swStats.getInt("games_played_skywars")}\n" +
                            "胜利/失败: ${swStats.getInt("wins")}/${swStats.getInt("losses")}\n" +
                            "Kill/Death: ${swStats.getInt("kills")}/${swStats.getInt("deaths")}\n" +
                            "\n共计:\n" +
                            "放置了 ${swStats.getInt("blocks_placed")} 个方块, 打开了 ${swStats.getInt("chests_opened")} 个箱子"))
                    }
                } catch (_: Exception) {}
                try {
                    if (playerStats.has("Duels")) {
                        val duelStats = playerStats.getJSONObject("Duels")
                        outputMessage.add(bot!!, PlainText("Duels 信息:\n" +
                            "硬币: ${duelStats.getInt("coins")}\n" +
                            "总游戏数: ${duelStats.getInt("rounds_played")}\n" +
                            "胜利/失败: ${duelStats.getInt("wins")}/${duelStats.getInt("losses")}\n" +
                            "Kill/Death: ${duelStats.getInt("kills")}/${duelStats.getInt("deaths")}\n" +
                            "近战命中: ${duelStats.getInt("melee_hits")}/${duelStats.getInt("melee_swings")}\n" +
                            "弓箭命中: ${duelStats.getInt("bow_hits")}/${duelStats.getInt("bow_shots")}\n" +
                            "\n共计:\n" +
                            "造成了 ${duelStats.getInt("damage_dealt")} 伤害, 恢复了 ${duelStats.getInt("health_regenerated")} 血量"))
                    }
                } catch (_: Exception) {}
            }

            outputMessage.add(bot!!, PlainText("本数据仅供参考"))
            sendMessage(outputMessage.build())
        } catch (e: Throwable) {
            sendMessage("失败 提示: 没有进入过游戏的玩家会查询错误\n$e")
            e.printStackTrace()
        }
    }
}