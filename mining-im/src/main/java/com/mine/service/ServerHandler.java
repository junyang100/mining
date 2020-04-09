package com.mine.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    private static List<ChannelHandlerContext> clients = new ArrayList();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server receive message :" + msg);
        for (ChannelHandlerContext c : clients) {
            if (!c.equals(ctx)) {
                c.channel().writeAndFlush("server send=" + msg);
            }
        }
//        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("有客户端连接：" + ctx.channel().remoteAddress().toString());
        clients.add(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有客户端断开：" + ctx.channel().remoteAddress().toString());
        clients.remove(ctx);
    }

    public static String sendMsg(String msg) {
        System.out.println("当前客户端数：" + clients.size());
        for (ChannelHandlerContext c : clients) {
            c.channel().writeAndFlush("server send=" + msg);
        }
        return "success";
    }
}
