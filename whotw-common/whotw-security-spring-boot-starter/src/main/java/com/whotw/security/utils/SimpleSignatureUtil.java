package com.whotw.security.utils;

import com.whotw.security.config.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

/**
 * @author EdisonXu
 * @date 2019-07-26
 */
@Component
public class SimpleSignatureUtil {

    private static final Logger logger = LoggerFactory.getLogger(SimpleSignatureUtil.class);

    @Resource
    private SecurityProperties securityProperties;

    public String sign(String... params){
        if(params==null || params.length==0)
            return null;
        StringBuilder sb = new StringBuilder(securityProperties.getSignature().getSecret());
        logger.info("Using secret: {}", securityProperties.getSignature().getSecret());
        for (String param : params) {
            sb.append(param);
        }
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes());
    }
}
