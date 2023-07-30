package ltd.guimc.lgzbot.hypixel

import java.util.Date
import java.util.UUID
import kotlin.properties.Delegates

class Player(val networkLevel: Double, val karma: Int, val lastLoginDate: Date, val lastLogoutDate: Date, val highestRank: String?)

fun getPlayer(uuid: String): Player {
    val resp = HypixelApiUtils.request("/player?uuid=$uuid").getJSONObject("player")
    val networkLevel = resp.getDouble()
    return TODO("Provide the return value")
}