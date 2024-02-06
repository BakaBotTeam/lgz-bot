package ltd.guimc.lgzbot.counter;

import net.mamoe.mirai.contact.Member;

import java.util.HashMap;

public class Counter {
    private Member member;
    public Counter(Member p) {
        member = p;
    }

    public HashMap<String, Integer> wordFrequency = new HashMap<>();
    public int messageVl=0;
}
