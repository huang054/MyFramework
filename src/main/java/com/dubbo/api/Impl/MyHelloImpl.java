package com.dubbo.api.Impl;

import com.dubbo.api.MyHello;
import com.dubbo.common.dubbo;

@dubbo
public class MyHelloImpl implements MyHello {
    @Override
    public String hello(String name) {
        return "hello "+name;
    }
}
