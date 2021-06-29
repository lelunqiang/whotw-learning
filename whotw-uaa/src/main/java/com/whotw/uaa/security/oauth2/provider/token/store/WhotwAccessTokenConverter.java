package com.whotw.uaa.security.oauth2.provider.token.store;

import com.whotw.security.SecurityUtils;
import com.whotw.security.core.AccountDetails;
import com.whotw.security.domain.Account;
import com.whotw.security.domain.SecurityConstants;
import com.whotw.security.oauth2.provider.token.WhotwUserAuthenticationConverter;
import com.whotw.utils.CommonUtil;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author EdisonXu
 * @date 2019-08-08
 */
public class WhotwAccessTokenConverter extends JwtAccessTokenConverter {

    private boolean isJwtToken;

    public WhotwAccessTokenConverter() {
        this(false);
    }

    public WhotwAccessTokenConverter(boolean isJwtToken) {
        this.isJwtToken = isJwtToken;
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(new WhotwUserAuthenticationConverter());
        setAccessTokenConverter(accessTokenConverter);
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if(isJwtToken)
            accessToken = super.enhance(accessToken, authentication);
        if (SecurityConstants.GRANT_TYPE_CLIENT_CREDENTIALS
                .equals(authentication.getOAuth2Request().getGrantType())
        ) {
            return accessToken;
        }

        // 此处添加的是返回到前端的用户信息，additionalInfo并不被添加到Token里面，DefaultAccessTokenConverter.convertAccessToken
        // 方法会调用XshUserAuthenticationConverter.convertUserAuthentication 去将认证信息塞入token
        // 如果要将additionalInfo，需要在调用上面super.enhance之前塞入accessToken
        final Map<String, Object> additionalInfo = new HashMap<>(8);
        AccountDetails accountDetails = SecurityUtils.getAccountDetails(authentication);
        if(accountDetails==null)
            return accessToken;
        additionalInfo.put(SecurityConstants.ACCOUNT_KEY, buildAccountInfo(accountDetails.getAccount()));
        additionalInfo.put(SecurityConstants.ACCOUNT_BOUND, accountDetails.getAccountBound());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

    /**
     * 剔除掉不需要的信息，以及敏感信息
     * @param original
     * @return
     */
    private Account buildAccountInfo(Account original){
        Account toReturn = CommonUtil.map(original, Account.class);
        toReturn.setPassword(null);
        toReturn.setWxOpenId(null);
        toReturn.setWxSessionKey(null);
        toReturn.setWxUnionId(null);
        return toReturn;
    }
}
