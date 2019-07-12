package utils;

import java.io.Serializable;
import java.security.MessageDigest;

/**
 * @author Drogon1573
 * @date 2019/7/5
 */
public class Md5Util implements Serializable {
    private static final char[] HEX_DIGITS =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
            'D', 'E', 'F'};
    private static final long serialVersionUID = -8096980929537508916L;

    /**
     * 生成指定字符串的MD5摘要
     * @param s 明文
     * @return MD5信息摘要
     */
    public final static String encrypt(String s) {
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
