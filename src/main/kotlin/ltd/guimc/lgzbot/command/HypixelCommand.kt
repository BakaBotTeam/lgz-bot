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
import org.json.JSONObject
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
            val level: Double = try {(HypixelApiUtils.getExactLevel(playerInfo.getLong("networkExp"))*100).roundToInt().toDouble() / 100.0} catch (_: Exception) { 1.0 }
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

            outputMessage.add(bot!!, PlainText("Hypixel 玩家数据: (-1 为获取失败)\n" +
                "玩家名: ${ if (rank != null) "[$rank]" else "" }$name\n" +
                "等级: $level\n" +
                "Karma: ${playerInfo.getIntOrNull("karma")}\n" +
                "玩家使用语言: ${ try { playerInfo.getString("userLanguage") } catch (_: Exception) { "查询失败" } }\n" +
                "首次登入: $firstLogin\n" +
                "上次登入: $lastLogin\n" +
                "上次登出: $lastLogout\n" +
                "当前状态: $stringOnlineStatus"))

            if (playerInfo.has("stats")) {
                val playerStats = playerInfo.getJSONObject("stats")
                try {
                    if (playerStats.has("giftingMeta")) {
                        val giftMeta = playerStats.getJSONObject("giftingMeta")
                        outputMessage.add(bot!!, PlainText("这个玩家一共送出了 ${giftMeta.getInt("giftsGiven")} 份礼物!"))
                    }
                } catch (_: Throwable) {}
                try {
                    if (playerStats.has("Bedwars")) {
                        val bwStats = playerStats.getJSONObject("Bedwars")
                        outputMessage.add(
                            bot!!, PlainText(
                                "Bedwars 信息: (-1 为获取失败)\n" +
                                    "硬币: ${bwStats.getIntOrNull("coins")}\n" +
                                    "毁床数: ${bwStats.getIntOrNull("beds_broken_bedwars")}\n" +
                                    "总游戏数: ${bwStats.getIntOrNull("games_played_bedwars")}\n" +
                                    "胜利/失败: ${bwStats.getIntOrNull("wins_bedwars")}/${bwStats.getIntOrNull("losses_bedwars")}\n" +
                                    "Kill/Death: ${bwStats.getIntOrNull("kills_bedwars")}/${bwStats.getIntOrNull("deaths_bedwars")}\n" +
                                    "最终击杀数: ${bwStats.getIntOrNull("final_kills_bedwars")}"
                            )
                        )
                    }
                } catch (_: Exception) {}
                try {
                    if (playerStats.has("SkyWars")) {
                        val swStats = playerStats.getJSONObject("SkyWars")
                        outputMessage.add(bot!!, PlainText("Skywars 信息: (-1 为获取失败)\n" +
                            "硬币: ${swStats.getIntOrNull("coins")}\n" +
                            "灵魂数量: ${swStats.getIntOrNull("souls")}\n" +
                            "总游戏数: ${swStats.getIntOrNull("games_played_skywars")}\n" +
                            "胜利/失败: ${swStats.getIntOrNull("wins")}/${swStats.getIntOrNull("losses")}\n" +
                            "Kill/Death: ${swStats.getIntOrNull("kills")}/${swStats.getIntOrNull("deaths")}\n" +
                            "\n共计:\n" +
                            "放置了 ${swStats.getIntOrNull("blocks_placed")} 个方块, 打开了 ${swStats.getIntOrNull("chests_opened")} 个箱子"))
                    }
                } catch (_: Exception) {}
                try {
                    if (playerStats.has("Duels")) {
                        val duelStats = playerStats.getJSONObject("Duels")
                        outputMessage.add(bot!!, PlainText("Duels 信息: (-1 为获取失败)\n" +
                            "硬币: ${duelStats.getInt("coins")}\n" +
                            "总游戏数: ${duelStats.getIntOrNull("time_played")}\n" +
                            "胜利/失败: ${duelStats.getIntOrNull("wins")}/${duelStats.getIntOrNull("losses")}" +
                            "WLR: ${calculatorR(duelStats.getIntOrNull("wins"), duelStats.getIntOrNull("losses"))}\n" +
                            "Kill/Death: ${duelStats.getIntOrNull("kills")}/${duelStats.getIntOrNull("deaths")} " +
                            "KDR: ${calculatorR(duelStats.getIntOrNull("kills"), duelStats.getIntOrNull("deaths"))}\n" +
                            "近战命中: ${calculatorR(duelStats.getIntOrNull("melee_hits"), duelStats.getIntOrNull("melee_swings"))}\n" +
                            "弓箭命中: ${duelStats.getIntOrNull("bow_hits")}/${duelStats.getIntOrNull("bow_shots")}\n" +
                            "\n共计:\n" +
                            "造成了 ${duelStats.getIntOrNull("damage_dealt")} 伤害, 恢复了 ${duelStats.getIntOrNull("health_regenerated")} 血量"))
                    }
                } catch (_: Exception) {}
                try {
                    if (playerStats.has("Walls3")) {
                        val mwStats = playerStats.getJSONObject("Walls3")
                        outputMessage.add(bot!!, PlainText("Mega Walls 信息: (-1 为获取失败)\n" +
                            "硬币: ${mwStats.getIntOrNull("coins")}\n" +
                            "总游戏数: ${mwStats.getIntOrNull("wins")+mwStats.getIntOrNull("losses")}\n" +
                            "胜利/失败: ${mwStats.getIntOrNull("wins")}/${mwStats.getIntOrNull("losses")} " +
                            "WLR: ${calculatorR(mwStats.getIntOrNull("wins"), mwStats.getIntOrNull("losses"))}\n" +
                            "Kill/Death: ${mwStats.getIntOrNull("kills")}/${mwStats.getIntOrNull("deaths")} " +
                            "KDR: ${calculatorR(mwStats.getIntOrNull("kills"), mwStats.getIntOrNull("deaths"))}\n" +
                            "Final Kill/Death: ${mwStats.getIntOrNull("final_kills")}/${mwStats.getIntOrNull("final_deaths")}\n" +
                            "\n共计:\n" +
                            "造成了 ${mwStats.getIntOrNull("damage_dealt")} 伤害, 共有 ${mwStats.getJSONArray("packages").length()} 个 Packages"))
                    }
                } catch (_: Exception) {}
                try {
                    if (playerStats.has("UHC")) {
                        val uhcStats = playerStats.getJSONObject("UHC")
                        outputMessage.add(bot!!, PlainText("UHC 信息: (-1 为获取失败)\n" +
                            "硬币: ${uhcStats.getIntOrNull("coins")}\n" +
                            "胜利/失败: ${uhcStats.getIntOrNull("wins")}/${uhcStats.getIntOrNull("deaths")} " +
                            "WLR: ${calculatorR(uhcStats.getIntOrNull("wins"), uhcStats.getIntOrNull("deaths"))}\n" +
                            "Kill/Death: ${uhcStats.getIntOrNull("kills")}/${uhcStats.getIntOrNull("deaths")} " +
                            "KDR: ${calculatorR(uhcStats.getIntOrNull("kills"), uhcStats.getIntOrNull("deaths"))}\n" +
                            "\n共计:\n" +
                            "共有 ${uhcStats.getJSONArray("packages").length()} 个 Packages"))
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

    private fun calculatorR(num1: Int, num2: Int): Double {
        val result = try {
            ((num1.toDouble() / num2.toDouble()) * 100.0).roundToInt().toDouble() / 100.0
        } catch (_: Throwable) {
            -1.0
        }
        
        if (num1 == -1 || num2 == -1) 
            return -1.0
        
        return result
    }

    private fun JSONObject.getIntOrNull(key: String): Int {
        return try {
            this.getInt(key)
        } catch (_: Exception) {
            -1
        }
    }
}