package com.whotw.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author EdisonXu
 * @date 2019/7/9
 */

@ConfigurationProperties(prefix = "xsh.security")
@RefreshScope
public class SecurityProperties {

    private final Signature signature = new Signature();

    public class Signature{
        private String secret = "T+233@zdHeaepz]q4k-dz";

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }
    }

    public Signature getSignature() {
        return signature;
    }
}
