package huzpsb.ll4j.nlp.token;

import java.util.regex.Pattern;

/**
 * from jie-ba
 */
public class CharUtils {
    private static final char[] connectors = new char[] { '+', '#', '&', '.', '_', '-' };


    public static boolean isChineseLetter(char ch) {
        return ch >= 0x4E00 && ch <= 0x9FA5;
    }


    public static boolean isEnglishLetter(char ch) {
        return (ch >= 0x0041 && ch <= 0x005A) || (ch >= 0x0061 && ch <= 0x007A);
    }


    public static boolean isDigit(char ch) {
        return ch >= 0x0030 && ch <= 0x0039;
    }


    public static boolean isConnector(char ch) {
        for (char connector : connectors)
            if (ch == connector)
                return true;
        return false;
    }


    public static boolean ccFind(char ch) {
        if (isChineseLetter(ch))
            return true;
        if (isEnglishLetter(ch))
            return true;
        if (isDigit(ch))
            return true;
        return isConnector(ch);
    }

    private static final Pattern ENGLISH_CONTEXT = Pattern.compile("[a-zA-Z]+");

    public static boolean isEnglishContext(String input) {
        return ENGLISH_CONTEXT.matcher(input).matches();
    }

    /**
     * 全角 to 半角,大写 to 小写
     *
     * @param input
     *            输入字符
     * @return 转换后的字符
     */
    public static char regularize(char input) {
        if (input == 0x3000) {
            return 32;
        }
        else if (input > 0xff00 && input < 0xff5f) {
            return (char) (input - 0xfee0);
        }
        else if (input >= 'A' && input <= 'Z') {
            input += 32;
        }
        return input;
    }

    public static String regularize(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = regularize(input.charAt(i));
            if (ccFind(c)) sb.append(c);
        }
        return sb.toString();
    }
}
