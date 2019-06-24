package com.dubbo.provide.server;

import com.dubbo.provide.myhandler.MyHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Bootstarp {

    private static Map<String,Object> registrys = new ConcurrentHashMap<>();

    private static List<String> cacheNames= new ArrayList<>();

    public  static Map<String, Object> getRegistrys() {
        return registrys;
    }



    public static List<String> getCacheNames() {
        return cacheNames;
    }


    private int port;

    public Bootstarp(int port){
        this.port=port;
    }

    public void start()throws Exception{
        scan("com.dubbo.api.Impl");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                     .option(ChannelOption.SO_BACKLOG,128).childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)  {
                            ch.pipeline().addLast(new LengthFieldPrepender(4));
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));



                            ch.pipeline().addLast("encoder",new ObjectEncoder());
                            ch.pipeline().addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                            //最后处理自己的逻辑
                            ch.pipeline().addLast(new MyHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //绑定服务端口
            ChannelFuture f = b.bind(port).sync();


            System.out.println("监听端口:"+port);
            //开始接收客户
            f.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }
    }

     protected  void scan(String packageName){
         URL url =this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.","/"));
         File dir = new File(url.getFile());
         for (File file :dir.listFiles()){
             if(file.isDirectory()){
                 scan(packageName+"."+file.getName());
             }else{
                cacheNames.add(packageName+"."+file.getName().replace(".class","").trim());
             }
         }
         Resigest();
     }

     protected  void  Resigest(){
        if(cacheNames.size()==0) {
            return;
        }
        for (String className:cacheNames){
            try {
                Class<?> clazz =Class.forName(className);
                Class<?> interfaces = clazz.getInterfaces()[0];
                registrys.put(interfaces.getName(),clazz.newInstance());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

     }
    public static void main(String[] args) throws Exception{
        new Bootstarp(8000).start();
    }
}
