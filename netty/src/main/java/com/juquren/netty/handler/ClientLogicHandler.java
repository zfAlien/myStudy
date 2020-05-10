package com.juquren.netty.handler;

import org.jboss.netty.channel.*;

public class ClientLogicHandler extends SimpleChannelHandler {
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("客户端连接成功!");
//        String str = "hi server!";
//        e.getChannel().write(str);//异步
    }

    @Override
    public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) throws Exception {
        System.out.println("客户端写消息完成");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String msg = (String) e.getMessage();
        System.out.println("客户端接收到消息, msg: " + msg);
//        ctx.getChannel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception{
        System.out.println("client断开连接了");
//        super.channelDisconnected(ctx,e);
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("client关闭了");
//        super.channelClosed(ctx,e);
    }
}
