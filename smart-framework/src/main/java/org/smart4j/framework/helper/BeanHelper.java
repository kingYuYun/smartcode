package org.smart4j.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.smart4j.framework.util.ReflectionUtil;

/**
 * Bean 助手类
 *
 * @author huangyong
 * @since 1.0.0
 */
public final class BeanHelper {

    /**
     * Bean 映射（用于存放 Bean 对象，key 为 Bean 类，value 为 Bean 对象）
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

        /*
                获取所有的 Bean 类
        */
    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> beanClass : beanClassSet) {
            Object obj = ReflectionUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass, obj);
        }
    }

    /**
     * 获取 Bean 映射
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 获取 Bean 实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls) {
        if (!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("can not get bean by class: " + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    /**
     * 设置 Bean 实例
     */
    public static void setBean(Class<?> cls, Object obj) {
        BEAN_MAP.put(cls, obj);
    }
}
