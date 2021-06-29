package com.whotw.common.properties;

/**
 * @author EdisonXu
 * @date 2019-07-28
 */

public class JwtProperties {

    private String secret = WhotwDefaults.Jwt.secret;

    private String base64Secret = WhotwDefaults.Jwt.base64Secret;

    private long tokenValidityInSeconds = WhotwDefaults.Jwt
            .tokenValidityInSeconds;

    private long tokenValidityInSecondsForRememberMe = WhotwDefaults.Jwt
            .tokenValidityInSecondsForRememberMe;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getBase64Secret() {
        return base64Secret;
    }

    public void setBase64Secret(String base64Secret) {
        this.base64Secret = base64Secret;
    }

    public long getTokenValidityInSeconds() {
        return tokenValidityInSeconds;
    }

    public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
        this.tokenValidityInSeconds = tokenValidityInSeconds;
    }

    public long getTokenValidityInSecondsForRememberMe() {
        return tokenValidityInSecondsForRememberMe;
    }

    public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
        this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
    }
}
