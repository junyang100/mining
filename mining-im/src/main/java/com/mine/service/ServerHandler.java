package com.mine.service;

import com.mine.proto.IMessage;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.Timeout;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    private static Map<String, Channel> clients = new HashMap<>();

    private static AttributeKey<String> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        IMessage.Message iMessage = (IMessage.Message)msg;
        System.out.println("server receive message :" + iMessage.toString());
        if (iMessage.getType() == 1) {
            clients.put(iMessage.getSender(), ctx.channel());
            ctx.channel().attr(SESSION_KEY).set(iMessage.getSender());
            ctx.channel().writeAndFlush("login success");
        } else if (iMessage.getType() == 3) {
            //do nothing
            System.out.println("idle request");
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
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("有客户端连接：" + ctx.channel().remoteAddress().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
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
