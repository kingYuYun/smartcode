package org.smart4j.chapter3.service;

import org.smart4j.chapter3.interfaceInfo.Greeting;

public class GreetingImpl implements Greeting {
    public void sayHello(String name) {
        System.out.println("Hello " + name);
    }
}
