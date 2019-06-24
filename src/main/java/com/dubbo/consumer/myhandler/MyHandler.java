package com.dubbo.consumer.myhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MyHandler extends ChannelInboundHandlerAdapter {
    private Object value;
    public Object getValue(){
        return this.value;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

       this.value=msg;

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();
    }
}
