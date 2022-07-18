package ltd.guimc.lgzbot.plugin.files

import ltd.guimc.lgzbot.plugin.utils.RegexUtils.getDefaultRegex
import net.mamoe.mirai.console.data.AutoSavePluginData

object Data : AutoSavePluginData("regex") {
    var regex : Array<Regex> = getDefaultRegex()
}