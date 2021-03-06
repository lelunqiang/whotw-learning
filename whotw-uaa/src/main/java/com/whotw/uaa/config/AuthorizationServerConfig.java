package com.whotw.uaa.config;

import com.whotw.uaa.security.core.userdetails.AccountDetailsService;
import com.whotw.uaa.security.oauth2.provider.token.AuthenticatedMobileTokenGranter;
import com.whotw.uaa.security.oauth2.provider.token.MobilePasswordTokenGranter;
import com.whotw.uaa.security.oauth2.provider.token.store.WhotwAccessTokenConverter;
import com.whotw.uaa.security.service.CustomTokenServices;
import com.whotw.uaa.security.service.ValidatePhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.ArrayList;
import java.util.List;

/**
 * ?????????????????????
 * @author EdisonXu
 * @date 2019/7/3
 */

@Configuration
@EnableAuthorizationServer
@Order(Ordered.LOWEST_PRECEDENCE)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter
{

    /**
     * ??????authenticationManager ????????? password grant type
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ValidatePhoneService validatePhoneService;
    @Autowired
    private AccountDetailsService accountDetailsService;
    @Autowired
    private ClientDetailsService customClientDetailsService;
    @Autowired
    private CustomTokenServices customTokenServices;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private WhotwAccessTokenConverter whotwAccessTokenConverter;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // ??????/oauth/token_key???????????????JWT token???????????????????????????
                .tokenKeyAccess("permitAll()")
                // ??????/oauth/check_token????????????????????????????????????decode token??????????????????????????????
                //TBD: Gateway?????????????????????decode???????????????????????????????????????
                .checkTokenAccess("isAuthenticated()")
                //???/oauth/token??????client_id??????client_secret???????????????
                .allowFormAuthenticationForClients()
                //TBD: ??????????????????client_secret??????encode????????????????????????????????????????????????????????????
                .passwordEncoder(passwordEncoder)
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        customTokenServices.setTokenEnhancer(whotwAccessTokenConverter);
        endpoints
                .tokenServices(customTokenServices)
                .tokenStore(tokenStore)
                // ???????????????password??????????????????????????????authenticationManager!!
                .authenticationManager(authenticationManager)
                // RefreshToken???????????????Token?????????UseDetails????????????????????????????????????token?????????
                .userDetailsService(accountDetailsService)
                // ??????????????????GET????????????????????????????????????????????????GET???POST
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                //.authorizationCodeServices(authorizationCodeServices)
                // AccessToken?????????RefreshToken????????????????????????
                .reuseRefreshTokens(false)
                .tokenGranter(tokenGranter(endpoints))
                .accessTokenConverter(whotwAccessTokenConverter)
                .tokenEnhancer(whotwAccessTokenConverter)
        ;

    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(customClientDetailsService);
    }

    private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints){
        return new CompositeTokenGranter(getTokenGranters(endpoints));
    }

    private List<TokenGranter> getTokenGranters(AuthorizationServerEndpointsConfigurer endpoints) {
        AuthorizationCodeServices authorizationCodeServices = endpoints.getAuthorizationCodeServices();
        OAuth2RequestFactory requestFactory = endpoints.getOAuth2RequestFactory();
        //TBD: ?????????Redis??????ClientDetails
        List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
        tokenGranters.add(new AuthorizationCodeTokenGranter(customTokenServices, authorizationCodeServices, customClientDetailsService,
                requestFactory));
        tokenGranters.add(new RefreshTokenGranter(customTokenServices, customClientDetailsService, requestFactory));
        ImplicitTokenGranter implicit = new ImplicitTokenGranter(customTokenServices, customClientDetailsService, requestFactory);
        tokenGranters.add(implicit);
        tokenGranters.add(new ClientCredentialsTokenGranter(customTokenServices, customClientDetailsService, requestFactory));
        if (authenticationManager != null) {
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, customTokenServices,
                    customClientDetailsService, requestFactory));
            MobilePasswordTokenGranter mobilePasswordTokenGranter = new MobilePasswordTokenGranter(customTokenServices, customClientDetailsService,
                    requestFactory, authenticationManager);
            tokenGranters.add(mobilePasswordTokenGranter);

            AuthenticatedMobileTokenGranter authedMobileTokenGranter = new AuthenticatedMobileTokenGranter(customTokenServices, customClientDetailsService,
                    requestFactory, validatePhoneService, authenticationManager);
            tokenGranters.add(authedMobileTokenGranter);
            /*
            FreeUserWxTokenGranter freeUserWxTokenGranter = new FreeUserWxTokenGranter(customTokenServices, customClientDetailsService, requestFactory, authenticationManager, wxServiceFeign);
            tokenGranters.add(freeUserWxTokenGranter);
            */
        }
        return tokenGranters;
    }
}
