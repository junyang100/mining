package com.mine.service;

import com.alibaba.fastjson.JSON;
import com.mine.pojo.IMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    private static Map<String, Channel> clients = new HashMap<>();

    private static AttributeKey<String> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        IMessage iMessage = JSON.parseObject(msg.toString(), IMessage.class);
        System.out.println("server receive message :" + iMessage.toString());
        if (iMessage.getType() == 1) {
            clients.put(iMessage.getSender(), ctx.channel());
            ctx.channel().attr(SESSION_KEY).set(iMessage.getSender());
            ctx.channel().writeAndFlush("login success");
        } else {
            String sender = ctx.channel().attr(SESSION_KEY).get();
            if (StringUtils.isBlank(sender)) {
                ctx.channel().writeAndFlush("need login");
                return;
            }
            Channel toChannel = clients.get(iMessage.getReceiver());
            if (toChannel == null) {
                ctx.channel().writeAndFlush("receiver not online");
                return;
            }
            toChannel.writeAndFlush(iMessage.getMsg());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("有客户端连接：" + ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("有客户端断开：" + ctx.channel().remoteAddress().toString());
        String sender = ctx.channel().attr(SESSION_KEY).get();
        Channel channel = clients.remove(sender);
        if (channel != null) {
            System.out.println(sender + "下线");
        }
    }

    public static String sendMsg(String msg) {
        System.out.println("当前客户端数：" + clients.size());
        for (Map.Entry<String, Channel> entry : clients.entrySet()) {
            entry.getValue().writeAndFlush("pushmsg=" + msg);
        }
        return "success";
    }
}
