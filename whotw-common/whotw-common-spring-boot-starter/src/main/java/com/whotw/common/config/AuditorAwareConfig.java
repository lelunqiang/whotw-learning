package com.whotw.common.config;

import com.whotw.security.domain.SecurityConstants;
import com.whotw.security.domain.audit.Operator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

/**
 * @author EdisonXu
 * @date 2019-09-05
 */
@Configuration
public class AuditorAwareConfig{

    private static final Logger logger = LoggerFactory.getLogger(AuditorAwareConfig.class);

    //TODO: 支持异步写入时塞入Auditor
    /*@Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
        methodInvokingFactoryBean.setTargetMethod("setStrategyName");
        methodInvokingFactoryBean.setArguments(new String[]{SecurityContextHolder.MODE_INHERITABLETHREADLOCAL});
        return methodInvokingFactoryBean;
    }*/

    @Bean
    public AuditorAware<Operator> auditorAware(){
        return () -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(attributes==null)
                return null;
            HttpServletRequest request = attributes.getRequest();
            String userId = request.getHeader(SecurityConstants.HEADER_KEY_USER_ID);
            String userFullName = request.getHeader(SecurityConstants.HEADER_KEY_USER_NAME);
            if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userFullName)){
                try {
                    userFullName = URLDecoder.decode(userFullName, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    //should never happen
                    logger.error("解析HTTP头中的用户名称失败", e);
                }
                Operator operator = new Operator(Long.valueOf(userId), userFullName);
                logger.debug("当前Auditor: "+ operator);
                return Optional.of(operator);
            }
            return Optional.empty();
        };
    }
}
