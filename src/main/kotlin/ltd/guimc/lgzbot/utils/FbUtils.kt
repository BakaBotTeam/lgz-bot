package ltd.guimc.lgzbot.utils

object FbUtils {
    fun getFbValue(): Array<String> {
        // Thanks for
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