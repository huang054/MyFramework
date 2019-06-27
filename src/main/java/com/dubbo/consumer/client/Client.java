package com.dubbo.consumer.client;




import com.dubbo.common.Msg;
import com.dubbo.consumer.myhandler.MyHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Client {

    public static <T> T createProxy(Class<?> clazz){
        MethodProxy methodProxy = new MethodProxy(clazz);
        T result = (T)Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},methodProxy);

        return result;
    }


}
class MethodProxy implements InvocationHandler{

    private Class<?> clazz;

    public  MethodProxy(Class<?> clazz){
        this.clazz=clazz;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Msg m = new Msg(clazz.getName(),method.getName(),method.getParameterTypes(),args);
        if(Object.class.equals(method.getDeclaringClass())){
            return method.invoke(this,args);
        }else{
         return myRpcInvoke(m);
        }

    }

    protected Object myRpcInvoke(Msg msg) throws NoSuchMethodException {
       final MyHandler myHandler = new MyHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap bootstarp = new Bootstrap();
            bootstarp.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new LengthFieldPrepender(4));
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));



                            ch.pipeline().addLast("encoder",new ObjectEncoder());
                            ch.pipeline().addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                            //最后处理自己的逻辑
                            ch.pipeline().addLast(myHandler);
                        }
                    });
           ChannelFuture f= bootstarp.connect("127.0.0.1",8000).sync();

           f.channel().writeAndFlush(msg).sync();

          f.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {

            group.shutdownGracefully();
        }
        if(myHandler.getValue()==null){
            throw new NoSuchMethodException("没有这个方法");
        }
        return myHandler.getValue();
    }
}