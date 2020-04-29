package com.mine.service;

import com.mine.proto.IMessage;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2020/4/8.
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 解码编码
        ch.pipeline().addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
        ch.pipeline().addLast(new ProtobufDecoder(IMessage.Message.getDefaultInstance()));
        ch.pipeline().addLast(new StringEncoder());
        ch.pipeline().addLast(new ServerHandler());
    }
}
