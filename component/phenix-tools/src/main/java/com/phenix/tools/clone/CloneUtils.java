package com.phenix.tools.clone;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: CloneUtils.java
 * Description: 拷贝List<T>中的对象到另一个List<V>中
 *
 * @author zhenghui
 * @date 2018/12/19 14:59
 * @since JDK 1.8
 */
@Slf4j
public class CloneUtils {

    /**
     * 拷贝相同属性值到新的对象中
     * @param oldBean 被拷贝的对象
     * @param vClass 需要拷贝相同属性的类
     * @param <T> 被拷贝的类（抽象意义上）
     * @param <V> 需要拷贝的类（抽象意义上）
     * @return 完成属性拷贝的新对象
     */
    public static <T, V> V copyColumnProperties(T oldBean,Class<V> vClass){
        try {
            V newBean = vClass.newInstance();
            BeanUtils.copyProperties(oldBean,newBean);
            return newBean;

        }catch (Exception e){
            log.error("拷贝对象中的相同属性失败,原因[{}]",e.getMessage());
            return null;
        }
    }

    /**
     * 循环拷贝oldList中的对象到newList中
     * @param oldList 被拷贝的列表对象
     * @param vClass 需要拷贝对应属性的类
     * @param <T> 被拷贝的类（抽象意义上）
     * @param <V> 需要拷贝的类（抽象意义上）
     * @return 完成拷贝的新List<V>
     */
    public static <T, V> List<V> copyListProperties(List<T> oldList, Class<V> vClass) {
        try {
            List<V> newList = new ArrayList<V>();

            for (T t : oldList) {
                V v = vClass.newInstance();
                BeanUtils.copyProperties(t, v);
                newList.add(v);
            }
            return newList;

        }catch (Exception e){
            log.error("拷贝list中对象的相应属性出错,原因[{}]",e.getMessage());
            return null;
        }

    }
}
