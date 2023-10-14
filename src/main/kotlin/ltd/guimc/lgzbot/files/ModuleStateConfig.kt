package ltd.guimc.lgzbot.files

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object ModuleStateConfig : ReadOnlyPluginConfig("modulestate") {
    @ValueDescription("消息过滤器与刷屏检测")
    val messageFilter by value(true)

    @ValueDescription("戳一戳处理")
    val nudge by value(true)

    @ValueDescription("机器人被邀请进群处理")
    val invite by value(true)

    @ValueDescription("GroupListener (进群/退群/禁言/解除禁言)")
    val grouplistener by value(true)

    @ValueDescription("占卜功能")
    val fortune by value(true)

    @ValueDescription("历史上的今天")
    val historytoday by value(true)

    @ValueDescription("摸鱼人日历")
    val moyu by value(true)

    @ValueDescription("GitHub仓库信息自动查询")
    val githubquery by value(true)

    @ValueDescription("静默[滥权小助手]")
    val silentmute by value(true)
}