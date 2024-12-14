package org.smart4j.chapter3.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class CGLibDtnamicProxy implements MethodInterceptor {

    private static CGLibDtnamicProxy instance = new CGLibDtnamicProxy();

    public static CGLibDtnamicProxy getInstance() throws Exception {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> cls) throws Exception {
        return (T) Enhancer.create(cls, this);
    }

    public Object intercept(Object tar, java.lang.reflect.Method method, Object[] objects, net.sf.cglib.proxy.MethodProxy methodProxy) throws Throwable {
        before();
        Object result = methodProxy.invokeSuper(tar, objects);
        after();
        return result;
    }

    private void after() {
        System.out.println("after proxy");
    }

    private void before() {
        System.out.println("before proxy");
    }
}
