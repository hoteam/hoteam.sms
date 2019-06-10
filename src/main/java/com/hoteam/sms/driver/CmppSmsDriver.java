package com.hoteam.sms.driver;

import com.hoteam.msre.common.model.Result;
import com.hoteam.sms.driver.config.CmppConfig;
import com.hoteam.sms.driver.core.Command;
import com.hoteam.sms.driver.core.NettyClient;
import com.hoteam.sms.driver.domain.CmppMessageHeader;
import com.hoteam.sms.driver.domain.CmppSubmit;
import com.hoteam.sms.driver.utils.MessageTool;
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
    private CmppConfig config;

    private NettyClient client;

    @Override
    public Result send(Optional<String> mobile, String content) {
        if (mobile.isPresent()) {
            boolean success = this.client.submit(buildMessage(mobile.get(), content));
            if (success) {
                logger.info("sms send to ismg success!!");
                return Result.SUCCESS("sms submit to ismg success!");
            } else {
                logger.error("sms does not send to ismg!!!");
                return Result.FAILED("sms send to ismg error!");
            }
        } else {
            return Result.FAILED("mobile is empty!!!");
        }
    }

    @Override
    public Result sends(Optional<List<String>> mobiles, String content) {
        int failed = 0;
        Result result;
        if (mobiles.isPresent() && mobiles.get().size() > 0) {
            for (String mobile : mobiles.get()) {
                result = send(Optional.of(mobile), content);
                if (result.isFailed()) {
                    failed++;
                }
            }
            if (failed == 0) {
                logger.info("batch sms send success!!");
                return Result.SUCCESS("batch sms send success!!");
            } else {
                logger.error("batch send sms does not full success with failed number:{}", failed);
                return Result.FAILED("batch send sms does not full success with failed number:" + failed);
            }
        } else {
            logger.warn("mobile is null or empty!!!");
            return Result.FAILED("mobile is null or empty");
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
    public CmppSmsDriver(CmppConfig config) {
        this.config = config;
        this.client = new NettyClient(config.getHost(), config.getPort(), config.getUser(), config.getPasd());
        this.client.start();
    }

    private CmppMessageHeader buildMessage(String mobile, String content) {
        int sequenceId = MessageTool.getSequence();
        return new CmppSubmit(Command.CMPP2_VERSION, this.config.getUser(), this.config.getCode(), sequenceId, mobile,
                content);
    }

    public CmppConfig getConfig() {
        return config;
    }
}