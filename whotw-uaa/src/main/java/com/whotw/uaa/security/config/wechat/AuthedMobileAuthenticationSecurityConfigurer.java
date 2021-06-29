package com.whotw.uaa.security.config.wechat;

import com.whotw.uaa.security.authentication.provider.MobileAuthenticationProvider;
import com.whotw.uaa.security.core.userdetails.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * 短信验证码登录配置
 *
 * @author EdisonXu
 * @date 2019/7/11
 */
@Component
public class AuthedMobileAuthenticationSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {


    @Autowired
    private AccountDetailsService userDetailsService;
    /*@Autowired
    private ParentBindingService parentBindingService;
    @Autowired
    private FreeUserService freeUserService;*/

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
        mobileAuthenticationProvider.setAccountDetailsService(userDetailsService);
        /**
        ParentMobileAuthenticationProvider parentMobileAuthenticationProvider = new ParentMobileAuthenticationProvider(parentBindingService);
        FreeUserWxAuthenticationProvider freeUserWxAuthenticationProvider = new FreeUserWxAuthenticationProvider(freeUserService);
         */
        httpSecurity.authenticationProvider(mobileAuthenticationProvider)
                    /*.authenticationProvider(parentMobileAuthenticationProvider)
                    .authenticationProvider(freeUserWxAuthenticationProvider)*/;
    }

}
