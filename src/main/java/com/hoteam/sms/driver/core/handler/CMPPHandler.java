package com.hoteam.sms.driver.core.handler;

import com.hoteam.sms.driver.domain.CmppDeliver;
import com.hoteam.sms.driver.domain.response.CmppSubmitResp;
import com.hoteam.sms.driver.utils.MessageTool;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 主要业务 handler
 *
 * @author weixiang
 * @date 2018/8/2 15:37
 */

public class CMPPHandler extends SimpleChannelInboundHandler {

    private final static Logger logger = LoggerFactory.getLogger(CMPPHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof CmppSubmitResp) {
            CmppSubmitResp resp = (CmppSubmitResp) msg;
            logger.info("message {} submit to gateway {}", resp.getMsgId(), resp.getState() == 0);
        }
        if (msg instanceof CmppDeliver) {
            CmppDeliver resp = (CmppDeliver) msg;
            // 是否为状态报告 0：非状态报告1：状态报告
            if (resp.getRegistered_Delivery() == 1) {
                logger.info("message {} deliver to {} by state: {}", resp.getMsg_Id(), resp.getDest_terminal_Id(), resp
                        .getStat());
            } else {
                //用户回复会打印在这里
                logger.info("message {} receive user {} response: {} ", MessageTool.bytesToLong(resp.getMsg_Id()), resp
                        .getSrc_terminal_Id(), resp.getMsg_Content());
            }
        }
    }

}
