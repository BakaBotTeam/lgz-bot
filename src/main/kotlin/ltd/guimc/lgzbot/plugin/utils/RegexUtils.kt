package ltd.guimc.lgzbot.plugin.utils

import ltd.guimc.lgzbot.plugin.PluginMain.logger
import ltd.guimc.lgzbot.plugin.utils.AsciiUtil.sbc2dbcCase

object RegexUtils {
    // 获取正则表达式列表
    fun getDefaultRegex(): Array<Regex> {
        // Read from resources regex.txt
        val regexFile = RegexUtils::class.java.getResourceAsStream("/regex.txt")
        val regexList = mutableListOf<Regex>()
        regexFile?.bufferedReader()?.use { reader ->
            reader.lines().forEach {
                // regexList.add(Regex(PinyinUtils.convertToPinyin(it)))
                regexList.add(Regex(it))
            }
        }

        return regexList.toTypedArray()
    }

    // 匹配正则表达式列表 返回是否匹配
    fun matchRegex(regexList: Array<Regex>, message: String): Boolean {
        for (regex in regexList) {
            // val unPeekText = PinyinUtils.convertToPinyin(sbc2dbcCase(message)).lowercase()
            val unPeekText = sbc2dbcCase(message).lowercase()
                .replace(" ", "")
                .replace(",", "")
                .replace(".", "")
            if (regex.containsMatchIn(unPeekText)) {
                logger.info("匹配成功 ${regex.find(unPeekText)?.value}")
                return true
            }
        }
        return false
    }

    // 匹配正则表达式 返回匹配结果
    fun matchRegex(regex: String, message: String): String? {
        return Regex(regex).find(message)?.groups?.get(0)?.value
    }
}