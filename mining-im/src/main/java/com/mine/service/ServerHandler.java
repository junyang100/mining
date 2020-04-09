package com.mine.service;

import com.alibaba.fastjson.JSON;
import com.mine.pojo.IMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.util.ArrayList;
import java.util.List;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    private static List<Channel> clients = new ArrayList();

    public static final AttributeKey<String> SESSION_KEY =
            AttributeKey.valueOf("SESSION_KEY");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        IMessage iMessage = JSON.parseObject(msg.toString(),IMessage.class);
        System.out.println("server receive message :" + iMessage.toString());
        if (iMessage.getType() == 1) {
            ctx.channel().attr(SESSION_KEY).set(iMessage.getSessionId());
            clients.add(ctx.channel());
            ctx.channel().writeAndFlush("login success");
        } else {
            for (Channel channel:clients) {
                String sessionId = channel.attr(SESSION_KEY).get();
                if (!iMessage.getSessionId().equals(sessionId)) {
                    channel.writeAndFlush(iMessage.getMsg());
                }
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("有客户端连接：" + ctx.channel().remoteAddress().toString());
//        clients.add(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有客户端断开：" + ctx.channel().remoteAddress().toString());
        clients.remove(ctx);
    }

    public static String sendMsg(String msg) {
        System.out.println("当前客户端数：" + clients.size());
        for (Channel c : clients) {
            c.writeAndFlush("server send=" + msg);
        }
        return "success";
    }
}
