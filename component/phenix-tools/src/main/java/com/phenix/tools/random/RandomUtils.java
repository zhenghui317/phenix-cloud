package com.phenix.tools.random;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

/**
 * 随机工具类
 * @author Administrator
 *
 */
public class RandomUtils {

    /**
     * 随机数字(根据长度随机)
     * @param maxLength 位数
     * @return
     */
    public static String randomNumberByLength(int maxLength){

        return RandomStringUtils.random(maxLength);
    }

    /**
     * 随机数字
     * @param maxValue 最大范围（不包含）
     * @return
     */
    public static String randomNumberByMaxValue(int maxValue){
        Random ra = new Random();
        return ra.nextInt(maxValue)+"";
    }


    /**
     * 随机字母
     * @param count
     * @return
     */
    public static String randomAlpha(int count){
        return RandomStringUtils.randomAlphabetic(count);
    }

    /**
     * 字母加数字
     * @param count
     * @return
     */
    public static String randomAlphaAndNumeric(int count){
        return RandomStringUtils.randomAlphanumeric(count);
    }

    /**
     * 纯数字
     * @param count
     * @return
     */
    public static String randomNumeric(int count){
        return RandomStringUtils.randomNumeric(count);
    }

    public static void main(String[] args) {

        System.out.println(RandomUtils.randomAlphaAndNumeric(16));

    }
}