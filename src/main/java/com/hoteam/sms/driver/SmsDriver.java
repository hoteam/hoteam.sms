package com.hoteam.sms.driver;

import com.hoteam.msre.common.model.Result;
import com.hoteam.sms.driver.util.SmsResult;

import java.util.Optional;

/**
 * @ Description：短信发送抽象接口
 * @ Author     ：丁明威
 * @ Date       ：Created in 15:40 2019/6/10
 * @ Modified By：
 */
public interface SmsDriver {

    /**
     * 给指定mobile发送短信
     *
     * @param mobile  手机号码
     * @param content 短信内容
     * @return
     */
    SmsResult send(Optional<String> mobile, String content);

    Result close();
}