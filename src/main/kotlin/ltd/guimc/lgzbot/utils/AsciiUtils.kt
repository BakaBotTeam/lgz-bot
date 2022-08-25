package ltd.guimc.lgzbot.utils

object AsciiUtil {
    const val SBC_SPACE = 12288 // 全角空格 12288
        .toChar()
    const val DBC_SPACE = 32 //半角空格 32
        .toChar()

    // ASCII character 33-126 <-> unicode 65281-65374
    const val ASCII_START = 33.toChar()
    const val ASCII_END = 126.toChar()
    const val UNICODE_START = 65281.toChar()
    const val UNICODE_END = 65374.toChar()
    const val DBC_SBC_STEP = 65248 // 全角半角转换间隔
        .toChar()

    fun sbc2dbc(src: Char): Char {
        if (src == SBC_SPACE) {
            return DBC_SPACE
        }
        return if (src in UNICODE_START..UNICODE_END) {
            (src.code - DBC_SBC_STEP.code).toChar()
        } else src
    }

    /**
     * Convert from SBC case to DBC case
     *
     * @param src
     * @return DBC case
     */
    fun sbc2dbcCase(src: String): String {
        val c = src.toCharArray()
        for (i in c.indices) {
            c[i] = sbc2dbc(c[i])
        }
        return String(c)
    }

    fun dbc2sbc(src: Char): Char {
        if (src == DBC_SPACE) {
            return SBC_SPACE
        }
        return if (src <= ASCII_END) {
            (src.code + DBC_SBC_STEP.code).toChar()
        } else src
    }

    /**
     * Convert from DBC case to SBC case.
     *
     * @param src
     * @return SBC case string
     */
    fun dbc2sbcCase(src: String?): String? {
        if (src == null) {
            return null
        }
        val c = src.toCharArray()
        for (i in c.indices) {
            c[i] = dbc2sbc(c[i])
        }
        return String(c)
    }
}