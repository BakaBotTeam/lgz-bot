package ltd.guimc.lgzbot.counter;

import net.mamoe.mirai.contact.Member;

import java.util.HashMap;

public class VLManager {
    private static final HashMap<Member, Counter> counters = new HashMap<>();
    public static Counter getCounter(Member p) {
        Counter c = counters.get(p);
        if (c == null) {
            c = new Counter(p);
            counters.put(p, c);
        }
        return c;
    }
}
