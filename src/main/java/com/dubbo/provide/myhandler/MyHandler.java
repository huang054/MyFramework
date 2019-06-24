package com.dubbo.provide.myhandler;

import com.dubbo.common.Msg;
import com.dubbo.provide.server.Bootstarp;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class MyHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();
        Msg m = (Msg)msg;
        if (Bootstarp.getRegistrys().containsKey(m.getClassName())){
            Object clazz =Bootstarp.getRegistrys().get(m.getClassName());
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
