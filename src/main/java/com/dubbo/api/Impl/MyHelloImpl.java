package com.dubbo.api.Impl;

import com.dubbo.api.MyHello;

public class MyHelloImpl implements MyHello {
    @Override
    public String hello(String name) {
        return "hello "+name;
    }
}
