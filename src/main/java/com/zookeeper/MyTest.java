package com.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.io.IOException;

public class MyTest {
    public static void main(String[] args) throws IOException {
      CuratorFramework build=   CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
               .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        build.start();
        try {
           String path= build.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/myTest2");
           System.out.println(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.in.read();
    }
}
