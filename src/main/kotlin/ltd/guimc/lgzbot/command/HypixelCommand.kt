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
import ltd.guimc.lgzbot.utils.CooldownUtils
import ltd.guimc.lgzbot.utils.hypixel.HypixelApiUtils
import ltd.guimc.lgzbot.utils.hypixel.ExpCalculator
import ltd.guimc.lgzbot.utils.MojangAPIUtils
import ltd.guimc.lgzbot.utils.TimeUtils
import ltd.guimc.lgzbot.utils.timer.MSTimer
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.ForwardMessageBuilder
import net.mamoe.mirai.message.data.PlainText
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.math.roundToInt

object HypixelCommand: SimpleCommand(
    owner = PluginMain,
    primaryName = "hypixel",
    secondaryNames = arrayOf("hyp"),
    description = "Hypixel Player Information"
) {
    val cooldown = CooldownUtils(30000)

    @Handler
    fun CommandSender.onHandler(name: String) = ltd_guimc_command_hypixel(name)

    fun CommandSender.ltd_guimc_command_hypixel(name: String) = launch {
        try {
            requireNotNull(bot) { "Must have bot to use it" }
            requireNotNull(user) { "Must have user to use it" }

            if (!cooldown.isTimePassed(user!!)) {
                if (cooldown.shouldSendCooldownNotice(user!!)) sendMessage("你可以在 ${cooldown.getLeftTime(user!!) / 1000} 秒后继续使用该指令")
                return@launch
            }

            val uuid = MojangAPIUtils.getUUIDByName(name)

            cooldown.flag(user!!)

            val outputMessage = ForwardMessageBuilder(bot!!.asFriend)

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
            val level: Double = try {
                (ExpCalculator.getExactLevel(playerInfo.getLong("networkExp")) * 100).roundToInt().toDouble() / 100.0
            } catch (_: Exception) {
                1.0
            }
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
                "在线 -> ${
                    try {
                        HypixelApiUtils.resolveGameType(statusInfo.getString("gameType"))
                    } catch (_: Exception) {
                        "Lobby?"
                    }
                }"
            }

            outputMessage.add(
                bot!!, PlainText(
                    "Hypixel 玩家数据:\n" +
                        "玩家名: ${if (rank != null) "[$rank]" else ""}$name\n" +
                        "等级: $level\n" +
                        "Karma: ${playerInfo.getIntOrNull("karma")}\n" +
                        "玩家使用语言: ${
                            try {
                                playerInfo.getString("userLanguage")
                            } catch (_: Exception) {
                                "查询失败"
                            }
                        }\n" +
                        "首次登入: $firstLogin\n" +
                        "上次登入: $lastLogin\n" +
                        "上次登出: $lastLogout\n" +
                        "最近常玩: ${playerInfo.getStringOrNull("mostRecentGameType")}" +
                        "当前状态: $stringOnlineStatus"
                )
            )

            if (playerInfo.has("stats")) {
                val playerStats = playerInfo.getJSONObject("stats")

                /*try {
                    if (playerInfo.has("giftingMeta")) {
                        val giftMeta = playerInfo.getJSONObject("giftingMeta")
                        outputMessage.add(
                            bot!!,
                            PlainText("这个玩家一共送出了 ${giftMeta.getInt("giftsGiven")} 份礼物!")
                        )
                    }
                } catch (_: Throwable) {}*/

                // Bedwars
                try {
                    if (playerStats.has("Bedwars")) {
                        val bwStats = playerStats.getJSONObject("Bedwars")
                        outputMessage.add(
                            bot!!, PlainText(
                                "Bedwars 信息:\n" +
                                    "等级: ${
                                        try {
                                            (ExpCalculator.getBedWarsLevel(bwStats.getIntOrNull("Experience")) * 100).roundToInt()
                                                .toDouble() / 100.0
                                        } catch (_: Exception) {
                                            1.0
                                        }
                                    }\n" +
                                    "硬币: ${bwStats.getIntOrNull("coins")}\n" +
                                    "毁床数: ${bwStats.getIntOrNull("beds_broken_bedwars")}\n" +
                                    "总游戏数: ${bwStats.getIntOrNull("games_played_bedwars")}\n" +
                                    "胜利/失败: ${bwStats.getIntOrNull("wins_bedwars")}/${bwStats.getIntOrNull("losses_bedwars")} " +
                                    "WLR: ${
                                        calculatorR(
                                            bwStats.getIntOrNull("wins_bedwars"),
                                            bwStats.getIntOrNull("losses_bedwars")
                                        )
                                    }\n" +
                                    "击杀/死亡: ${bwStats.getIntOrNull("kills_bedwars") + bwStats.getIntOrNull("final_kills_bedwars")}/${
                                        bwStats.getIntOrNull(
                                            "deaths_bedwars"
                                        )
                                    } " +
                                    "KDR: ${
                                        calculatorR(
                                            bwStats.getIntOrNull("kills_bedwars") + bwStats.getIntOrNull("final_kills_bedwars"),
                                            bwStats.getIntOrNull("deaths_bedwars")
                                        )
                                    }\n" +
                                    "最终击杀数: ${bwStats.getIntOrNull("final_kills_bedwars")}"
                            )
                        )
                    }
                } catch (_: Exception) {}

                // Skywars
                try {
                    if (playerStats.has("SkyWars")) {
                        val swStats = playerStats.getJSONObject("SkyWars")
                        outputMessage.add(
                            bot!!, PlainText(
                                "Skywars 信息:\n" +
                                    "等级: ${
                                        try {
                                            (ExpCalculator.getSkyWarsLevel(swStats.getIntOrNull("experience")) * 100).roundToInt()
                                                .toDouble() / 100.0
                                        } catch (_: Exception) {
                                            1.0
                                        }
                                    }\n" +
                                    "硬币: ${swStats.getIntOrNull("coins")}\n" +
                                    "灵魂数量: ${swStats.getIntOrNull("souls")}\n" +
                                    "总游戏数: ${swStats.getIntOrNull("games_played_skywars")}\n" +
                                    "胜利/失败: ${swStats.getIntOrNull("wins")}/${swStats.getIntOrNull("losses")} " +
                                    "WLR: ${
                                        calculatorR(
                                            swStats.getIntOrNull("wins"),
                                            swStats.getIntOrNull("losses")
                                        )
                                    }\n" +
                                    "击杀/助攻/死亡: ${swStats.getIntOrNull("kills")}/${swStats.getIntOrNull("assists")}/${swStats.getIntOrNull("deaths")} " +
                                    "KDR: ${
                                        calculatorR(
                                            swStats.getIntOrNull("kills"),
                                            swStats.getIntOrNull("deaths")
                                        )
                                    }\n" +
                                    "\n共计:\n" +
                                    "共有 ${swStats.getIntOrNull("heads")} 个 Heads, 放置了 ${swStats.getIntOrNull("blocks_placed")} 个方块, 打开了 ${
                                        swStats.getIntOrNull(
                                            "chests_opened"
                                        )
                                    } 个箱子"
                            )
                        )
                    }
                } catch (_: Exception) {}

                // Duels
                try {
                    if (playerStats.has("Duels")) {
                        val duelStats = playerStats.getJSONObject("Duels")
                        outputMessage.add(
                            bot!!, PlainText(
                                "Duels 信息:\n" +
                                    "硬币: ${duelStats.getInt("coins")}\n" +
                                    "总游戏数: ${duelStats.getIntOrNull("rounds_played")}\n" +
                                    "胜利/失败: ${duelStats.getIntOrNull("wins")}/${duelStats.getIntOrNull("losses")} " +
                                    "WLR: ${
                                        calculatorR(
                                            duelStats.getIntOrNull("wins"),
                                            duelStats.getIntOrNull("losses")
                                        )
                                    }\n" +
                                    "击杀/死亡: ${duelStats.getIntOrNull("kills")}/${duelStats.getIntOrNull("deaths")} " +
                                    "KDR: ${
                                        calculatorR(
                                            duelStats.getIntOrNull("kills"),
                                            duelStats.getIntOrNull("deaths")
                                        )
                                    }\n" +
                                    "近战命中: ${
                                        calculatorR(
                                            duelStats.getIntOrNull("melee_hits"),
                                            duelStats.getIntOrNull("melee_swings")
                                        )
                                    }\n" +
                                    "弓箭命中: ${
                                        calculatorR(
                                            duelStats.getIntOrNull("bow_hits"),
                                            duelStats.getIntOrNull("bow_shots")
                                        )
                                    }\n" +
                                    "\n共计:\n" +
                                    "造成了 ${duelStats.getIntOrNull("damage_dealt")} 伤害, 恢复了 ${
                                        duelStats.getIntOrNull(
                                            "health_regenerated"
                                        )
                                    } 血量"
                            )
                        )
                    }
                } catch (_: Exception) {}

                // Mega Walls
                try {
                    if (playerStats.has("Walls3")) {
                        val mwStats = playerStats.getJSONObject("Walls3")
                        outputMessage.add(
                            bot!!, PlainText(
                                "Mega Walls 信息:\n" +
                                    "硬币: ${mwStats.getIntOrNull("coins")}\n" +
                                    "胜利/失败: ${mwStats.getIntOrNull("wins")}/${mwStats.getIntOrNull("losses")} " +
                                    "WLR: ${
                                        calculatorR(
                                            mwStats.getIntOrNull("wins"),
                                            mwStats.getIntOrNull("losses")
                                        )
                                    }\n" +
                                    "击杀/助攻/死亡: ${mwStats.getIntOrNull("kills") + mwStats.getIntOrNull("final_kills")}/${mwStats.getIntOrNull("assists")}/${
                                        mwStats.getIntOrNull(
                                            "deaths"
                                        ) + mwStats.getIntOrNull("final_deaths")
                                    } " +
                                    "KDR: ${
                                        calculatorR(
                                            mwStats.getIntOrNull("kills") + mwStats.getIntOrNull("final_kills"),
                                            mwStats.getIntOrNull("deaths") + mwStats.getIntOrNull("final_deaths")
                                        )
                                    }\n" +
                                    "Final Kill/Death: ${mwStats.getIntOrNull("final_kills")}/${mwStats.getIntOrNull("final_deaths")}\n" +
                                    "\n共计:\n" +
                                    "造成了 ${mwStats.getIntOrNull("damage_dealt")} 伤害, 共有 ${
                                        mwStats.getJSONArray("packages").length()
                                    } 个 Packages"
                            )
                        )
                    }
                } catch (_: Exception) {}

                // UHC
                try {
                    if (playerStats.has("UHC")) {
                        val uhcStats = playerStats.getJSONObject("UHC")
                        outputMessage.add(
                            bot!!, PlainText(
                                "UHC 信息:\n" +
                                    "硬币: ${uhcStats.getIntOrNull("coins")}\n" +
                                    "已选择的职业 ${uhcStats.getStringOrNull("equippedKit")}\n" +
                                    "胜利/失败: ${uhcStats.getIntOrNull("wins")}/${uhcStats.getIntOrNull("deaths")} " +
                                    "WLR: ${
                                        calculatorR(
                                            uhcStats.getIntOrNull("wins"),
                                            uhcStats.getIntOrNull("deaths")
                                        )
                                    }\n" +
                                    "击杀/死亡: ${uhcStats.getIntOrNull("kills")}/${uhcStats.getIntOrNull("deaths")} " +
                                    "KDR: ${
                                        calculatorR(
                                            uhcStats.getIntOrNull("kills"),
                                            uhcStats.getIntOrNull("deaths")
                                        )
                                    }\n" +
                                    "\n共计:\n" +
                                    "共有 ${uhcStats.getJSONArray("packages").length()} 个合成配方"
                            )
                        )
                    }
                } catch (_: Exception) {}

                // Arcade Games
                try {
                    if (playerStats.has("Arcade")) {
                        val arcadeStats = playerStats.getJSONObject("Arcade")
                        outputMessage.add(bot!!, PlainText(
                            "街机游戏 信息:\n" +
                                "硬币: ${arcadeStats.getIntOrNull("coins")}\n\n" +
                                "以下为街机游戏:"
                        ))
                        outputMessage.add(bot!!, PlainText(
                            "Mini Walls:\n" +
                                "胜利: ${arcadeStats.getIntOrNull("wins_mini_walls")}\n" +
                                "击杀/死亡: ${arcadeStats.getIntOrNull("kills_mini_walls")+arcadeStats.getIntOrNull("final_kills_mini_walls")}/${arcadeStats.getIntOrNull("deaths_mini_walls")} " +
                                "KDR: ${calculatorR(arcadeStats.getIntOrNull("kills_mini_walls")+arcadeStats.getIntOrNull("final_kills_mini_walls"), arcadeStats.getIntOrNull("deaths_mini_walls"))}\n" +
                                "最终击杀: ${arcadeStats.getIntOrNull("final_kills_mini_walls")}\n" +
                                "凋零击杀数: ${arcadeStats.getIntOrNull("wither_kills_mini_walls")}\n" +
                                "弓箭命中率: ${calculatorR(arcadeStats.getIntOrNull("arrows_hit_mini_walls"), arcadeStats.getIntOrNull("arrows_shot_mini_walls"))}"
                        ))
                        outputMessage.add(bot!!, PlainText(
                            "派对游戏 胜场: ${arcadeStats.getIntOrNull("wins_party")}"
                        ))
                        outputMessage.add(bot!!, PlainText(
                            "僵尸末日: \n" +
                                "最高坚持了 ${arcadeStats.getIntOrNull("best_round_zombies")} 轮\n" +
                                "死亡次数: ${arcadeStats.getIntOrNull("deaths_zombies")}\n" +
                                "命中头部率: ${calculatorR(arcadeStats.getIntOrNull("headshots_zombies"), arcadeStats.getIntOrNull("bullets_shot_zombies"))}\n" +
                                "打开门数量: ${arcadeStats.getIntOrNull("doors_opened_zombies")}\n" +
                                "救起其他玩家次数: ${arcadeStats.getIntOrNull("players_revived_zombies")}"
                        ))
                        outputMessage.add(bot!!, PlainText("以上为街机游戏"))
                    }
                } catch (_: Exception) {}

                // The Pit
                try {
                    if (playerStats.has("Pit")) {
                        val pitProfile = playerStats.getJSONObject("Pit")
                        pitProfile.keys().forEach {
                            val pitStats = pitProfile.getJSONObject(it)
                            if (it != "profile") {
                                outputMessage.add(bot!!, PlainText(
                                    "天坑 ($it): \n" +
                                        "进入次数: ${pitStats.getIntOrNull("joins")} 跳入天坑次数: ${pitStats.getIntOrNull("jumped_into_pit")}\n" +
                                        "击杀/助攻/死亡: ${pitStats.getIntOrNull("kills")}/${pitStats.getIntOrNull("assists")}/${pitStats.getIntOrNull("deaths")} " +
                                        "KDR: ${calculatorR(pitStats.getIntOrNull("kills"), pitStats.getIntOrNull("deaths"))}\n" +
                                        "近战命中率: ${calculatorR(pitStats.getIntOrNull("sword_hits"), pitStats.getIntOrNull("left_clicks"))}\n" +
                                        "最高连杀: ${pitStats.getIntOrNull("max_streak")}\n" +
                                        "总游玩时长: ${pitStats.getIntOrNull("playtime_minutes")} 分钟"
                                ))
                            }
                        }
                    }
                } catch (_: Exception) {}
            }

            outputMessage.add(bot!!, PlainText("Skyblock信息: https://sky.shiiyu.moe/stats/$name"))
            outputMessage.add(bot!!, PlainText("本数据仅供参考"))
            sendMessage(outputMessage.build())
        } catch (e: FileNotFoundException) {
            sendMessage("你好像在尝试查询一个不存在的玩家?")
            e.printStackTrace()
        } catch (e: IOException) {
            if (e.message != null && e.message!!.indexOf("HTTP response code: 403 for URL: https://api.hypixel.net/") != -1) {
                sendMessage("Hypixel APIKey失效了...")
            } else {
                sendMessage("请求过程中出现了一点问题!\n$e")
                e.printStackTrace()
            }
        } catch (e: Throwable) {
            sendMessage("请求过程中出现了一点问题!\n$e")
            e.printStackTrace()
        }
    }

    private fun calculatorR(num1: Int, num2: Int): Double {
        val result = try {
            ((num1.toDouble() / num2.toDouble()) * 100.0).roundToInt().toDouble() / 100.0
        } catch (_: Throwable) {
            num1.toDouble()
        }
        
        return result
    }

    private fun JSONObject.getIntOrNull(key: String): Int {
        return try {
            this.getInt(key)
        } catch (_: Exception) {
            0
        }
    }

    private fun JSONObject.getStringOrNull(key: String): String {
        return try {
            this.getString(key)
        } catch (_: Throwable) {
            "Null"
        }
    }
}