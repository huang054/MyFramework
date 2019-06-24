package com.dubbo.consumer.test;

import com.dubbo.api.MyHello;
import com.dubbo.consumer.client.Client;

public class Test {

    public static void main(String[] args) {
       MyHello myHello= Client.createProxy(MyHello.class);
       String s=myHello.hello("逍遥叹");

       System.out.println(s);
    }
}
