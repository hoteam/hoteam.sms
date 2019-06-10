package com.hoteam.sms.driver.core.coder.cmpp;


import com.hoteam.sms.driver.core.Command;
import com.hoteam.sms.driver.domain.CmppDeliver;
import com.hoteam.sms.driver.domain.response.CmppActiveTestResp;
import com.hoteam.sms.driver.domain.response.CmppConnectResp;
import com.hoteam.sms.driver.domain.response.CmppSubmitResp;
import com.hoteam.sms.driver.utils.MessageTool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * 解码
 *
 * @author weixiang
 * @date 2018/8/2 15:37
 */
public class CMPPDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        //字节数组
        byte[] buf = new byte[byteBuf.readableBytes()];
        //读取数据到字节数组
        byteBuf.readBytes(buf);

        //开始解析数据,先提取出长度字段标识长度的数据,也就是该条消息
        //4位 消息长度
        int totalLength = MessageTool.bytesToInt(ArrayUtils.subarray(buf, 0, 4));
        //获取到该长度的字节数组
        byte[] bytes = ArrayUtils.subarray(buf, 0, totalLength);

        //获取到响应类型,也就是哪个接口的响应,4位
        int commandId = MessageTool.bytesToInt(ArrayUtils.subarray(bytes, 4, 8));

        //连接请求响应
        switch (commandId) {
            case Command.CMPP_ACTIVE_TEST:
                System.out.println("-----------------接收到链路检测请求-----------------");
                channelHandlerContext.writeAndFlush(new CmppActiveTestResp());
                break;
            case Command.CMPP_ACTIVE_TEST_RESP:
                System.out.println("--------------接收到链路检测应答--------------");
                break;
            case Command.CMPP_DELIVER:
                System.out.println("-------------短信发送状态报告---------------");
                CmppDeliver deliver = new CmppDeliver(bytes);
                list.add(deliver);
                break;
            case Command.CMPP_SUBMIT_RESP:
                System.out.println("-------------接收到短信提交应答-------------");
                System.out.println(bytes.length);
                CmppSubmitResp submitResp = new CmppSubmitResp(bytes);
                list.add(submitResp);
                break;
            case Command.CMPP_QUERY_RESP:
                System.out.println("-------------接收到短信查询应答-------------");
                break;
            case Command.CMPP_CONNECT_RESP:
                System.out.println("-------------请求连接短信网关应答-------------");
                //服务器端告诉客户端已接受你的连接
                CmppConnectResp connectResp = new CmppConnectResp(bytes);
                System.out.println("连接状态：" + connectResp.getStatus());
                break;
            default:
                System.out.println("暂无解析" + commandId);
                break;
        }
        //list.add(commandId);
    }
}