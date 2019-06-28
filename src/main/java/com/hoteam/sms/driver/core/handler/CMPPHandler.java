package com.hoteam.sms.driver.core.handler;

import com.alibaba.fastjson.JSONObject;
import com.hoteam.sms.driver.core.SmsCache;
import com.hoteam.sms.driver.core.coder.cmpp.CMPPCallback;
import com.hoteam.sms.driver.domain.CmppDeliver;
import com.hoteam.sms.driver.domain.response.CmppSubmitResp;
import com.hoteam.sms.driver.util.MessageTool;
import com.hoteam.sms.driver.util.Sms;
import com.hoteam.sms.driver.util.SmsResult;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


/**
 * 主要业务 handler
 *
 * @author weixiang
 * @date 2018/8/2 15:37
 */

public class CMPPHandler extends SimpleChannelInboundHandler {

    private final static Logger logger = LoggerFactory.getLogger(CMPPHandler.class);

    private final CMPPCallback callback;

    public CMPPHandler(CMPPCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object message) throws Exception {
        if (message instanceof CmppSubmitResp) {
            CmppSubmitResp response = (CmppSubmitResp) message;
            logger.info("sms {} submit to gateway with result: {}", response.getMsgId(), response.getState() == 0);
            if (null != callback) {
                this.callback.onSubmit(buildSubmitResult(response));
            }
        } else if (message instanceof CmppDeliver) {
            CmppDeliver response = (CmppDeliver) message;
            // 是否为状态报告 0：非状态报告1：状态报告
            if (response.getRegistered_Delivery() == 1) {
                logger.info("sms {} deliver to {} with result: {}", response.getMsg_Id_DELIVRD(), response.getDest_terminal_Id(),
                        response.getStat());
                if (null != callback) {
                    this.callback.onDeliver(buildDeliverResult(response));
                }
            } else {
                //用户回复会打印在这里
                logger.info("message {} receive user {} response: {} ", MessageTool.bytesToLong(response.getMsg_Id()), response
                        .getSrc_terminal_Id(), response.getMsg_Content());
            }
        } else {
            logger.info("receive unknown message : {}", JSONObject.toJSON(message));
        }
    }

    private SmsResult buildSubmitResult(CmppSubmitResp response) {
        Sms cache = SmsCache.instance().pop(Optional.of(response.getSequenceId()));
        Sms sms;
        if (null == cache) {
            sms = new Sms(response.getMsgId(), "未知号码", "未知内容");
        } else {
            sms = new Sms(response.getMsgId(), cache.getMobile(), cache.getContent());
        }
        String message = buildMessage(response.getState());
        if (response.getState() == 0) {
            return new SmsResult(true, message, sms);
        } else {
            return new SmsResult(false, message, sms);
        }
    }

    private SmsResult buildDeliverResult(CmppDeliver deliver) {
        Sms sms = new Sms(deliver.getMsg_Id_DELIVRD(), deliver.getDest_terminal_Id(), null);
        String message = buildMessage(deliver.getResult());
        if (deliver.getResult() == 0) {
            return new SmsResult(true, message, sms);
        } else {
            return new SmsResult(false, message, sms);
        }
    }


    private String buildMessage(int state) {
        String message;
        switch (state) {
            case 0:
                message = "成功";
                break;
            case 1:
                message = "消息结构错误";
                break;
            case 2:
                message = "命令错误";
                break;
            case 3:
                message = "消息序号重复";
                break;
            case 4:
                message = "消息长度错误";
                break;
            case 5:
                message = "资费代码错误";
                break;
            case 6:
                message = "超过最大信息长度";
                break;
            case 7:
                message = "业务代码错误";
                break;
            case 8:
                message = "流量控制错误";
                break;
            case 9:
                message = "其他错误";
                break;
            default:
                message = "未知错误";
                break;
        }
        return message;
    }
}
