import ltd.guimc.lgzbot.utils.LL4JUtils;

public class Test {
    public static void main(String[] args) {
        LL4JUtils.INSTANCE.init();
        System.out.println(LL4JUtils.INSTANCE.predict("我测你的吗"));
    }
}
