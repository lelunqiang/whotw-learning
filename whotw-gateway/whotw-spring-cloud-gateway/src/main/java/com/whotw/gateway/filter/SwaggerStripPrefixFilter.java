package com.whotw.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.whotw.gateway.config.SwaggerProvider.API_URI;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * 自动裁剪SwaggerURL，使其同时支持根据Path或discovery client locator两种方式的路由
 *
 * @author EdisonXu
 * @date 2019-08-09
 */
@Component
public class SwaggerStripPrefixFilter extends AbstractGatewayFilterFactory {

    @Override
    public GatewayFilter apply(Object config) {
        return new SwaggerStripPrefixGatewayFilter();
    }

    public class SwaggerStripPrefixGatewayFilter implements GatewayFilter{

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpRequest request = exchange.getRequest();
            addOriginalRequestUrl(exchange, request.getURI());
            String path = request.getURI().getRawPath();

            // 如果是Swagger的地址，SwaggerProvider中的原理是根据application.xml中所提供的Path拼接一个可以被定位到
            // 目标服务的地址，如 /api/institution/** 被拼接成 /api/institution/v2/api-docs（如果spring.cloud.gateway.discovery.locator.enabled
            // 被置为true，则实际被拼接地址为 /service-institution-info/api/institution/v2/api-docs)
            // 但实际上后面Swagger地址并不在该地址，需要把前面拼接的部分删除
            if(!isSwaggerUrl(path))
                return chain.filter(exchange);

            String pathPrefix = path.substring(0, path.indexOf(API_URI));
            int skipParts = StringUtils.tokenizeToStringArray(pathPrefix, "/").length;

            String newPath = "/"
                    + Arrays.stream(StringUtils.tokenizeToStringArray(path, "/"))
                    .skip(skipParts).collect(Collectors.joining("/"));
            newPath += (newPath.length() > 1 && path.endsWith("/") ? "/" : "");
            ServerHttpRequest newRequest = request.mutate().path(newPath).build();

            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());
            return chain.filter(exchange.mutate().request(newRequest).build());
        }

        private boolean isSwaggerUrl(String path){
            return path.endsWith(API_URI);
        }

    }

}
