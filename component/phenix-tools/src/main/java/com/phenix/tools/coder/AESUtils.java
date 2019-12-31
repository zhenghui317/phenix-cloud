package com.phenix.tools.coder;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * @Author: John
 * @Date: 2019/3/13 17:39
 */
@Slf4j
public final class AESUtils {

    public static final String SIGN_ALGORITHMS = "SHA1PRNG";
    private static final String KEY_GENERATOR = "AES";
    private static final String ENCODING = "UTF-8";

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return
     */
    public static String encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_GENERATOR);
            SecureRandom random = SecureRandom.getInstance(SIGN_ALGORITHMS);
            random.setSeed(password.getBytes(ENCODING));
            kgen.init(256, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, KEY_GENERATOR);
            Cipher cipher = Cipher.getInstance(KEY_GENERATOR);// 创建密码器
            byte[] byteContent = content.getBytes(ENCODING);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return parseByte2HexStr(result); // 加密
        } catch (Exception e) {
            log.error("加密异常", e);
        }
        return null;
    }


    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static String decrypt(String content, String password) {
        try {
            byte[] bytes = parseHexStr2Byte(content);
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_GENERATOR);
            SecureRandom random = SecureRandom.getInstance(SIGN_ALGORITHMS);
            random.setSeed(password.getBytes(ENCODING));
            kgen.init(256, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, KEY_GENERATOR);
            Cipher cipher = Cipher.getInstance(KEY_GENERATOR);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(bytes);
            return new String(result, ENCODING); // 加密
        } catch (Exception e) {
            log.error("解密异常", e);
        }
        return null;
    }


    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        String content = "test53546334";
        String password = "12345678";
        //加密
        System.out.println("加密前：" + content);
        String encryptResultStr = encrypt(content, password);
        System.out.println("加密后：" + encryptResultStr);
        //解密
        System.out.println("解密后：" + decrypt(encryptResultStr, password));
    }
}
