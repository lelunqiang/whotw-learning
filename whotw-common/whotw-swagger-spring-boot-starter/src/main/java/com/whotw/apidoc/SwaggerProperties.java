package com.whotw.apidoc;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger相关配置封装
 *
 * @author EdisonXu
 * @date 2019-08-09
 */
@ConfigurationProperties(prefix = "whotw.swagger", ignoreUnknownFields = true)
public class SwaggerProperties {

    /**
     * 是否开启swagger
     */
    private Boolean enabled;
    /**
     * 指定解析的包路径
     **/
    private String basePackage = "";
    /**
     * 指定解析的URL Pattern
     **/
    private List<String> includePath = new ArrayList<>();
    /**
     * 指定不解析的URL Pattern
     **/
    private List<String> excludePath = new ArrayList<>();
    /**
     * 标题
     **/
    private String title = "Application API";
    /**
     * 描述
     **/
    private String description = "API documentation";
    /**
     * 版本
     **/
    private String version = "1.0";
    /**
     * 许可证
     **/
    private String license;
    /**
     * 许可证URL
     **/
    private String licenseUrl;
    /**
     * 服务条款URL
     **/
    private String termsOfServiceUrl;
    /**
     * 联系人姓名
     **/
    private String contactName="";
    /**
     * 联系人url
     **/
    private String contactUrl="";
    /**
     * 联系人email
     **/
    private String contactEmail="";
    /**
     * host信息
     **/
    private String host = "";
    /**
     * 使用默认响应消息
     */
    private boolean useDefaultResponseMessages = true;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public List<String> getIncludePath() {
        return includePath;
    }

    public void setIncludePath(List<String> includePath) {
        this.includePath = includePath;
    }

    public List<String> getExcludePath() {
        return excludePath;
    }

    public void setExcludePath(List<String> excludePath) {
        this.excludePath = excludePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isUseDefaultResponseMessages() {
        return useDefaultResponseMessages;
    }

    public void setUseDefaultResponseMessages(boolean useDefaultResponseMessages) {
        this.useDefaultResponseMessages = useDefaultResponseMessages;
    }
}
