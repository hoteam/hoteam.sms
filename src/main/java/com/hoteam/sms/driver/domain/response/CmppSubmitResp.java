package com.hoteam.sms.driver.domain.response;


import com.hoteam.sms.driver.util.MessageTool;
import org.apache.commons.lang3.ArrayUtils;


/**
 * @author weixiang
 * @date 2018/8/2 15:23
 */
public class CmppSubmitResp {

    private int sequenceId;

    private int state;

    private long msgId;

    public CmppSubmitResp(byte[] bytes) {
        this.sequenceId = MessageTool.bytesToInt(ArrayUtils.subarray(bytes, 8, 12));
        this.msgId = MessageTool.bytesToLong(ArrayUtils.subarray(bytes, 12, 20));
        this.msgId = Math.abs(this.msgId);
        this.state = bytes[20];
    }

    public int getState() {
        return state;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public long getMsgId() {
        return msgId;
    }

    @Override
    public String toString() {
        return "CmppSubmitResp{" +
                "sequenceId=" + sequenceId +
                ", state=" + state +
                ", msgId=" + msgId +
                '}';
    }
}
