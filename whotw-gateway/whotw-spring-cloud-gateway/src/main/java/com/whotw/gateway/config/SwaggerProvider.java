package com.whotw.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author EdisonXu
 * @date 2019-08-09
 */
@Component
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {

    public static final String API_URI = "/v2/api-docs";

    private final GatewayProperties gatewayProperties;
    private static final Logger logger = LoggerFactory.getLogger(SwaggerProvider.class);

    @Value("${spring.cloud.gateway.discovery.locator.enabled}")
    private boolean isDiscoveryLocatorEnabled;

    public SwaggerProvider(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public List<SwaggerResource> get() {
        return gatewayProperties
                .getRoutes()
                .stream()
                .flatMap(
                        routeDefinition ->
                                routeDefinition
                                        .getPredicates()
                                        .stream()
                                        .filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
                                        .map(predicateDefinition -> {
                                            String location =
                                                    predicateDefinition
                                                            .getArgs()
                                                            .get(NameUtils.GENERATED_NAME_PREFIX + "0")//只取第一个
                                                            .replace("/**", API_URI);
                                            if(isDiscoveryLocatorEnabled)
                                                location = "/" + routeDefinition.getUri().getHost()+location;
                                            return swaggerResource(routeDefinition.getId(), location); })
                )
                .sorted(Comparator.comparing(SwaggerResource::getName))
                .collect(Collectors.toList());
    }

    private static SwaggerResource swaggerResource(String name, String location){
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

}
