package im;

import com.alibaba.fastjson.JSON;
import com.mine.pojo.IMessage;
import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class IMClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8888"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    private static SocketChannel socketChannel;

    public static void main(String[] args) throws Exception {
        new Thread(() -> bind()).start();
        //login
        String sessionId = UUID.randomUUID().toString();
        IMessage loginMsg = new IMessage();
        loginMsg.setType((byte) 1);
        loginMsg.setSessionId(sessionId);
        Thread.sleep(1000);
        sendMsg(JSON.toJSONString(loginMsg));
        //chat
        Scanner scanner = new Scanner(System.in);
        loginMsg.setType((byte) 2);
        while (true) {
            loginMsg.setMsg(scanner.nextLine());
            sendMsg(JSON.toJSONString(loginMsg));
        }
    }

    public static void bind() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("decoder", new StringDecoder());
                            p.addLast("encoder", new StringEncoder());
                            p.addLast(new IMHandler());
                        }
                    });
            ChannelFuture future = b.connect(HOST, PORT).sync();
//            future.channel().writeAndFlush("Hello Netty Server ,I am a common client");
            socketChannel = (SocketChannel) future.channel();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("-----client.group.shutdown-----");
            group.shutdownGracefully();
        }
    }

    public static void sendMsg(String msg) {
        socketChannel.writeAndFlush(msg);
    }

}
