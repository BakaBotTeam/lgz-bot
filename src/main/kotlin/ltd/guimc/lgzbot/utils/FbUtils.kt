package ltd.guimc.lgzbot.utils

object FbUtils {
    fun getFbValue(): Array<String> {
        // Thanks for https://github.com/Ikaros-521/nonebot_plugin_random_stereotypes/blob/master/nonebot_plugin_random_stereotypes/data.py
        val fbFile = FbUtils::class.java.getResourceAsStream("/faqing.txt")
        val fbList = mutableListOf<String>()
        fbFile?.bufferedReader()?.use { reader ->
            reader.lines().forEach {
                // regexList.add(Regex(PinyinUtils.convertToPinyin(it)))
                fbList.add(it)
            }
        }

        return fbList.toTypedArray()
    }
}