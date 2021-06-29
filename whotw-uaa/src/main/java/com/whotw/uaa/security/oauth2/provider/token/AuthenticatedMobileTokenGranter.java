package com.whotw.uaa.security.oauth2.provider.token;

import com.whotw.common.data.RestConstants;
import com.whotw.security.domain.SecurityConstants;
import com.whotw.security.exception.AccountNotFoundException;
import com.whotw.security.exception.WechatAuthenticationException;
import com.whotw.security.oauth2.provider.token.mobile.MobileAuthenticationToken;
import com.whotw.security.oauth2.provider.token.mobile.ParentMobileAuthenticationToken;
import com.whotw.uaa.security.oauth2.exception.OAuth2AccountNotFoundException;
import com.whotw.uaa.security.oauth2.exception.OAuth2AccountStatusException;
import com.whotw.uaa.security.oauth2.exception.OAuth2BadCredentialsException;
import com.whotw.uaa.security.service.ValidatePhoneService;
import io.jsonwebtoken.ExpiredJwtException;
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
 * 支持grant_type为 authed_mobile 登录的Token
 *
 * @author EdisonXu
 * @date 2019-07-22
 */

public class AuthenticatedMobileTokenGranter extends AbstractTokenGranter {

    private final ValidatePhoneService validatePhoneService;
    private final AuthenticationManager authenticationManager;

    public AuthenticatedMobileTokenGranter(AuthorizationServerTokenServices tokenServices,
                                           ClientDetailsService clientDetailsService,
                                           OAuth2RequestFactory requestFactory,
                                           ValidatePhoneService validatePhoneService,
                                           AuthenticationManager authenticationManager) {
        this(tokenServices, clientDetailsService, requestFactory, validatePhoneService, authenticationManager, SecurityConstants.GRANT_TYPE_AUTHED_MOBILE);
    }

    public AuthenticatedMobileTokenGranter(AuthorizationServerTokenServices tokenServices,
                                           ClientDetailsService clientDetailsService,
                                           OAuth2RequestFactory requestFactory,
                                           ValidatePhoneService validatePhoneService,
                                           AuthenticationManager authenticationManager,
                                           String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.validatePhoneService = validatePhoneService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails clientDetails, TokenRequest tokenRequest) {
        Map<String, String> parameters = tokenRequest.getRequestParameters();
        String mobile = parameters.get(RestConstants.PARAM_MOBILE);
        String signature = parameters.get(RestConstants.PARAM_SIGNATURE);
        String institutionId = parameters.get(RestConstants.PARAM_INSTITUTION_ID);
        String trustMobile = parameters.get(RestConstants.PARAM_TRUST_MOBILE);
        String parentLogin = parameters.get(RestConstants.PARAM_PARENT_LOGIN);
        String parentBindingId = parameters.get(RestConstants.PARAM_PARENT_BINDING_ID);

        boolean trustMobileExists = StringUtils.isNotBlank(trustMobile);
        boolean isParentLogin = parentLogin==null ? false : Boolean.valueOf(parentLogin);

        // 当可信手机不存在时，进行校验
        if(!trustMobileExists){
            if(StringUtils.isBlank(mobile))
                throw new InvalidRequestException("必须提供手机号");
            if(StringUtils.isBlank(signature))
                throw new InvalidRequestException("必须提供认证签名");

            try {
                //验证当前传入的手机号，是否是之前验证过的
//                validatePhoneService.validateSignature(signature, mobile);
            } catch (ExpiredJwtException e){
                // should never happen
                logger.error("签名信息过期，请重新验证手机", e);
                throw new InvalidGrantException(e.getMessage());
            } catch (Exception e) {
                logger.error("", e);
                throw new InvalidGrantException("手机验证失败，请重试");
            }
        }else
            mobile = trustMobile;

        Authentication userAuth = null;
        if(isParentLogin){
            if(parentBindingId!=null)
                userAuth = new ParentMobileAuthenticationToken(mobile, null, institutionId, Long.valueOf(parentBindingId));
            else
                userAuth = new ParentMobileAuthenticationToken(mobile, null, institutionId);
        }else{
            //Authentication userAuth = new WechatAuthenticationToken(clientId, clientSecret, accountDetails, Collections.emptyList());
            userAuth = new MobileAuthenticationToken(mobile, null, institutionId);
        }

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
