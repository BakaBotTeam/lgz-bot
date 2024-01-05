package ltd.guimc.nextgen.lgzbot;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.springframework.stereotype.Component;

@Component
public class PluginMain extends BotPlugin {
    @GroupMessageHandler
    public int onGroupMessage(Bot bot, GroupMessageEvent event) {
        return MESSAGE_IGNORE;
    }
}
