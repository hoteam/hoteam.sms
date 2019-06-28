package com.hoteam.sms.driver.domain;

import com.hoteam.sms.driver.core.Command;
import com.hoteam.sms.driver.util.MessageTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @ Description：
 * @ Author     ：丁明威
 * @ Date       ：Created in 11:14 2019/6/10
 * @ Modified By：
 */
public class CmppTerminate extends CmppMessageHeader {
    private String serviceId;

    private String pwd;

    public CmppTerminate(String serviceId, String pwd,int sequenceId) {
        super(Command.CMPP_TERMINATE, Command.CMPP2_VERSION,sequenceId);
        this.serviceId = serviceId;
        this.pwd = pwd;
    }

    @Override
    public byte[] toByteArray() {
        ByteBuf buf = Unpooled.buffer(4 + 4 + 4 + 6 + 16 + 1 + 4);
        //Total_Length
        buf.writeInt(4 + 4 + 4 + 6 + 16 + 1 + 4);
        //Command_Id
        buf.writeInt(Command.CMPP_TERMINATE);
        //Sequence_Id
        buf.writeInt(MessageTool.getSequence());
        //sourceAddr
        buf.writeBytes(MessageTool.getLenBytes(serviceId, 6));
        //authenticatorSource
        buf.writeBytes(MessageTool.getAuthenticatorSource(serviceId, pwd));
        //version
        buf.writeByte(1);
        //timestamp
        buf.writeInt(Integer.parseInt(MessageTool.getTimestamp()));

        return buf.array();
    }
}