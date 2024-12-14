package org.smart4j.chapter3.proxy;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

public class GreetingBeforAdvice implements MethodBeforeAdvice, AfterReturningAdvice {
    @Override
    public void before(java.lang.reflect.Method method, Object[] args, Object target) throws Throwable {
        System.out.println("before:");
    }

    @Override
    public void afterReturning(Object returnValue, java.lang.reflect.Method method, Object[] args, Object target) throws Throwable {
        System.out.println("After " + method.getName() + " " + target);
    }
}
