package im;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by admin on 2019/3/22.
 */
public class IMHandler extends ChannelInboundHandlerAdapter {

    private IMClient client;

    public IMHandler(IMClient client) {
        this.client = client;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        try {
            System.out.println("client read="+obj.toString());
//            ByteBuf msg;
//            File f = new File("d:/1.txt");
//            System.out.println(f.getName());
//            InputStream in = new FileInputStream(f);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024 * 100];
//            int n = 0;
//            while ((n = in.read(buffer)) != -1) {
//                msg = Unpooled.buffer(buffer.length);
//                //这里读取到多少，就发送多少，是为了防止最后一次读取没法满填充buffer，
//                //导致将buffer中的处于尾部的上一次遗留数据也发送走
//                msg.writeBytes(buffer, 0, n);
//                ctx.writeAndFlush(msg);
//                msg.clear();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        try {
//            System.out.println("send file path=" + "d:/1.txt");
//            ByteBuf msg;
//            InputStream in = new FileInputStream("d:/1.txt");
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024 * 100];
//            int n = 0;
//            while ((n = in.read(buffer)) != -1) {
//                msg = Unpooled.buffer(buffer.length);
//                //这里读取到多少，就发送多少，是为了防止最后一次读取没法满填充buffer，
//                //导致将buffer中的处于尾部的上一次遗留数据也发送走
//                msg.writeBytes(buffer, 0, n);
//                ctx.channel().writeAndFlush(msg);
//                msg.clear();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleState state = ((IdleStateEvent) evt).state();
//            if (state == IdleState.WRITER_IDLE) {
//                // write heartbeat to server
//                IMessage.Message iMessage = IMessage.Message.newBuilder()
//                        .setType(3).setSender("idle").setMsg("idle").build();
//                ctx.writeAndFlush(iMessage);
//            }
//        } else {
//            super.userEventTriggered(ctx, evt);
//        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        final EventLoop eventLoop = ctx.channel().eventLoop();
//        boolean rs = false;
//        while (!rs){
//            try {
//                System.out.println("断线重连");
//                client.doConnect();
//                Thread.sleep(3000);
//                //todo 断线重连后的登陆问题 是否需要重新登陆
////                IMessage loginMsg = new IMessage();
////                loginMsg.setType((byte) 1);
////                loginMsg.setSender("client" + new Random().nextInt());
////                client.sendMsg(JSON.toJSONString(loginMsg));
//                rs=true;
//            } catch (Exception e) {
//                System.out.println("reconnection error");
//            }
//            Thread.sleep(3000);
//        }
//        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
