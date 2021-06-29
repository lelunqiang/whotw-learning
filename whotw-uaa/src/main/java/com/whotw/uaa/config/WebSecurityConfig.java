package com.whotw.uaa.config;

import com.whotw.uaa.security.config.wechat.AuthedMobileAuthenticationSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author EdisonXu
 * @date 2019/7/11
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthedMobileAuthenticationSecurityConfigurer authedMobileAuthenticationSecurityConfigurer;

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder(); //不能再用了
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /*
     * 要支持password的认证方式，必须要提供一个authenticationManager
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                //.antMatchers("/register/**", "/test/**", "/validation/**")
                ;
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);//配置CorsFilter优先级
        return bean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
/*                .authorizeRequests()
                    .antMatchers("/validation/**", "/test/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()*/
                .apply(authedMobileAuthenticationSecurityConfigurer)
                    .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
        ;
    }
}
