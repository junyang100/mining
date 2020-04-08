package im;

import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class IMClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8888"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    private static SocketChannel socketChannel;

    public static void main(String[] args) throws Exception {
        new Thread(() -> bind()).start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            sendMsg(scanner.nextLine());
        }
    }

    public static void bind(){
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
        }finally {
            System.out.println("-----client.group.shutdown-----");
            group.shutdownGracefully();
        }
    }

    public static void sendMsg(String msg) {
        socketChannel.writeAndFlush(msg);
    }

}
