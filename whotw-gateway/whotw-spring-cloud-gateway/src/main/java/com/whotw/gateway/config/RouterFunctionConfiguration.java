package com.whotw.gateway.config;

import com.whotw.gateway.handler.HystrixFallbackHandler;
import com.whotw.gateway.handler.SwaggerResourceHandler;
import com.whotw.gateway.handler.SwaggerSecurityHandler;
import com.whotw.gateway.handler.SwaggerUIHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * @author EdisonXu
 * @date 2019-08-09
 */
@Configuration
public class RouterFunctionConfiguration {

    @Autowired
    private HystrixFallbackHandler hystrixFallbackHandler;
    @Autowired
    private SwaggerResourceHandler swaggerResourceHandler;
    @Autowired
    private SwaggerUIHandler swaggerUIHandler;
    @Autowired
    private SwaggerSecurityHandler swaggerSecurityHandler;

    @Bean
    public RouterFunction routerFunction(){
        return RouterFunctions.route(
                RequestPredicates.path("/fallback")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)), hystrixFallbackHandler)
                .andRoute(RequestPredicates.GET("/swagger-resources")
                        .and(RequestPredicates.accept(MediaType.ALL)), swaggerResourceHandler)
                .andRoute(RequestPredicates.GET("/swagger-resources/configuration/ui")
                        .and(RequestPredicates.accept(MediaType.ALL)), swaggerUIHandler)
                .andRoute(RequestPredicates.GET("/swagger-resources/configuration/security")
                        .and(RequestPredicates.accept(MediaType.ALL)), swaggerSecurityHandler)
                ;
    }

}
