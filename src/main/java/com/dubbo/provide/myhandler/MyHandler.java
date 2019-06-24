package com.dubbo.provide.myhandler;

import com.dubbo.common.Msg;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;


public class MyHandler extends ChannelInboundHandlerAdapter {
    private Map<String,Object> map;
    public  MyHandler(Map<String,Object> map){
        this.map=map;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();
        Msg m = (Msg)msg;
        if (map.containsKey(m.getClassName())){
            Object clazz =map.get(m.getClassName());
            result=clazz.getClass().getMethod(m.getMethodName(),m.getParames()).invoke(clazz,m.getParamesValue());
        }

        ctx.channel().writeAndFlush(result).addListener(ChannelFutureListener.CLOSE);;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();
       //ctx.close();
    }
}
