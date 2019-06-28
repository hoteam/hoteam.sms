package com.hoteam.sms.driver;

import com.hoteam.msre.common.model.Result;
import com.hoteam.sms.driver.config.CmppConfig;
import com.hoteam.sms.driver.core.MessageBuilder;
import com.hoteam.sms.driver.core.NettyClient;
import com.hoteam.sms.driver.core.SmsCache;
import com.hoteam.sms.driver.core.coder.cmpp.CMPPCallback;
import com.hoteam.sms.driver.domain.CmppSubmit;
import com.hoteam.sms.driver.util.Sms;
import com.hoteam.sms.driver.util.SmsResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * @ Description：中移动短信发送驱动
 * @ Author     ：丁明威
 * @ Date       ：Created in 15:58 2019/6/10
 * @ Modified By：
 */
public class CmppSmsDriver implements SmsDriver {
    private final static Logger logger = LoggerFactory.getLogger(CmppSmsDriver.class);
    private final static Integer MAX_CACHE_SIZE = 1024 * 1024;
    private final static String PREFIX = "86";
    private CmppConfig config;
    private NettyClient client;
    private MessageBuilder builder;

    @Override
    public SmsResult send(Optional<String> mobile, String content) {
        if (SmsCache.instance().size() > MAX_CACHE_SIZE) {
            logger.error("sms cache is too large !!! system will reset the sms cache!!");
            SmsCache.instance().reset();
        }
        if (mobile.isPresent()) {
            String targetMobile;
            if (mobile.get().startsWith(PREFIX)) {
                targetMobile = mobile.get();
            } else {
                targetMobile = PREFIX + mobile.get();
            }
            List<CmppSubmit> messages = builder.build(targetMobile, content);
            Sms sms = new Sms((long) messages.get(0).getSequenceId(), targetMobile, content);
            boolean success = this.send(messages);
            if (success) {
                logger.info("send sms to gateway success");
                SmsCache.instance().put(Optional.of(sms));
                return new SmsResult(true, "send sms to gateway success", sms);
            } else {
                logger.error("send sms to gateway failed");
                return new SmsResult(false, "send sms to gateway failed !!!", sms);
            }
        } else {
            logger.error("sms target mobile is empty!!!");
            return new SmsResult(false, "sms target mobile is empty!!!", null);
        }
    }

    @Override
    public Result close() {
        if (null != this.client) {
            this.client.terminate();
            logger.info("cmpp sms driver close !!!");
        }
        return Result.SUCCESS("netty client close success!");
    }

    /**
     * 构造方法
     *
     * @param config
     */
    public CmppSmsDriver(CmppConfig config, CMPPCallback callback) {
        this.config = config;
        this.client = new NettyClient(config, callback);
        this.client.start();
        this.builder = new MessageBuilder(config.getUser(), config.getCode());
    }

    private boolean send(List<CmppSubmit> messages) {
        int success = 0;
        for (CmppSubmit submit : messages) {
            if (this.client.submit(submit)) {
                success++;
            } else {
                logger.error("message[{}] send failed!", submit.getSequenceId());
            }
        }
        return success == messages.size();
    }
}