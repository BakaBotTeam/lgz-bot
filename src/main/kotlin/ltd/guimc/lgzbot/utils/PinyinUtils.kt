package ltd.guimc.lgzbot.utils

import com.github.promeg.pinyinhelper.Pinyin

object PinyinUtils {
    // 将汉字转化为拼音
    fun convertToPinyin(text: String): String {
        return Pinyin.toPinyin(text, "")
    }
}