package im;

import com.alibaba.fastjson.JSON;
import com.mine.proto.IMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by admin on 2019/3/22.
 */
public class IMHandler extends ChannelInboundHandlerAdapter {

    private IMClient client;

    public IMHandler(IMClient client) {
        this.client = client;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                // write heartbeat to server
                IMessage.Message iMessage = IMessage.Message.newBuilder()
                        .setType(3).setSender("idle").setMsg("idle").build();
                ctx.writeAndFlush(iMessage);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        final EventLoop eventLoop = ctx.channel().eventLoop();
        boolean rs = false;
        while (!rs){
            try {
                System.out.println("断线重连");
                client.doConnect();
                Thread.sleep(3000);
                //todo 断线重连后的登陆问题 是否需要重新登陆
//                IMessage loginMsg = new IMessage();
//                loginMsg.setType((byte) 1);
//                loginMsg.setSender("client" + new Random().nextInt());
//                client.sendMsg(JSON.toJSONString(loginMsg));
                rs=true;
            } catch (Exception e) {
                System.out.println("reconnection error");
            }
            Thread.sleep(3000);
        }
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
