package com.whotw.uaa.rest;


import com.whotw.uaa.rest.vo.AccountRegisterVO;
import com.whotw.uaa.service.RegistrationService;
import com.whotw.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegistrationService registrationService;


    /**
     * 创建新用户同时自动登录
     *
     * @return
     */
    @PostMapping("/new/login")
    @ResponseBody
    @Transactional(rollbackFor = {Exception.class})
    public OAuth2AccessToken registerWithAutoLogin(@Valid @RequestBody AccountRegisterVO accountRegisterVO, HttpServletRequest request) {
//        String[] clientInfo = WebUtils.extractClient(request);
        String[] clientInfo = new String[2];
        clientInfo[0] = "test";
        clientInfo[1] = "1234";
        return registrationService.registerWithAutoLogin(accountRegisterVO, clientInfo);
    }

}
