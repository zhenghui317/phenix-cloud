package com.phenix.tools.coder;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * MD5工具类
 * @author zhenghui
 * @date 2019-10-24
 */
public class MD5Utils {

    /**
     * 利用MD5进行加密
     *
     * @param plainText 待加密的字符串
     * @return 加密后的字符串
     */
    public String encoderByMd5(String plainText) {
        //定义一个字节数组
        byte[] secretBytes = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(plainText.getBytes());
            //获得加密后的数据
            secretBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        //将加密后的数据转换为16进制数字
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    /**
     * 判断用户密码是否正确
     *
     * @param newpasswd 用户输入的密码
     * @param oldpasswd 数据库中存储的密码－－用户密码的摘要
     * @return
     */
    public boolean checkpassword(String newpasswd, String oldpasswd) {
        if(StringUtils.isBlank(newpasswd)){
            return false;
        }
        if (encoderByMd5(newpasswd).equals(oldpasswd)) {
            return true;
        }
        return false;
    }
}