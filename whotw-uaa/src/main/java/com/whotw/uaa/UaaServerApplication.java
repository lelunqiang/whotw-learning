package com.whotw.uaa;

import com.whotw.DefaultApplication;
import com.whotw.apidoc.annotation.EnableWhotwSwagger2;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;

/**
 * @author whotw
 */
@SpringBootApplication(scanBasePackages = {"com.whotw"})
@EnableWhotwSwagger2
@RefreshScope
public class UaaServerApplication extends DefaultApplication {
    public UaaServerApplication(Environment env) {
        super(env);
    }

    public static void main(String[] args) {
        start(args, UaaServerApplication.class);
    }

}
