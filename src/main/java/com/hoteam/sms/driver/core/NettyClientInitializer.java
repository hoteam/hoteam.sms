package com.hoteam.sms.driver.core;

import com.hoteam.sms.driver.core.coder.cmpp.CMPPCallback;
import com.hoteam.sms.driver.core.coder.cmpp.CMPPDecoder;
import com.hoteam.sms.driver.core.coder.cmpp.CMPPEncoder;
import com.hoteam.sms.driver.core.handler.CMPPHandler;
import com.hoteam.sms.driver.core.handler.HeartHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    private final NettyClient client;

    private final CMPPCallback callback;

    public NettyClientInitializer(NettyClient client, CMPPCallback callback) {
        this.client = client;
        this.callback = callback;
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        //长度编码器，防止粘包拆包
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, -4, 0, true));
        //pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,0));

        //心跳
        //readerIdleTime:为读超时间(即测试端一定时间内未接收到被测试端消息);
        //writerIdleTime:为写超时间（即测试端一定时间内向被测试端发送消息);
        //allIdeTime：所有类型的超时时间
        pipeline.addLast("idleState handler", new IdleStateHandler(0, 0, 20, TimeUnit.SECONDS));
        //心跳包
        pipeline.addLast("heart handler", new HeartHandler(client));
        //解析
        pipeline.addLast("encoder", new CMPPEncoder());
        pipeline.addLast("decoder", new CMPPDecoder());
        //客户端的逻辑
        pipeline.addLast("cmpp handler", new CMPPHandler(callback));
    }
}
