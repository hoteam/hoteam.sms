package com.hoteam.sms.driver.core;

/**
 * 短信POJO类
 */
public class Sms {
    private String mobile;
    private String content;

    public Sms(String mobile, String content) {
        this.mobile = mobile;
        this.content = content;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
