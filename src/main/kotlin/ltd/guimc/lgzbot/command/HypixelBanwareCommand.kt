package ltd.guimc.lgzbot.command

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.PluginMain.isRunning
import ltd.guimc.lgzbot.PluginMain.logger
import ltd.guimc.lgzbot.hypixel.HypixelApiUtils
import ltd.guimc.lgzbot.utils.timer.MSTimer
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.contact.Group
import kotlin.concurrent.thread

object HypixelBanwareCommand: SimpleCommand(
    owner = PluginMain,
    primaryName = "banware",
    description = "Hypixel Ban Listener"
) {
    var listenerThread: Thread? = null
    var listenerGroup: MutableList<Group> = mutableListOf()

    @Handler
    fun CommandSender.onHandler() = ltd_guimc_command_banware()

    fun CommandSender.ltd_guimc_command_banware() = launch {
        requireNotNull(subject)
        require(subject is Group)
        val group = subject!! as Group

        if (listenerGroup.indexOf(group) == -1) {
            if (listenerThread == null) {
                listenerThread = thread(isDaemon = true) {
                    launch {
                        val msTimer = MSTimer()
                        val jsonObject2 = HypixelApiUtils.request("/punishmentstats")
                        var lastWatchdog = jsonObject2.getInt("watchdog_total")
                        var lastStaff = jsonObject2.getInt("staff_total")

                        while (isRunning) {
                            if (msTimer.isTimePressed(5000)) {
                                try {
                                    val jsonObject = HypixelApiUtils.request("/punishmentstats")

                                    val nowWatchdog = jsonObject.getInt("watchdog_total")
                                    val nowStaff = jsonObject.getInt("staff_total")

                                    if (nowWatchdog - lastWatchdog != 0) {
                                        listenerGroup.forEach {
                                            try {
                                                it.sendMessage("[Hypixel Ban] Watchdog last 5s: ${nowWatchdog - lastWatchdog}")
                                            } catch (e: Throwable) { logger.warning(e) }
                                        }
                                        lastWatchdog = nowWatchdog
                                    }

                                    if (nowStaff - lastStaff != 0) {
                                        listenerGroup.forEach {
                                            try {
                                                it.sendMessage("[Hypixel Ban] Staff last 5s: ${nowStaff - lastStaff}")
                                            } catch (e: Throwable) { logger.warning(e) }
                                        }
                                        lastStaff = nowStaff
                                    }

                                    msTimer.reset()
                                } catch (e: Throwable) { logger.warning(e) }
                            }
                        }
                    }
                }
            }

            listenerGroup.add(subject as Group)
            sendMessage("已将本群添加到 Hypixel 封禁监听器")
        } else {
            listenerGroup.remove(subject as Group)
            sendMessage("已将本群从 Hypixel 封禁监听器移除")
        }
    }
}