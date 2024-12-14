package org.smart4j.framework.helper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.proxy.AspectProxy;
import org.smart4j.framework.proxy.Proxy;
import org.smart4j.framework.proxy.ProxyManager;
import org.smart4j.framework.proxy.TransactionProxy;

/**
 * 方法拦截助手类
 *
 * @author huangyong
 * @since 1.0.0
 */
public final class AopHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            //获取代理类机器目标类集合的映射关系
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            //获取目标类对应的代理对象列表的映射关系
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            //遍历映射关系，从中获取到目标类与代理对象列表
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                //创建代理对象：调用ProxyManager.createProxy(targetClass, proxyList)方法创建代理对象。
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                //注册代理对象：将创建的代理对象注册到BeanHelper中。
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            LOGGER.error("aop failure", e);
        }
    }

    // 创建代理类对应的代理点之间的映射关系
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
        addAspectProxy(proxyMap);
        addTransactionProxy(proxyMap);
        return proxyMap;
    }

    // 创建目标类对应的代理对象列表之间的映射关系
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        //初始化映射：创建一个 HashMap 对象 targetMap，用于存储目标类与代理对象列表的映射关系。
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        //遍历输入映射：遍历传入的 proxyMap，其中键是代理类，值是目标类的集合。
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            Class<?> proxyClass = proxyEntry.getKey();
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            for (Class<?> targetClass : targetClassSet) {
                //创建代理对象：对于每个代理类，通过 newInstance() 方法创建一个新的代理对象实例。
                Proxy proxy = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(targetClass)) {
                    //添加到映射：检查 targetMap 是否已包含当前目标类，如果包含则将代理对象添加到现有的列表中；
                    targetMap.get(targetClass).add(proxy);
                } else {
                    //否则，创建一个新的列表并将其添加到 targetMap 中。
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }
        return targetMap;
    }

    // 创建代理类对应的切点
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        //获取应用包名下某父类或接口的所有子类或实现类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> proxyClass : proxyClassSet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
    }

    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
        //获取应用包名下带有某注解的所有类
        Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
        proxyMap.put(TransactionProxy.class, serviceClassSet);
    }

    //
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        if (annotation != null && !annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

}
