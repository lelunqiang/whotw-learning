package com.whotw.uaa.security.service;

import com.whotw.common.data.RestConstants;
import com.whotw.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author EdisonXu
 * @date 2019-07-25
 */
@Service
public class ValidatePhoneService {

    @Autowired(required = false)
    private JwtTokenProvider jwtTokenProvider;

    public void validateSignature(String token, String mobile){
        Map<String, Object> map = jwtTokenProvider.parseToken(token);
        Object mobileObj = map.get(RestConstants.PARAM_MOBILE);
        Assert.notNull(mobileObj, "该Token未含手机号");
        Assert.isTrue(mobileObj.toString().equals(mobile), "手机号码不匹配");
    }
}
