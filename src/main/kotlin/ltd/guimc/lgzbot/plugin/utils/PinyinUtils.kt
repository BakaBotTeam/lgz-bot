package ltd.guimc.lgzbot.plugin.utils

import com.github.promeg.pinyinhelper.Pinyin

object PinyinUtils {
    fun convertToPinyin(text: String): String {
        return Pinyin.toPinyin(text, "")
    }
}