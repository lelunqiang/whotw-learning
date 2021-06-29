package com.whotw.common.properties;

/**
 * 验证码配置
 *
 * @author EdisonXu
 * @date 2019-07-24
 */

public class ValidationCodeProperties {

    /**
     * 图片验证码配置
     */
    private com.whotw.common.properties.ImageCodeProperties image = new com.whotw.common.properties.ImageCodeProperties();
    /**
     * 短信验证码配置
     */
    private com.whotw.common.properties.SmsCodeProperties sms = new com.whotw.common.properties.SmsCodeProperties();

    public com.whotw.common.properties.ImageCodeProperties getImage() {
        return image;
    }

    public void setImage(com.whotw.common.properties.ImageCodeProperties image) {
        this.image = image;
    }

    public com.whotw.common.properties.SmsCodeProperties getSms() {
        return sms;
    }

    public void setSms(com.whotw.common.properties.SmsCodeProperties sms) {
        this.sms = sms;
    }

}
