package ltd.guimc.lgzbot.utils

import huzpsb.ll4j.data.DataSet
import huzpsb.ll4j.model.HsmLoader
import huzpsb.ll4j.model.Model
import huzpsb.ll4j.nlp.token.Tokenizer

object LL4JUtils {
    lateinit var model: Model
    lateinit var tokenizer: Tokenizer

    fun init() {
        val tokenizerFile = LL4JUtils.javaClass.getResourceAsStream("/ts.model")
        val modelFile = LL4JUtils.javaClass.getResourceAsStream("/anti-ad.model")
        tokenizer = Tokenizer.loadFromFile(tokenizerFile)
        model = HsmLoader.load(modelFile)
    }

    fun predict(string: String): Boolean =
        model.predict(tokenizer.tokenize(0, string.replace("\n", "")).values).keys.iterator().next() == 1

    fun predictMap(string: String): MutableMap<Int, Double> =
        model.predict(tokenizer.tokenize(0, string.replace("\n", "")).values)

    fun predictAllResult(string: String): DoubleArray =
        model.predictAllResult(tokenizer.tokenize(0, string.replace("\n", "")).values)

    fun learn(type: Int, string: String) {
        val dataSet = DataSet()
        dataSet.split.add(tokenizer.tokenize(type, string.replace("\n", "")))
        model.trainOn(dataSet)
    }
}