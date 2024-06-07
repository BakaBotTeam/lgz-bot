import ltd.guimc.lgzbot.utils.LL4JUtils;

import java.nio.charset.Charset;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        LL4JUtils.INSTANCE.init();
        System.out.println(Charset.defaultCharset());
        System.out.println(Arrays.toString(LL4JUtils.INSTANCE.predictAllResult("")));
        System.out.println(LL4JUtils.INSTANCE.getModel().predict(LL4JUtils.INSTANCE.getTokenizer().tokenize(0, " Oyster 新内步" +
            "学生党の首选" +
            "[+]吻订沙路，让您所向披靡" +
            "[+]反击退，让您不再惧怕掉进虚空" +
            "[+]叨抱，让沙路的抱力更上一层楼" +
            "[+]搭路，吻订不吞方块" +
            "[+]瞬移，远程偷家让别人 die 都不知道怎么 die 的" +
            "[+]视觉美丽的同时让您 fps++" +
            "[+]完美结局总动 GG，装 b 更加彻底" +
            "[+]万能的管理不会不耐烦" +
            "[+]我们承诺永不跑路" +
            "内步 15r buy➕1632596365").values));
    }
}
