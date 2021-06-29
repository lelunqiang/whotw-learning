package com.whotw.apidoc.annotation;

import com.whotw.apidoc.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author EdisonXu
 * @date 2019-08-09
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class})
public @interface EnableWhotwSwagger2 {
}
