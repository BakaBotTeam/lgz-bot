import ltd.guimc.lgzbot.utils.LL4JUtils;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        LL4JUtils.INSTANCE.init();
        System.out.println(Arrays.toString(LL4JUtils.INSTANCE.predictAllResult("")));
    }
}
