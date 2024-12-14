package org.smart4j.chapter3;

import org.smart4j.chapter3.interfaceInfo.Greeting;
import org.smart4j.chapter3.proxy.GreetingBeforAdvice;
import org.smart4j.chapter3.service.GreetingImpl;
import org.springframework.aop.framework.ProxyFactory;

public class Client {
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        // 1、创建JDK动态代理,只能代理接口，不能代理没有接口的类
//        Greeting greeting = new JDKDynamicProxy(new GreetingImpl()).getProxy();
        
        //2、CGLib动态代理
//        Greeting greeting = CGLibDtnamicProxy.getInstance().getProxy(GreetingImpl.class);
        //3、SpringAOP
        ProxyFactory proxyFactory=  new ProxyFactory(); // 创建代理工厂
        proxyFactory.setTarget(new GreetingImpl()); // 设置目标对象
        proxyFactory.addAdvice(new GreetingBeforAdvice()); //添加前置增强+添加后置增强
        Greeting greeting = (Greeting) proxyFactory.getProxy(); //从代理工厂获取代理对象
        greeting.sayHello("World");
    }
}
