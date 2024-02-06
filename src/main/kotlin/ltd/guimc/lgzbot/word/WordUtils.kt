package ltd.guimc.lgzbot.word

import com.huaban.analysis.jieba.JiebaSegmenter

object WordUtils {
    fun containsChinese(text: String): Boolean {
        val regex = Regex("[\\u4E00-\\u9FFF]+")
        return regex.containsMatchIn(text)
    }
    fun hashMapToString(wordFrequency: HashMap<String, Int>): String {
        val sb = StringBuilder()
        for ((key, value) in wordFrequency) {
            sb.append("$key: $value\n")
        }
        return sb.toString()
    }
    fun filter(message:String):List<String>{
        return JiebaSegmenter().sentenceProcess(message).filter { it.length>=2 && containsChinese(it) }
    }
    fun sortAndTrim(wordFrequency: HashMap<String, Int>,n: Int): HashMap<String, Int> {
        val sortedList = wordFrequency.toList().sortedByDescending { it.second }
        val result = hashMapOf<String, Int>()
        var count = 0
        for ((key, value) in sortedList) {
            if (count >= n) {
                break
            }
            result[key] = value
            count++
        }
        return result
    }
}