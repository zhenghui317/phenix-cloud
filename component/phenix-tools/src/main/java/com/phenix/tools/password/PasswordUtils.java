package com.phenix.tools.password;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * 密码工具类
 * @author zhenghui
 * @date 2019-11-18
 */
@Slf4j
public class PasswordUtils {

    private static final String KEY = "gvrlucrb7s3k076n";
    /**
     * 加密密码
     * @param password
     * @return
     */
    public static String encryptPassword(String password) {
        return PasswordUtils.encryptPassword(KEY, password);
    }

    /**
     * 加密密码
     * @param pkey
     * @param password
     * @return
     */
    public static String encryptPassword(String pkey, String password) {

//        String key = HexUtil.encodeHexStr(pkey);
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, KEY.getBytes());
        //加密为16进制表示
        String encryptHex = aes.encryptHex(password);
        return encryptHex;
    }


    /**
     * 解密密码
     * @param cipherText
     * @return
     * @throws Exception
     */
    public static String decrypt(String cipherText) throws Exception {
        return decrypt(KEY, cipherText);
    }

    /**
     * 解密密码
     * @param pkey
     * @param cipherText
     * @return
     * @throws Exception
     */
    public static String decrypt(String pkey, String cipherText) throws Exception {
//        String key = HexUtil.encodeHexStr(pkey);
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, KEY.getBytes());
        //解密为字符串
        String decryptStr = aes.decryptStr(cipherText, Charset.forName("UTF-8"));
        return decryptStr;
    }


}
