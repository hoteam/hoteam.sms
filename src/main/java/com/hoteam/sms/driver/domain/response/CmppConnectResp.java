package com.hoteam.sms.driver.domain.response;

import com.hoteam.sms.driver.utils.MessageTool;
import org.apache.commons.lang3.ArrayUtils;

/**
 * CMPP连接响应
 *
 * @ Description：
 * @ Author     ：丁明威
 * @ Date       ：Created in 10:24 2019/6/10
 * @ Modified By：
 */
public class CmppConnectResp {
    private int status;
    private String authenticator;
    private int version;


    public CmppConnectResp(byte[] bytes) {
        this.status = MessageTool.bytesToInt(ArrayUtils.subarray(bytes, 0, 7));
    }

    public CmppConnectResp(int status, String authenticator, int version) {
        this.status = status;
        this.authenticator = authenticator;
        this.version = version;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(String authenticator) {
        this.authenticator = authenticator;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "CmppConnectResp{" +
                "status=" + status +
                ", authenticator='" + authenticator + '\'' +
                ", version=" + version +
                '}';
    }
}