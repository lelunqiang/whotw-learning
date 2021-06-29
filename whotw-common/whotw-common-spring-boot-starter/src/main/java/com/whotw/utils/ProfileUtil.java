package com.whotw.utils;

import com.whotw.common.data.ProfileConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Profile相关的工具类
 *
 * @author EdisonXu
 * @date 2019/7/12
 */
@Component
public class ProfileUtil {

    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

    @Autowired
    private Environment env;

    /**
     *
     *
     * @param app the Spring application.
     */
    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap<>();
        /*
         * The default profile to use when no other profiles are defined
         * This cannot be set in the application.yml file.
         * See https://github.com/spring-projects/spring-boot/issues/1219
         */
        defProperties.put(SPRING_PROFILE_DEFAULT, ProfileConstants.SPRING_PROFILE_DEVELOPMENT);
        app.setDefaultProperties(defProperties);
    }

    public boolean isProduction(){
        if(env==null)
            return false;
        String[] activeProfiles = env.getActiveProfiles();
        return Arrays.stream(activeProfiles).anyMatch(profile-> ProfileConstants.SPRING_PROFILE_PRODUCTION.equalsIgnoreCase(profile));
    }

    public String getApplicationName(){
        return env.getProperty("spring.application.name");
    }
}
