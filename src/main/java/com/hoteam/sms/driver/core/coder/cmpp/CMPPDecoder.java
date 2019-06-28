package com.hoteam.sms.driver.core.coder.cmpp;


import com.hoteam.sms.driver.core.Command;
import com.hoteam.sms.driver.domain.CmppDeliver;
import com.hoteam.sms.driver.domain.response.CmppActiveTestResp;
import com.hoteam.sms.driver.domain.response.CmppConnectResp;
import com.hoteam.sms.driver.domain.response.CmppSubmitResp;
import com.hoteam.sms.driver.util.MessageTool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 解码
 *
 * @author weixiang
 * @date 2018/8/2 15:37
 */
public class CMPPDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMPPDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuffer, List<Object> list) {
        //字节数组
        byte[] buffer = new byte[byteBuffer.readableBytes()];
        //读取数据到字节数组
        byteBuffer.readBytes(buffer);

        //开始解析数据,先提取出长度字段标识长度的数据,也就是该条消息
        //4位 消息长度
        int totalLength = MessageTool.bytesToInt(ArrayUtils.subarray(buffer, 0, 4));
        //获取到该长度的字节数组
        byte[] bytes = ArrayUtils.subarray(buffer, 0, totalLength);

        //获取到响应类型,也就是哪个接口的响应,4位
        int commandId = MessageTool.bytesToInt(ArrayUtils.subarray(bytes, 4, 8));

        //连接请求响应
        switch (commandId) {
            case Command.CMPP_ACTIVE_TEST:
                LOGGER.debug("receive active test");
                channelHandlerContext.writeAndFlush(new CmppActiveTestResp());
                break;
            case Command.CMPP_ACTIVE_TEST_RESP:
                LOGGER.debug("receive active test response");
                break;
            case Command.CMPP_DELIVER:
                CmppDeliver deliver = new CmppDeliver(bytes);
                LOGGER.debug("receive deliver state response with sequenceId :{} target : {} state: {}",
                        MessageTool.bytesToInt(deliver.getMsg_Id()), deliver.getDest_terminal_Id(), deliver.getStat());
                list.add(deliver);
                break;
            case Command.CMPP_SUBMIT_RESP:
                CmppSubmitResp submitResponse = new CmppSubmitResp(bytes);
                LOGGER.debug("receive message submit response width sequenceId: {} state : {}",
                        submitResponse.getMsgId(), submitResponse.getState());
                list.add(submitResponse);
                break;
            case Command.CMPP_QUERY_RESP:
                LOGGER.debug("receive message query response");
                break;
            case Command.CMPP_CONNECT_RESP:
                CmppConnectResp connectResponse = new CmppConnectResp(bytes);
                LOGGER.debug("receive gateway connect response with state : {}", connectResponse.getStatus());
                break;
            default:
                LOGGER.debug("receive unknown message");
                break;
        }
    }
}
