package com.mine.service;

import com.mine.codec.ObjDecoder;
import com.mine.codec.ObjEncoder;
import com.mine.domain.FileTransferProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 解码编码
//        ch.pipeline().addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
//        ch.pipeline().addLast(new StringDecoder());
//        ch.pipeline().addLast(new StringEncoder());
//        ch.pipeline().addLast("encoder", new ObjectEncoder());
//        ch.pipeline().addLast("decoder",
//                new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        ch.pipeline().addLast(new ObjDecoder(FileTransferProtocol.class));
        ch.pipeline().addLast(new ObjEncoder(FileTransferProtocol.class));
        ch.pipeline().addLast(new ServerHandler());
    }
}
