package ltd.guimc.lgzbot.utils

import huzpsb.ll4j.utils.data.DataSet
import huzpsb.ll4j.model.Model
import huzpsb.ll4j.nlp.token.Tokenizer
import java.io.BufferedReader
import java.io.InputStreamReader

object LL4JUtils {
    lateinit var model: Model
    lateinit var tokenizer: Tokenizer

    fun init() {
        val tokenizerFile = LL4JUtils.javaClass.getResourceAsStream("/ts.model")!!
        val modelFile = LL4JUtils.javaClass.getResourceAsStream("/anti-ad.model")!!
        tokenizer = Tokenizer.load(tokenizerFile)
        model = Model.read(BufferedReader(InputStreamReader(modelFile)))
    }

    fun predict(string: String): Boolean =
        model.predictDebug(tokenizer.tokenize(0, string.replace("\n", "")).values).first == 1

    fun predictDebug(string: String): Pair<Int, Double> =
        model.predictDebug(tokenizer.tokenize(0, string.replace("\n", "")).values)

    fun predictAllResult(string: String): DoubleArray =
        model.predictAllResult(tokenizer.tokenize(0, string.replace("\n", "")).values)

    fun learn(type: Int, string: String) {
        val dataSet = DataSet()
        dataSet.split.add(tokenizer.tokenize(type, string.replace("\n", "")))
        model.trainOn(dataSet)
    }
}