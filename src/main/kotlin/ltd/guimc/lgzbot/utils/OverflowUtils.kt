package ltd.guimc.lgzbot.utils

object OverflowUtils {
    fun checkOverflowCore(): Boolean {
        try {
            Class.forName("top.mrxiaom.overflow.internal.Overflow")
            return true
        } catch (ignored: ClassNotFoundException) {
            return false
        }
    }
}