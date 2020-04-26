package im;

import com.alibaba.fastjson.JSON;
import com.mine.pojo.IMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class IMClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8888"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    private SocketChannel socketChannel;
    private NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
    private Channel channel;
    private Bootstrap bootstrap;

    public static void main(String[] args) throws Exception {
        IMClient client = new IMClient();
        new Thread(() -> client.bind()).start();
        Thread.sleep(2000);
        //login
        IMessage loginMsg = new IMessage();
        loginMsg.setType((byte) 1);
        loginMsg.setSender("client" + new Random().nextInt());
        client.sendMsg(JSON.toJSONString(loginMsg));
//        Thread.sleep(2000);
        //chat
        Scanner scanner = new Scanner(System.in);
        loginMsg.setType((byte) 2);
        while (true) {
            String line[] = scanner.nextLine().split(":");
            loginMsg.setReceiver(line[0]);
            loginMsg.setMsg(line[1]);
            client.sendMsg(JSON.toJSONString(loginMsg));
        }
    }

    public void bind() {
        System.out.println("bind start");
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                            p.addLast("decoder", new StringDecoder());
                            p.addLast("encoder", new StringEncoder());
//                            p.addLast(new ReconnectionHandler(b, HOST, PORT));
                            p.addLast(new IMHandler(IMClient.this));
                        }
                    });
            doConnect();
        } catch (Exception e) {
            System.out.println(e);
        }
//        finally {
//            System.out.println("-----client.group.shutdown-----");
//            group.shutdownGracefully();
//        }
    }

    public void doConnect() throws InterruptedException {
        if (channel != null && channel.isActive()) {
            return;
        }
        ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
        socketChannel = (SocketChannel) future.channel();
        future.channel().closeFuture().sync();
    }

    public void sendMsg(String msg) {
        socketChannel.writeAndFlush(msg);
    }

}
