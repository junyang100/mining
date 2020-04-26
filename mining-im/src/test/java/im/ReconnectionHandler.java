//package im;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.*;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;
//import io.netty.handler.timeout.IdleStateHandler;
//import io.netty.util.Timeout;
//
//import java.nio.channels.SocketChannel;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by admin on 2020/4/22.
// */
//public class ReconnectionHandler extends ChannelInboundHandlerAdapter {
//
//    private Bootstrap bootstrap;
//
//    private int attempts;
//
//    private volatile boolean reconnect = true;
//
//    private String host;
//
//    private int port;
//
//    public ReconnectionHandler(Bootstrap bootstrap, String host, int port) {
//        this.bootstrap = bootstrap;
//        this.host = host;
//        this.port = port;
//    }
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
//        System.out.println("当前链路已经激活了，重连尝试次数重新置为0");
//        attempts = 0;
//        ctx.fireChannelActive();
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("链接关闭");
//        if (reconnect) {
//            System.out.println("链接关闭，将进行重连");
//            if (attempts < 12) {
//                attempts++;
//                //重连的间隔时间会越来越长
//                int timeout = 2 << attempts;
//                Thread.sleep(timeout * 1000);
//                try {
//                    connect();
//                } catch (Exception e) {
//                    System.out.println(e);
//                }
//            }
//        }
//        ctx.fireChannelInactive();
//    }
//
//    public void connect() throws Exception {
//
//        ChannelFuture future;
//        //bootstrap已经初始化好了，只需要将handler填入就可以了
//        synchronized (bootstrap) {
//            bootstrap.handler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
//                @Override
//                public void initChannel(io.netty.channel.socket.SocketChannel ch) throws Exception {
//                    ChannelPipeline p = ch.pipeline();
//                    p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
//                    p.addLast("decoder", new StringDecoder());
//                    p.addLast("encoder", new StringEncoder());
//                    p.addLast(new ReconnectionHandler(bootstrap, host, port));
//                    p.addLast(new IMHandler());
//                }
//            });
//            future = bootstrap.connect(host, port);
//        }
//        //future对象
//        future.addListener(new ChannelFutureListener() {
//
//            public void operationComplete(ChannelFuture f) throws Exception {
//                boolean succeed = f.isSuccess();
//
//                //如果重连失败，则调用ChannelInactive方法，再次出发重连事件，一直尝试12次，如果失败则不再重连
//                if (!succeed) {
//                    System.out.println("重连失败");
//                    f.channel().pipeline().fireChannelInactive();
//                } else {
//                    System.out.println("重连成功");
//                }
//            }
//        });
//
//    }
//}
