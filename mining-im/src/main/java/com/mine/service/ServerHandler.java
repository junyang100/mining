package com.mine.service;

import com.alibaba.fastjson.JSON;
import com.mine.pojo.IMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    private static Map<String, Channel> clients = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        IMessage iMessage = JSON.parseObject(msg.toString(), IMessage.class);
        System.out.println("server receive message :" + iMessage.toString());
        if (iMessage.getType() == 1) {
            clients.put(iMessage.getSessionId(), ctx.channel());
            ctx.channel().writeAndFlush("login success");
        } else {
            for (Map.Entry<String, Channel> entry : clients.entrySet()) {
                if (!iMessage.getSessionId().equals(entry.getKey())) {
                    entry.getValue().writeAndFlush(iMessage.getMsg());
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
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("有客户端断开：" + ctx.channel().remoteAddress().toString());
        clients.remove(ctx);
    }

    public static String sendMsg(String msg) {
        System.out.println("当前客户端数：" + clients.size());
        for (Map.Entry<String, Channel> entry : clients.entrySet()) {
            entry.getValue().writeAndFlush("pushmsg=" + msg);
        }
        return "success";
    }
}
