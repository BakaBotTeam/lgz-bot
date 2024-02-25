import ltd.guimc.lgzbot.utils.LL4JUtils;

import java.nio.charset.Charset;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        LL4JUtils.INSTANCE.init();
        System.out.println(Charset.defaultCharset());
        System.out.println(Arrays.toString(LL4JUtils.INSTANCE.predictAllResult("shop.nyaproxy.xyz 1+ 21+NFA补货出mc美区激活码")));
        System.out.println(LL4JUtils.INSTANCE.getModel().predict(LL4JUtils.INSTANCE.getTokenizer().tokenize(0, "shop.nyaproxy.xyz 1+ 21+NFA补货出mc美区激活码").values));
    }
}
