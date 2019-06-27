package com.dubbo.consumer.test;

import com.dubbo.api.MyHello;
import com.dubbo.api.MyHello2;
import com.dubbo.consumer.client.Client;

public class Test {

    public static void main(String[] args) {
        MyHello myHello1= Client.createProxy(MyHello.class);
        String s1=myHello1.hello("逍遥叹");
        System.out.println(s1);
       MyHello2 myHello= Client.createProxy(MyHello2.class);
       String s=myHello.hello("逍遥叹");

       System.out.println(s);
    }
}
