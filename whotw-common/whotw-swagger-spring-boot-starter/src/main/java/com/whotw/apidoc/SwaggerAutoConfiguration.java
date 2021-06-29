package com.whotw.apidoc;

import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.DispatcherServlet;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Servlet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author EdisonXu
 * @date 2019-08-09
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({
        ApiInfo.class,
        BeanValidatorPluginsConfiguration.class,
        Servlet.class,
        DispatcherServlet.class,
        Docket.class
})
@Profile("swagger")
@EnableSwagger2
@EnableAutoConfiguration
@EnableSwaggerBootstrapUI
@Import(BeanValidatorPluginsConfiguration.class)
@ConditionalOnProperty(name = "whotw.swagger.enabled", matchIfMissing = true)
@EnableConfigurationProperties({SwaggerProperties.class})
public class SwaggerAutoConfiguration {

    /**
     * 	默认的排除路径，排除Spring Boot默认的错误处理路径和端点
     */
    private static final List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/error","/actuator/**");
    private static final String BASE_PATH = "/**";

    private static final Logger logger = LoggerFactory.getLogger(SwaggerAutoConfiguration.class);

    private final SwaggerProperties properties;
    @Autowired
    private TypeResolver typeResolver;

    public SwaggerAutoConfiguration(SwaggerProperties properties) {
        this.properties = properties;
    }


    @Bean
    public Docket api(@Value("${spring.application.name:application}") String appName){
        if(properties.getIncludePath().isEmpty()){
            properties.getIncludePath().add(BASE_PATH);
        }
        List<Predicate<String>> includePathPredicates =
                properties
                        .getIncludePath()
                        .stream()
                        .map(path->(Predicate<String>)PathSelectors.ant(path))
                        .collect(Collectors.toList());

        if(properties.getExcludePath().isEmpty()){
            properties.getExcludePath().addAll(DEFAULT_EXCLUDE_PATH);
        }
        List<Predicate<String>> excludePathPredicates =
                properties
                        .getExcludePath()
                        .stream()
                        .map(path->(Predicate<String>)PathSelectors.ant(path))
                        .collect(Collectors.toList());

        return createDocket()
                .apiInfo(buildApiInfo(appName))
                .host(properties.getHost())
                .useDefaultResponseMessages(properties.isUseDefaultResponseMessages())
                //.groupName(MANAGEMENT_GROUP_NAME)
                .forCodeGeneration(true)
                .directModelSubstitute(ByteBuffer.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage()))
                .paths(Predicates.and(Predicates.not(Predicates.or(excludePathPredicates)), Predicates.or(includePathPredicates)))
                .build()
                //.pathMapping("/")
                ;
    }

    private ApiInfo buildApiInfo(String appName){

        String title = appName!=null ? StringUtils.capitalize(appName) : properties.getTitle();

        Contact contact = new Contact(
                properties.getContactName(),
                properties.getContactUrl(),
                properties.getContactEmail());

        return new ApiInfoBuilder()
                .title(title)
                .description(properties.getDescription())
                .version(properties.getVersion())
                .license(properties.getLicense())
                .licenseUrl(properties.getLicenseUrl())
                .termsOfServiceUrl(properties.getTermsOfServiceUrl())
                .contact(contact)
                .build();

    }

    protected Docket createDocket() {
        return new Docket(DocumentationType.SWAGGER_2);
    }
}
