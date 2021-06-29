package com.whotw.sample;

import com.whotw.DefaultApplication;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;

/**
 * @author whotw
 * @description SampleServerApplication
 * @date 2021/4/20 23:22
 */
@SpringBootApplication(scanBasePackages = {"com.whotw"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.whotw"})
@RefreshScope
public class SampleServerApplication extends DefaultApplication {
    public SampleServerApplication(Environment env) {
        super(env);
    }

    public static void main(String[] args) {
        //启动报java.lang.IllegalStateException: failed to req API:/nacos/v1/ns/service/list  我们没有安装启动nacos，安装启动nacos就可以了
        start(args, SampleServerApplication.class);
    }
}
