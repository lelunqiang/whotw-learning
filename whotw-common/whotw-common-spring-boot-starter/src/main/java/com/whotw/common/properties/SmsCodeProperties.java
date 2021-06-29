/**
 * 
 */
package com.whotw.common.properties;

/**
 * 短信验证码配置
 *
 * @author EdisonXu
 * @date 2019-07-24
 *
 */
public class SmsCodeProperties {
	
	/**
	 * 验证码长度
	 */
	private int length = WhotwDefaults.ValidationCode.length;
	/**
	 * 过期时间
	 */
	private int expireIn = WhotwDefaults.ValidationCode.expireInSeconds;
	/**
	 * 要拦截的url，多个url用逗号隔开，ant pattern
	 */
	private String url;

	public int getLength() {
		return length;
	}
	public void setLength(int lenght) {
		this.length = lenght;
	}
	public int getExpireIn() {
		return expireIn;
	}
	public void setExpireIn(int expireIn) {
		this.expireIn = expireIn;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
