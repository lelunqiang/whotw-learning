package com.whotw.common.properties;

public interface WhotwDefaults {

    String MAIN_PREFIX = "whotw";

    interface ValidationCode {

        int length = 6;

        int expireInSeconds = 60*5; // 5 minutes

        interface ImageCode{
            int length = 4;
            int width =  67;
            int height = 23;
        }
    }

    interface Jwt {

        String secret = null;
        String base64Secret = null;
        long tokenValidityInSeconds = 1800; // 0.5 hour
        long tokenValidityInSecondsForRememberMe = 2592000; // 30 hours;
    }
}
