package com.mine.service;

import com.alibaba.fastjson.JSON;
import com.mine.domain.*;
import com.mine.utils.CacheUtil;
import com.mine.utils.FileUtil;
import com.mine.utils.MsgUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    private static Map<String, Channel> clients = new HashMap<>();

    private static AttributeKey<String> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //数据格式验证
        if (!(msg instanceof FileTransferProtocol)) return;

        FileTransferProtocol fileTransferProtocol = (FileTransferProtocol) msg;
        //0传输文件'请求'、1文件传输'指令'、2文件传输'数据'
        switch (fileTransferProtocol.getTransferType()) {
            case 0:
                FileDescInfo fileDescInfo = (FileDescInfo) fileTransferProtocol.getTransferObj();

                //断点续传信息，实际应用中需要将断点续传信息保存到数据库中
                FileBurstInstruct fileBurstInstructOld = CacheUtil.burstDataMap.get(fileDescInfo.getFileName());
                if (null != fileBurstInstructOld) {
                    if (fileBurstInstructOld.getStatus() == Constants.FileStatus.COMPLETE) {
                        CacheUtil.burstDataMap.remove(fileDescInfo.getFileName());
                    }
                    //传输完成删除断点信息
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " bugstack虫洞栈服务端，接收客户端传输文件请求[断点续传]。" + JSON.toJSONString(fileBurstInstructOld));
                    ctx.writeAndFlush(MsgUtil.buildTransferInstruct(fileBurstInstructOld));
                    return;
                }

                //发送信息
                FileTransferProtocol sendFileTransferProtocol = MsgUtil.buildTransferInstruct(Constants.FileStatus.BEGIN, fileDescInfo.getFileUrl(), 0);
                ctx.writeAndFlush(sendFileTransferProtocol);
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " bugstack虫洞栈服务端，接收客户端传输文件请求。" + JSON.toJSONString(fileDescInfo));
                break;
            case 2:
                FileBurstData fileBurstData = (FileBurstData) fileTransferProtocol.getTransferObj();
                FileBurstInstruct fileBurstInstruct = FileUtil.writeFile("E://netty", fileBurstData);

                //保存断点续传信息
                CacheUtil.burstDataMap.put(fileBurstData.getFileName(), fileBurstInstruct);

                ctx.writeAndFlush(MsgUtil.buildTransferInstruct(fileBurstInstruct));
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " bugstack虫洞栈服务端，接收客户端传输文件数据。" + JSON.toJSONString(fileBurstData));

                //传输完成删除断点信息
                if (fileBurstInstruct.getStatus() == Constants.FileStatus.COMPLETE) {
                    CacheUtil.burstDataMap.remove(fileBurstData.getFileName());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleState state = ((IdleStateEvent) evt).state();
//            if (state == IdleState.READER_IDLE) {
//                ctx.channel().close();
//            }
//        } else {
//            super.userEventTriggered(ctx, evt);
//        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("有客户端连接：" + ctx.channel().remoteAddress().toString());
        ctx.channel().writeAndFlush("connect success");
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
