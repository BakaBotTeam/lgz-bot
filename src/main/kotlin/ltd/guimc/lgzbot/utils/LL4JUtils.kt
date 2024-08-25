package ltd.guimc.lgzbot.utils

import huzpsb.ll4j.model.Model
import huzpsb.ll4j.nlp.token.Tokenizer
import huzpsb.ll4j.utils.data.DataSet
import ltd.guimc.lgzbot.utils.AsciiUtil.sbc2dbcCase
import ltd.guimc.lgzbot.utils.TextUtils.removeInterference
import ltd.guimc.lgzbot.utils.TextUtils.removeNonVisible

object LL4JUtils {
    lateinit var model: Model
    lateinit var tokenizer: Tokenizer
    var version = "FEB25"

    fun init() {
        val tokenizerFile = LL4JUtils.javaClass.getResourceAsStream("/ts.model")!!
        val modelFile = LL4JUtils.javaClass.getResourceAsStream("/anti-ad.model")!!
        tokenizer = Tokenizer.load(tokenizerFile.bufferedReader(Charsets.UTF_8))
        modelFile.bufferedReader(Charsets.UTF_8).use {
            model = Model.read(it)
        }
    }

    fun predict(string: String): Boolean =
        model.predictDebug(
            tokenizer.tokenize(
                0,
                string.replace("\n", "").replace("live.bilibili.com", "")
            ).values
        ).first == 1

    fun predictDebug(string: String): Pair<Int, Double> =
        model.predictDebug(tokenizer.tokenize(0, string.replace("\n", "").replace("live.bilibili.com", "")).values)

    fun predictAllResult(string: String): DoubleArray =
        model.predictAllResult(
            tokenizer.tokenize(
                0, sbc2dbcCase(string.replace("\n", "").replace("live.bilibili.com", ""))
                    .lowercase()
                    .removeInterference()
                    .removeNonVisible()
            ).values
        )

    fun learn(type: Int, string: String) {
        val dataSet = DataSet()
        dataSet.split.add(tokenizer.tokenize(type, string.replace("\n", "")))
        model.trainOn(dataSet)
    }

    fun downloadModel() {
        try {
            model = Model.read(HttpUtils.getResponse("https://raw.githubusercontent.com/siuank/ADDetector4J/main/anti-ad.model"))
            tokenizer = Tokenizer.load(HttpUtils.getResponse("https://raw.githubusercontent.com/siuank/ADDetector4J/main/t1.tokenized.txt").reader())
            val time = GithubUtils.getLastCommit("siuank/ADDetector4J").commitTime
            version = "${time.month.name.substring(0..3)}${time.dayOfMonth}"
        } catch (_: Exception) {}
    }
}