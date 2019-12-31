package com.phenix.tools.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Map;

/**
 * 反射帮助类
 *
 * @author zhenghui
 * @date 2019-1-9
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReflectUtils {


    public static Object getAnnotationValue(Annotation annotation, String property) {
        Object result = null;
        if (annotation != null) {
            InvocationHandler invo = Proxy.getInvocationHandler(annotation); //获取被代理的对象
            Map map = (Map) getFieldValue(invo, property);
            if (map != null) {
                result = map.get(property);
            }
        }
        return result;
    }


    public static <T> Object getFieldValue(T target, String property) {
        if (target == null || property == null) {
            return null;
        }
        Class<T> currClass = (Class<T>) target.getClass();

        try {
            Field field = currClass.getDeclaredField(property);
            field.setAccessible(true);
            return field.get(target);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(currClass + " has no property: " + property);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static <T> Object getFieldValue(T target, Class<T> currClass, String property) {
        if (target == null || currClass == null || property == null) {
            return null;
        }
        try {
            Field field = currClass.getDeclaredField(property);
            field.setAccessible(true);
            return field.get(target);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(currClass + " has no property: " + property);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T, R> Boolean setFieldValue(T target, String property, R value) {
        if (target == null || property == null) {
            return null;
        }
        Class<T> currClass = (Class<T>) target.getClass();

        try {
            Field field = currClass.getDeclaredField(property);
            field.setAccessible(true);
            field.set(target, value);
            return true;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(currClass + " has no property: " + property);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static <T, R> Boolean setFieldValue(T target, Class<T> currClass, String property, R value) {
        if (target == null || currClass == null || property == null) {
            return null;
        }
        try {
            Field field = currClass.getDeclaredField(property);
            field.setAccessible(true);
            field.set(target, value);
            return true;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(currClass + " has no property: " + property);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static <T> T execute(Class<?> clazz, Object obj, String methodName) {
        try {
            Method method = clazz.getMethod(methodName);
            return method == null ? null : (T) method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T executePrivate(Class<?> clazz, Object obj, String methodName) {
        try {
            Method methodPrivate = clazz.getDeclaredMethod(methodName);
            methodPrivate.isAccessible();
            //调private方法
            methodPrivate.setAccessible(true);
            return (T) methodPrivate.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> T execute(Class<?> objclass, Object obj, String methodName, T value) {
        try {
            if (value == null) {
                return null;
            }
            Method func = objclass.getMethod(methodName, value.getClass());
            if (func != null) {
                return (T) func.invoke(obj, value);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T executeInt(Class<?> objclass, Object obj, String methodName, int value) {
        try {
            Method func = objclass.getMethod(methodName, int.class);
            return (T) func.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T executeDouble(Class<?> objclass, Object obj, String methodName, double value) {
        try {
            Method func = objclass.getMethod(methodName, double.class);
            return (T) func.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T executeFloat(Class<?> objclass, Object obj, String methodName, float value) {
        try {
            Method func = objclass.getMethod(methodName, float.class);
            return (T) func.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> T executeLong(Class<?> objclass, Object obj, String methodName, long value) {
        try {
            Method func = objclass.getMethod(methodName, long.class);
            return (T) func.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
