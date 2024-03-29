/*
 * THIS FILE IS PART OF lgz-bot PROJECT
 *
 * You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.
 * Your modified application must also be licensed under the AGPLv3.
 *
 * Copyright (c) 2022 - now Guimc Team.
 */

package ltd.guimc.lgzbot.utils

import java.util.*
import kotlin.math.max
import kotlin.math.min

object TextUtils {
    private fun getLevenshteinDistance(X: String, Y: String): Int {
        val m = X.length
        val n = Y.length
        val T = Array(m + 1) { IntArray(n + 1) }
        for (i in 1..m) {
            T[i][0] = i
        }
        for (j in 1..n) {
            T[0][j] = j
        }
        var cost: Int
        for (i in 1..m) {
            for (j in 1..n) {
                cost = if (X[i - 1] == Y[j - 1]) 0 else 1
                T[i][j] = min(min(T[i - 1][j] + 1, T[i][j - 1] + 1),
                    T[i - 1][j - 1] + cost)
            }
        }
        return T[m][n]
    }

    // 寻找两字符串相似度，返回双精度浮点数
    fun findSimilarity(x: String?, y: String?): Double {
        require(!(x == null || y == null)) { "Strings should not be null" }

        val maxLength = max(x.length, y.length)
        return if (maxLength > 0) {
            (maxLength * 1.0 - getLevenshteinDistance(x, y)) / maxLength * 1.0
        } else 1.0
    }

    // 去除不可见字符
    fun String.removeNonVisible(): String {
        return this.replace("\\p{C}".toRegex(), "")
    }

    // 去除干扰字符
    fun String.removeInterference(): String {
        return this.replace(" ", "")
            .replace(",", "")
            .replace(".", "")
            .replace("!", "")
            .replace("?", "")
            .replace(";", "")
            .replace(":", "")
            .replace("\"", "")
            .replace("'", "")
            .replace("“", "")
            .replace("”", "")
            .replace("‘", "")
            .replace("’", "")
            .replace("<", "")
            .replace(">", "")
            .replace("(", "")
            .replace(")", "")
            .replace("內", "内")
    }

    fun String.convert2UUID(): UUID {
        return UUID.fromString("${this.substring(0, 7)}-${this.substring(8, 11)}-${this.substring(12, 15)}-${this.substring(16, 19)}-${this.substring(20)}")
    }
}