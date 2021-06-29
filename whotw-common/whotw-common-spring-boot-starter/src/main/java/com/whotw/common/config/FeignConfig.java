package com.whotw.common.config;

import com.whotw.security.domain.SecurityConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 为Feign加上userId和userFullName头
 *
 * @author EdisonXu
 * @date 2019-09-05
 */
@Configuration
public class FeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //为方便内部调用不进行权限校验，添加InnerAccess的头
        requestTemplate.header(SecurityConstants.HEADER_KEY_INNER_ACCESS, "Inner_Access");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes==null)
            return;
        HttpServletRequest request = attributes.getRequest();
        String institutionId = request.getHeader(SecurityConstants.HEADER_KEY_INSTITUTION_ID);
        String accountId = request.getHeader(SecurityConstants.HEADER_KEY_ACCOUNT_ID);
        String userId = request.getHeader(SecurityConstants.HEADER_KEY_USER_ID);
        String userFullName = request.getHeader(SecurityConstants.HEADER_KEY_USER_NAME);
        //添加
        if(institutionId!=null)
            requestTemplate.header(SecurityConstants.HEADER_KEY_INSTITUTION_ID, institutionId);
        if(accountId!=null)
            requestTemplate.header(SecurityConstants.HEADER_KEY_ACCOUNT_ID, accountId);
        if(userId!=null)
            requestTemplate.header(SecurityConstants.HEADER_KEY_USER_ID, userId);
        if(userFullName!=null)
            requestTemplate.header(SecurityConstants.HEADER_KEY_USER_NAME, userFullName);
        //内部调用UAA时，需要带上Authorization头
        String token = request.getHeader(SecurityConstants.HEADER_KEY_AUTHORIZATION);
        if(token!=null)
            requestTemplate.header(SecurityConstants.HEADER_KEY_AUTHORIZATION, token);
    }
}
