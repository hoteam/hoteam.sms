package com.hoteam.sms.driver.domain;

import com.hoteam.sms.driver.core.Command;
import com.hoteam.sms.driver.util.MessageTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author weixiang
 * @date 2018/8/2 10:28
 */
public class CmppSubmit extends CmppMessageHeader {

    private static final Logger logger = LoggerFactory.getLogger(CmppSubmit.class);

    int msgId;

    byte pkTotal;

    byte pkNumber;

    byte registeredDelivery = 1;

    byte msgLevel = 0;

    /**
     * 10位长度
     */
    String serviceId;

    byte feeUserType = 2;

    /**
     * 21位长度
     */
    String feeTerminalId;

    byte feeTerminalType = 0;

    byte tp_pid = 0;

    byte tp_udhi;

    /**
     * 0：ASCII串
     * 3：短信写卡操作
     * 4：二进制信息
     * 8：UCS2编码
     * 15：含GB汉字
     */
    byte msgFmt = 8;

    /**
     * 6位长度
     */
    String msgSrc;
    /**
     * 02：对“计费用户号码”按条计信息费
     * 03：对“计费用户号码”按包月收取信息费
     * 04：对“计费用户号码”的信息费封顶
     * 05：对“计费用户号码”的收费是由SP实现
     * 2位长度
     */
    String feeType;

    /**
     * 6位长度
     */
    String feeCode;
    /**
     * 17位长度
     */
    String vaildTime = "";
    /**
     * 17位长度
     */
    String atTime = "";

    /**
     * 21位长度
     */
    String srcId;
    byte destUsrTl = 1;

    /**
     * 21位长度
     */
    String destTerminalId;

    byte destTerminalType = 0;

    byte msgLength;
    /**
     * 消息总长度
     */
    int totalLength;

    byte[] msgContent;

    /**
     * 保留字
     */
    String linkId = "";

    private int terminalIdLen;
    private int linkIdLen;
    private int submitExpMsgLen;

    public CmppSubmit(byte version, String serviceId, String srcId, int sequenceId, String mobile, byte[]
            content, boolean large, int pkTotal, int pkNumber, int totalLength) {
        super(Command.CMPP_SUBMIT, version, sequenceId);
        if (version == Command.CMPP2_VERSION) {
            terminalIdLen = 21;
            linkIdLen = 8;
            submitExpMsgLen = Command.CMPP2_SUBMIT_LEN_EXPMSGLEN;
        } else {
            terminalIdLen = 32;
            linkIdLen = 20;
            submitExpMsgLen = Command.CMPP3_SUBMIT_LEN_EXPMSGLEN;
        }
        if (large) {
            this.tp_udhi = 1;
            this.pkNumber = (byte) pkNumber;
            this.pkTotal = (byte) pkTotal;
        } else {
            this.tp_udhi = 0;
            this.pkNumber = 1;
            this.pkTotal = 1;
        }
        this.serviceId = serviceId;
        this.feeTerminalId = "";
        this.feeType = "02";
        this.feeCode = "000000";
        this.srcId = srcId + "1630";
        this.msgFmt = (byte) 8;
        this.linkId = "";
        this.destTerminalId = mobile;
        //这个设置无用
        this.msgId = sequenceId;
        this.sequenceId = sequenceId;
        this.msgContent = content;
        this.totalLength = totalLength;
        this.msgLength = (byte) content.length;
    }

    @Override
    public byte[] toByteArray() {
        //创建指定长度的buffer
        ByteBuf buffer = Unpooled.buffer(138 + ((byte) 1) * 21 + msgContent.length);
        //写入消息头:total_length,command_id,sequence_id
        buffer.writeInt(138 + ((byte) 1) * 21 + msgContent.length);
        buffer.writeInt(commandId);
        buffer.writeInt(this.sequenceId);
        //写入消息体
        buffer.writeLong(0);
        buffer.writeByte(this.pkTotal);
        buffer.writeByte(this.pkNumber);
        buffer.writeByte(this.registeredDelivery);
        buffer.writeByte(this.msgLevel);
        buffer.writeBytes(MessageTool.getLenBytes(serviceId, 10));
        buffer.writeByte(this.feeUserType);
        buffer.writeBytes(MessageTool.getLenBytes(this.feeTerminalId, 21));
        buffer.writeByte(this.tp_pid);
        buffer.writeByte(this.tp_udhi);
        buffer.writeByte(this.msgFmt);
        buffer.writeBytes(MessageTool.getLenBytes(serviceId, 6));
        buffer.writeBytes(MessageTool.getLenBytes(feeType, 2));
        buffer.writeBytes(MessageTool.getLenBytes(feeCode, 6));
        buffer.writeBytes(MessageTool.getLenBytes(vaildTime, 17));
        buffer.writeBytes(MessageTool.getLenBytes(atTime, 17));
        buffer.writeBytes(MessageTool.getLenBytes(srcId, 21));
        buffer.writeByte((byte) 1);
        buffer.writeBytes(MessageTool.getLenBytes(destTerminalId, 21));
        buffer.writeByte(msgContent.length);
        buffer.writeBytes(msgContent);
        buffer.writeLong((long) 0);
        return buffer.array();
    }
}
