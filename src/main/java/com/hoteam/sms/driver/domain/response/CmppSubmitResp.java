package com.hoteam.sms.driver.domain.response;


import com.hoteam.sms.driver.utils.MessageTool;
import org.apache.commons.lang3.ArrayUtils;


/**
 * @author weixiang
 * @date 2018/8/2 15:23
 */
public class CmppSubmitResp {

    private long msgId;

    private int state;

    private long msgId2;

    public CmppSubmitResp(byte[] bytes) {
        System.out.println("提交结果消息字节长度：" + bytes.length);
        for (int i : bytes) {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();
        this.msgId = MessageTool.bytesToInt(ArrayUtils.subarray(bytes, 8, 12));
        this.msgId2 = MessageTool.bytesToLong(ArrayUtils.subarray(bytes, 12, 20));
        this.msgId2 = Math.abs(this.msgId2);
        this.state = bytes[20];
    }

    public int getState() {
        return state;
    }

    public long getMsgId2() {
        return msgId2;
    }

    public long getMsgId() {

        return msgId;
    }

    @Override
    public String toString() {
        return "CmppSubmitResp{" +
                "msgId=" + msgId +
                ", state=" + state +
                ", msgId2=" + msgId2 +
                '}';
    }

}
