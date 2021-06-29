/**
 * 
 */
package com.whotw.common.properties;

/**
 * 图片验证码配置项
 *
 * @author EdisonXu
 * @date 2019-07-24
 *
 */
public class ImageCodeProperties extends SmsCodeProperties {
	
	public ImageCodeProperties() {
		setLength(WhotwDefaults.ValidationCode.ImageCode.length);
	}
	
	/**
	 * 图片宽
	 */
	private int width = WhotwDefaults.ValidationCode.ImageCode.width;
	/**
	 * 图片高
	 */
	private int height = WhotwDefaults.ValidationCode.ImageCode.height;
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

}
