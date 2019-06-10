package com.hoteam.sms.driver.domain.response;

/**
 * @ Description：短信送交响应
 * @ Author     ：丁明威
 * @ Date       ：Created in 10:36 2019/6/10
 * @ Modified By：
 */
public class CmppDeliverResp {
    private int msg_id;
    private int result;

    public CmppDeliverResp(int msg_id, int result) {
        this.msg_id = msg_id;
        this.result = result;
    }

    public CmppDeliverResp() {
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}