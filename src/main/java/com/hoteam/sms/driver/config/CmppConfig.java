package com.hoteam.sms.driver.config;

/**
 * @ Description：CMPP2.0短信网关认证信息
 * @ Author     ：丁明威
 * @ Date       ：Created in 15:37 2019/6/10
 * @ Modified By：
 */
public class CmppConfig {
    /**
     * 短信网关地址
     */
    private String host;
    /**
     * 短信网关端口
     */
    private Integer port = 7890;
    /**
     * SP(Service provider)的6位企业代码
     */
    private String spid;
    /**
     * 短信网关账号
     */
    private String user;
    /**
     * 短信网关密码
     */
    private String pasd;
    /**
     * 短信网关代码，8位代码
     */
    private String code;

    public CmppConfig(String host, Integer port, String spid, String user, String pasd, String code) {
        this.host = host;
        this.port = port;
        this.spid = spid;
        this.user = user;
        this.pasd = pasd;
        this.code = code;
    }

    public CmppConfig() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasd() {
        return pasd;
    }

    public void setPasd(String pasd) {
        this.pasd = pasd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}