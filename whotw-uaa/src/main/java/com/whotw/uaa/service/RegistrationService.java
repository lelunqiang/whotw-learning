package com.whotw.uaa.service;

import com.whotw.common.data.RestConstants;
import com.whotw.security.domain.SecurityConstants;
import com.whotw.uaa.rest.dto.AccountDTO;
import com.whotw.uaa.rest.vo.RegistrationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author whotw
 * @description RegistrationService
 * @date 2021/5/28 15:36
 */
@Service
public class RegistrationService {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    private TokenEndpoint tokenEndpoint;
    private DaoAuthenticationProvider provider;

    public RegistrationService(PasswordEncoder passwordEncoder, ClientDetailsService clientDetailsService) {
        ClientDetailsUserDetailsService userDetailsService = new ClientDetailsUserDetailsService(clientDetailsService);
        provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
    }

    /**
     * 注册账号并自动登录新注册的账号
     *
     * @param registrationInfo
     * @param clientInfo
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    public OAuth2AccessToken registerWithAutoLogin(RegistrationInfo registrationInfo, String[] clientInfo) {
        return registerWithAutoLogin(registrationInfo, clientInfo, false);
    }

    @Transactional(rollbackFor = {Exception.class})
    public OAuth2AccessToken registerWithAutoLogin(RegistrationInfo registrationInfo, String[] clientInfo, boolean mobileValidated) {
        //注册用户信息
//        AccountDTO account = register(registrationInfo, mobileValidated, clientInfo);

        AccountDTO account = new AccountDTO();
        account.setMobile("17674362786");
        account.setInstitutionId(489329625L);
        try {
            if (mobileValidated)
                return loginWithTrustMobile(clientInfo, account.getMobile(), account.getInstitutionId(), account.isParentLogin(), account.getId(), account.getNew());
            else
                return autoLogin(registrationInfo.getSignature(), clientInfo, account.getMobile(), account.getInstitutionId(), account.isParentLogin(), account.getId(), account.getNew());
        } catch (Exception e) {
            logger.error("账户创建成功但登录失败", e);
            throw new RuntimeException("账户创建成功但登录失败");
        }
    }

    public OAuth2AccessToken autoLogin(String signature, String[] clientInfo, String mobile, Long institutionId, boolean parentLogin, Long parentBindingId, Boolean isNew) throws HttpRequestMethodNotSupportedException {
        // 自动Login
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(clientInfo[0], clientInfo[1]);
        //Authentication userAuth = authenticationManager.authenticate(authentication);
        Authentication userAuth = authenticateClient(authentication);
        Map<String, String> authParams = new HashMap<>();
        authParams.put(RestConstants.PARAM_GRANT_TYPE, SecurityConstants.GRANT_TYPE_AUTHED_MOBILE);
        authParams.put(RestConstants.PARAM_MOBILE, mobile);
        authParams.put(RestConstants.PARAM_SIGNATURE, signature);
        authParams.put(RestConstants.PARAM_INSTITUTION_ID, institutionId.toString());
        authParams.put(RestConstants.PARAM_PARENT_LOGIN, String.valueOf(parentLogin));
        if (parentBindingId != null)
            authParams.put(RestConstants.PARAM_PARENT_BINDING_ID, String.valueOf(parentBindingId));
        ResponseEntity responseEntity = tokenEndpoint.postAccessToken(userAuth, authParams);
        OAuth2AccessToken oAuth2AccessToken = (OAuth2AccessToken) responseEntity.getBody();
        if (parentLogin && isNew)
            oAuth2AccessToken.getAdditionalInformation().put(RestConstants.NEW_ACCOUNT, isNew);
        return oAuth2AccessToken;
    }

    public OAuth2AccessToken loginWithTrustMobile(String[] clientInfo, String trustMobile, Long institutionId, boolean parentLogin, Long parentBindingId, Boolean isNew) throws HttpRequestMethodNotSupportedException {
        Assert.notNull(institutionId, "必须提供机构ID");
        Assert.hasText(trustMobile, "无效的手机号");

        // 自动Login
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(clientInfo[0], clientInfo[1]);
        //Authentication userAuth = authenticationManager.authenticate(authentication);
        Authentication userAuth = authenticateClient(authentication);
        Map<String, String> authParams = new HashMap<>();
        authParams.put(RestConstants.PARAM_GRANT_TYPE, SecurityConstants.GRANT_TYPE_AUTHED_MOBILE);
        authParams.put(RestConstants.PARAM_TRUST_MOBILE, trustMobile);
        authParams.put(RestConstants.PARAM_INSTITUTION_ID, institutionId.toString());
        authParams.put(RestConstants.PARAM_PARENT_LOGIN, String.valueOf(parentLogin));
        if (parentBindingId != null)
            authParams.put(RestConstants.PARAM_PARENT_BINDING_ID, String.valueOf(parentBindingId));
        ResponseEntity responseEntity = tokenEndpoint.postAccessToken(userAuth, authParams);
        OAuth2AccessToken oAuth2AccessToken = (OAuth2AccessToken) responseEntity.getBody();
        if (parentLogin && isNew)
            oAuth2AccessToken.getAdditionalInformation().put(RestConstants.NEW_ACCOUNT, isNew);
        return oAuth2AccessToken;
    }

    private Authentication authenticateClient(UsernamePasswordAuthenticationToken authentication) {
        try {
            return provider.authenticate(authentication);
        } catch (AuthenticationException e) {
            if (e instanceof BadCredentialsException) {
                e = new BadCredentialsException(e.getMessage(), new BadClientCredentialsException());
            }
            throw e;
        }
    }
}
