package ltd.guimc.lgzbot.listener.nudge

import ltd.guimc.lgzbot.PluginMain
import ltd.guimc.lgzbot.utils.MemberUtils.mute
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.permission.PermitteeId.Companion.permitteeId
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.getMemberOrFail
import net.mamoe.mirai.event.events.NudgeEvent

object NudgeMute {
    suspend fun onNudge(e: NudgeEvent) {
        // check target
        if (e.target != e.bot) return

        // check subject
        if (e.subject !is Group) return

        val group = e.subject as Group
        val from = group.getMemberOrFail(e.from.id)

        // check permission
        if (!group.permitteeId.hasPermission(PluginMain.nudgeMute)) return

        // check bot permission
        if (from.permission.level >= group.botAsMember.permission.level) return

        from.mute(600, "NudgeMute is Enabled")
        e.cancel()
    }
}