package com.hoteam.sms.driver.core.handler;


import com.hoteam.sms.driver.core.NettyClient;
import com.hoteam.sms.driver.domain.CmppActiveTest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 心跳Handler
 *
 * @author weixiang
 * @date 2018/8/2 15:37
 */
public class HeartHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(HeartHandler.class);
    private NettyClient client;

    public HeartHandler(NettyClient client) {
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("start to init connection ...");
        super.channelActive(ctx);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            IdleState state = event.state();
            if (state == IdleState.WRITER_IDLE || state == IdleState.ALL_IDLE) {
                client.submit(new CmppActiveTest());
                logger.info("heartbeat from sp to gateway start !!!");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.warn("connection is lost V_V,start to reconnect !!!");
        client.reConnect(10);
    }
}
