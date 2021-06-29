package com.whotw.security.config;

import com.whotw.security.domain.SecurityConstants;
import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 为Feign加上OAuth的Authorization头和Inner_Access头
 *
 * @author EdisonXu
 * @date 2019-07-25
 */
@Configuration
@ConditionalOnClass({Feign.class})
public class FeignAuthorizationConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes==null)
            return;
        HttpServletRequest request = attributes.getRequest();
        String authHeader = request.getHeader("Authorization");
        //添加token
        if(authHeader!=null)
            requestTemplate.header("Authorization", authHeader);
        //为方便内部调用不进行权限校验，添加InnerAccess的头
        requestTemplate.header(SecurityConstants.HEADER_KEY_INNER_ACCESS, "Inner_Access");
    }
}
