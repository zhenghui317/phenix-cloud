package com.phenix.tools.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * 签名类
 *
 * @author zhenghui
 * @date 2018-12-29
 */
public class SignUtils {
    private static Logger logger = LoggerFactory.getLogger(SignUtils.class);

    /**
     * 签名
     *
     * @param entity
     * @return
     * @throws Exception
     */
    public static String sign(Object entity, String appkey){
        TreeMap<String, String> params = SignUtils.objToMap(entity);
        return SignUtils.sign(params, appkey);
    }

    /**
     * 签名
     *
     * @param params
     * @return
     * @throws Exception
     */
    public static String sign(TreeMap<String, String> params, String appkey){
        try {
            if (params.containsKey("sign")) {
                //签名明文组装不包含sign字段
                params.remove("sign");
            }
            params.put("key", appkey);
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() != null && entry.getValue().length() > 0) {
                    sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            //记得是md5编码的加签
            String sign = md5(sb.toString().getBytes("UTF-8"));
            params.remove("key");
            return sign;
        } catch (Exception ex) {
            SignUtils.logger.error(ex.getMessage());
            return null;
        }
    }

    public static boolean validSign(Object entity, String appkey){
        TreeMap<String, String> params = SignUtils.objToMap(entity);
        return SignUtils.validSign(params, appkey);
    }

    public static boolean validSign(TreeMap<String, String> param, String appkey){
        if (param != null && !param.isEmpty()) {
            if (!param.containsKey("sign")) {
                return false;
            }
            String sign = param.get("sign").toString();
            String mysign = sign(param, appkey);
            return sign.toLowerCase().equals(mysign.toLowerCase());
        }
        return false;
    }

    /**
     * md5
     *
     * @param b
     * @return
     */
    public static String md5(byte[] b) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(b);
            byte[] hash = md.digest();
            StringBuffer outStrBuf = new StringBuffer(32);
            for (int i = 0; i < hash.length; i++) {
                int v = hash[i] & 0xFF;
                if (v < 16) {
                    outStrBuf.append('0');
                }
                outStrBuf.append(Integer.toString(v, 16).toLowerCase());
            }
            return outStrBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            SignUtils.logger.error(e.getMessage());
            return new String(b);
        }
    }

    /**
     * json转化为实体
     *
     * @param <T>
     * @param jsonstr
     * @param cls
     * @return
     */
    public static <T> T json2Obj(String jsonstr, Class<T> cls) {
        T entity = JSONObject.parseObject(jsonstr,cls);
        return entity;
    }

    /**
     * 将对象转成TreeMap,属性名为key,属性值为value
     *
     * @param object 对象
     * @return
     * @throws IllegalAccessException
     */
    private static TreeMap<String, String> objToMap(Object object){
        try {
            Class clazz = object.getClass();
            TreeMap<String, String> treeMap = new TreeMap<String, String>();

            while (null != clazz.getSuperclass()) {
                Field[] declaredFields1 = clazz.getDeclaredFields();

                for (Field field : declaredFields1) {
                    String name = field.getName();

                    // 获取原来的访问控制权限
                    boolean accessFlag = field.isAccessible();
                    // 修改访问控制权限
                    field.setAccessible(true);
                    Object value = field.get(object);
                    // 恢复访问控制权限
                    field.setAccessible(accessFlag);

                    if (null != value && StringUtils.isNotBlank(value.toString())) {
                        //如果是List,将List转换为json字符串
                        if (value instanceof List) {
                            value = JSON.toJSONString(value);
                        }
                        treeMap.put(name, value.toString());
                    }
                }

                clazz = clazz.getSuperclass();
            }
            return treeMap;
        }catch (Exception e){
            SignUtils.logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 生成随机码
     *
     * @param n
     * @return
     */
    public static String getValidatecode(int n) {
        Random random = new Random();
        String sRand = "";
        n = n == 0 ? 4 : n;// default 4
        for (int i = 0; i < n; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
        }
        return sRand;
    }

}
