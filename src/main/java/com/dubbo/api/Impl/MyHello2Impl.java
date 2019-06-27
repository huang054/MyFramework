package com.dubbo.api.Impl;

import com.dubbo.api.MyHello2;
import com.dubbo.common.dubbo;


public class MyHello2Impl implements MyHello2 {
    @Override
    public String hello(String name) {
        return name+":hello";
    }
}
