package im;

import com.mine.codec.ObjDecoder;
import com.mine.codec.ObjEncoder;
import com.mine.domain.FileBurstData;
import com.mine.domain.FileTransferProtocol;
import com.mine.utils.FileUtil;
import com.mine.utils.MsgUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class IMClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8888"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    private SocketChannel socketChannel;
    private NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
    private Channel channel;
    private Bootstrap bootstrap;
    private ChannelHandlerContext ctx;

    public static void main(String[] args) throws Exception {
        IMClient client = new IMClient();
        new Thread(() -> client.bind()).start();
        Thread.sleep(2000);
        //login
//        IMessage.Message iMessage = IMessage.Message.newBuilder()
//                .setType(1).setSender("client" + new Random().nextInt()).build();
////        IMessage loginMsg = new IMessage();
////        loginMsg.setType((byte) 1);
////        loginMsg.setSender("client" + new Random().nextInt());
//        client.sendMsg(iMessage);
//        Thread.sleep(2000);
        //chat
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
//            IMessage.Message chatMsg = IMessage.Message.newBuilder()
//                    .setType(2).setSender(iMessage.getSender())
//                    .setReceiver(line[0]).setMsg(line[1]).build();
//            loginMsg.setReceiver(line[0]);
//            loginMsg.setMsg(line[1]);
            System.out.println("user input=" + line);
            client.sendFile(line);
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
//                            p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
//                            p.addLast("decoder", new StringDecoder());
//                            p.addLast("encoder", new StringEncoder());
//                            p.addLast(new ReconnectionHandler(b, HOST, PORT));
//                            p.addLast("encoder", new ObjectEncoder());
//                            p.addLast("decoder",
//                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
//                            p.addLast(new LoggingHandler(LogLevel.DEBUG));
                            //对象传输处理
                            p.addLast(new ObjDecoder(FileTransferProtocol.class));
                            p.addLast(new ObjEncoder(FileTransferProtocol.class));
                            p.addLast(new IMHandler(IMClient.this));
                        }
                    });
            if (channel != null && channel.isActive()) {
                return;
            }
            ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
            socketChannel = (SocketChannel) future.channel();
            future.channel().closeFuture().sync();
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
        socketChannel.closeFuture().sync();
    }

    public void sendMsg(String msg) {
        socketChannel.writeAndFlush(msg);
    }

    public void sendFile(String path) {
        FileBurstData fileBurstData = null;
        try {
            fileBurstData = FileUtil.readFile(path, 0);
            socketChannel.writeAndFlush(MsgUtil.buildTransferData(fileBurstData));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        byte[] buffer = new byte[1024 * 100];
//
//        try {
//            System.out.println("send file path=" + path);
////            msg.writeBytes("file demo".getBytes());
////            socketChannel.writeAndFlush(msg);
//            File f = new File("d:/141727.hprof.gz");
//            System.out.println(f.getName());
//            InputStream in = new FileInputStream(f);
//            int n = 0;
//
//
//            ByteBuf msg = Unpooled.buffer(buffer.length);
//            while ((n = in.read(buffer)) != -1) {
//                //这里读取到多少，就发送多少，是为了防止最后一次读取没法满填充buffer，
//                //导致将buffer中的处于尾部的上一次遗留数据也发送走
//                msg.writeBytes(buffer, 0, n);
//                System.out.println("n=" + n);
//                ChannelFuture future = socketChannel.writeAndFlush(msg);
////                future.addListener(new ChannelFutureListener() {
////                    @Override
////                    public void operationComplete(ChannelFuture future) throws Exception {
////                        if(future.isSuccess()){
////                            msg.clear();
////                        }
////                    }
////                });
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

}
