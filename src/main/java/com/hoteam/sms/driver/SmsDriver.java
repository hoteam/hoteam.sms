package com.hoteam.sms.driver;

import com.hoteam.msre.common.model.Result;

import java.util.List;
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
    Result send(Optional<String> mobile, String content);

    /**
     * 给指定列表的手机号码发送短信
     *
     * @param mobiles
     * @param content
     * @return
     */
    Result sends(Optional<List<String>> mobiles, String content);

    Result close();
}