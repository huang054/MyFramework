package com.zookeeper;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;


public class MyClient {

    public static void main(String[] args) throws Exception {
        final CuratorFramework build=   CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(16000).retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        build.start();
        //创建节点的方式,初始化为空

      //  build.create().forPath("test");
        //创建一个新的节点，节点内容是test,这里和原生的zookeeper的对象序列化方式相同
        build.create().creatingParentsIfNeeded().forPath("/test");
    //    build.create().forPath("/test","test".getBytes());
        //创建一个节点，这里指定是持久化的方式
        build.create().withMode(CreateMode.PERSISTENT).forPath("/test","test".getBytes());
        // 创建带有父节点的节点
        build.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).
                withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath("/test/test01/test01","test01".getBytes());
        build.create().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {

                //异步创建的回调函数
                //todo something;
            }
        }).forPath("/bobo","test".getBytes());
        //删除节点的最简单的版本
        build.delete().forPath("/test");
        //删除父子关系的节点
        build.delete().deletingChildrenIfNeeded().forPath("/test/test01");
        //build
        build.delete().deletingChildrenIfNeeded().withVersion(-1).forPath("/test");
        //强制删除
        build.delete().guaranteed().deletingChildrenIfNeeded().withVersion(-1).forPath("/test/test01");
        //异步删除，带有回调函数
        build.delete().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {

            }
        }).forPath("/test");

//最简单的读取数据
        build.getData().forPath("/test");
        //设置一个状态
        Stat stat = new Stat();
        stat.setVersion(2);
        //获取指定路径下，状态时version为2的数据
        byte[] data = build.getData().storingStatIn(stat).forPath("/test");
        //修改或者是新增数据
        build.setData().inBackground(new BackgroundCallback() {
            // 回调函数
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                CuratorEventType type = event.getType();
                //表示节点已经存在
                if (type == CuratorEventType.EXISTS) {

                }
            }
        }).forPath("/test03").setVersion(1);
        //事件监听
        String path = build.create().forPath("/wuxiaobo");
        NodeCache nodeCache = new NodeCache(build, path, false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                //当节点发生变化的时候这个方法就会被调用
            }
        });

        PathChildrenCache pathChildrenCache = new PathChildrenCache(build, path, false);
        pathChildrenCache.start(true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        //当被添加的时候
                        break;
                    case CHILD_UPDATED:
                        //被修改的时候
                        break;
                    case CHILD_REMOVED:
                        //被移除的时候
                        break;
                }
            }
        });
//master选举，回调函数是在成功获取Master权利的时候会被回调该函数
        LeaderSelector selector = new LeaderSelector(build, path, new LeaderSelectorListenerAdapter() {
            //这个方法一旦执行完毕，Curator就会立即释放Master权利，然后重新开始新一轮的Master选举
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                System.out.println("成为master角色");
                Thread.sleep(3000);
                System.out.println("完成master操作,释放master权利");
            }
        });
        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);

        //分布式锁
       final InterProcessMutex interProcessMutex = new InterProcessMutex(build, path);
       final CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i=0; i< 30; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                        interProcessMutex.acquire();
                    } catch (Exception e) {
                        SimpleDateFormat simpleDateFormat  =new SimpleDateFormat("HH:mm:ss|SSS");
                        String format = simpleDateFormat.format(new Date());
                        System.out.println("订单号是:"+format);
                    }
                    try {
                        interProcessMutex.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        countDownLatch.countDown();

        //分布式计数器,retryNTimes表示最大重试3次，每次重试的时间是1000ms
        DistributedAtomicInteger distributedAtomicInteger = new DistributedAtomicInteger(build, path,
                new RetryNTimes(3, 1000));
        AtomicValue<Integer> add = distributedAtomicInteger.add(8);
        System.out.println("result:" + add);
        //分布式barrier
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("localhost:2181")
                            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
                    try {
                        Thread.sleep(Math.round(Math.random()*3000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    DistributedDoubleBarrier distributedDoubleBarrier = new DistributedDoubleBarrier(curatorFramework, "/test", 5);
                    System.out.println(Thread.currentThread().getName()+"号进入Barrier");
                    build.start();
                    try {
                        distributedDoubleBarrier.enter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("启动....");
                    try {
                        Thread.sleep(Math.round(Math.random()*3000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        distributedDoubleBarrier.leave();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("退出.....");
                }
            }).start();
        }

    }
}
