package com.whotw.security.oauth2.provider.token.wechat;

import com.whotw.security.core.AccountDetails;
import com.whotw.security.oauth2.provider.token.CustomAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * 微信登录用户认证信息
 *
 * @author EdisonXu
 * @date 2019/7/11
 */
public class WechatAuthenticationToken extends AbstractAuthenticationToken implements CustomAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private String name;
    private String avatarUrl;
    private String openId;
    private String unionId;
    private Long parentBindingId;
    private Long instAccountId;
    private Long institutionId;

    private AccountDetails accountDetails;

    public WechatAuthenticationToken(String name, String avatarUrl, String openId, String unionId, Long institutionId) {
        super(null);
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.openId = openId;
        this.unionId = unionId;
        this.institutionId = institutionId;
        //this.parentBindingId = parentBindingId;
        //this.instAccountId = instAccountId;
        setAuthenticated(false);
    }

    public WechatAuthenticationToken(AccountDetails accountDetails, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.accountDetails = accountDetails;
        setAuthenticated(true);
    }

    public WechatAuthenticationToken(String name, String avatarUrl, String openId, String unionId, Long institutionId,
                                     AccountDetails accountDetails,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.openId = openId;
        this.unionId = unionId;
        this.institutionId = institutionId;
        this.accountDetails = accountDetails;
        //this.parentBindingId = parentBindingId;
        //this.instAccountId = instAccountId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 将用户信息放到Principal中，SecurityUilts里可以直接从上下文里拿出该信息使用
     * @return
     */
    @Override
    public Object getPrincipal() {
        return accountDetails;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public String getOpenId() {
        return openId;
    }

    public WechatAuthenticationToken setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public String getUnionId() {
        return unionId;
    }

    public WechatAuthenticationToken setUnionId(String unionId) {
        this.unionId = unionId;
        return this;
    }

    public Long getParentBindingId() {
        return parentBindingId;
    }

    public WechatAuthenticationToken setParentBindingId(Long parentBindingId) {
        this.parentBindingId = parentBindingId;
        return this;
    }

    public Long getInstAccountId() {
        return instAccountId;
    }

    public WechatAuthenticationToken setInstAccountId(Long instAccountId) {
        this.instAccountId = instAccountId;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public WechatAuthenticationToken setName(String name) {
        this.name = name;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public WechatAuthenticationToken setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public Long getInstitutionId() {
        return institutionId;
    }

    public WechatAuthenticationToken setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
        return this;
    }
}
