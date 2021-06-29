package com.whotw.uaa.security.oauth2.provider.token;

import com.whotw.common.data.RestConstants;
import com.whotw.security.domain.SecurityConstants;
import com.whotw.security.exception.AccountNotFoundException;
import com.whotw.security.exception.WechatAuthenticationException;
import com.whotw.security.oauth2.provider.token.mobile.MobileAuthenticationToken;
import com.whotw.uaa.security.oauth2.exception.OAuth2AccountNotFoundException;
import com.whotw.uaa.security.oauth2.exception.OAuth2AccountStatusException;
import com.whotw.uaa.security.oauth2.exception.OAuth2BadCredentialsException;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

/**
 * 支持grant_type为 mobile_password 登录的Token
 *
 * @author EdisonXu
 * @date 2019-07-22
 */

public class MobilePasswordTokenGranter extends AbstractTokenGranter {

    private final AuthenticationManager authenticationManager;

    public MobilePasswordTokenGranter(AuthorizationServerTokenServices tokenServices,
                                      ClientDetailsService clientDetailsService,
                                      OAuth2RequestFactory requestFactory,
                                      AuthenticationManager authenticationManager) {
        this(tokenServices, clientDetailsService, requestFactory, authenticationManager, SecurityConstants.GRANT_TYPE_MOBILE_PASSWORD);
    }

    public MobilePasswordTokenGranter(AuthorizationServerTokenServices tokenServices,
                                      ClientDetailsService clientDetailsService,
                                      OAuth2RequestFactory requestFactory,
                                      AuthenticationManager authenticationManager,
                                      String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails clientDetails, TokenRequest tokenRequest) {
        Map<String, String> parameters = tokenRequest.getRequestParameters();
        String mobile = parameters.get(RestConstants.PARAM_MOBILE);
        String password = parameters.get(RestConstants.PARAM_PASSWORD);
        String institutionId = parameters.get(RestConstants.PARAM_INSTITUTION_ID);
        String adminLogin = parameters.get(RestConstants.PARAM_IS_ADMIN_LOGIN);

        boolean isAdminLogin = adminLogin==null ? false : Boolean.valueOf(adminLogin);

        if(StringUtils.isBlank(mobile))
            throw new InvalidRequestException("必须提供手机号");
        if(StringUtils.isBlank(password))
            throw new InvalidRequestException("必须提供密码");

        String clientId = clientDetails.getClientId();
        String clientSecret = clientDetails.getClientSecret();

        //Authentication userAuth = new WechatAuthenticationToken(clientId, clientSecret, accountDetails, Collections.emptyList());
        Authentication userAuth = new MobileAuthenticationToken(mobile, password, institutionId, isAdminLogin);
        try {
            //验证通过后尝试读取用户账号信息
            userAuth = authenticationManager.authenticate(userAuth);
        }
        catch (AccountNotFoundException e){
            throw new OAuth2AccountNotFoundException(e.getMessage());
        }
        catch (AccountStatusException ase) {
            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new OAuth2AccountStatusException(ase.getMessage());
        }
        catch (BadCredentialsException e) {
            // If the username/password are wrong the spec says we should send 400/invalid grant
            throw new OAuth2BadCredentialsException(e.getMessage());

        }catch (WechatAuthenticationException e) {
            throw new InvalidGrantException(e.getMessage());
        }
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user");
        }
        OAuth2Request oAuth2Request = super.getRequestFactory().createOAuth2Request(clientDetails, tokenRequest);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, userAuth);
        return oAuth2Authentication;
    }
}
