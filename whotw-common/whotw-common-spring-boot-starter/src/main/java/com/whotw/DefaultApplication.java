package com.whotw;

import com.whotw.common.data.ProfileConstants;
import com.whotw.utils.ProfileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static org.slf4j.LoggerFactory.getLogger;

public class DefaultApplication implements InitializingBean {

    private static final Logger logger = getLogger(DefaultApplication.class);

    private final Environment env;

    public DefaultApplication(Environment env) {
        this.env = env;
    }

    /**
     * Initializes jhipster.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(ProfileConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(ProfileConstants.SPRING_PROFILE_PRODUCTION)) {
            logger.error("You have misconfigured your application! It should not run " +
                    "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(ProfileConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(ProfileConstants.SPRING_PROFILE_CLOUD)) {
            logger.error("You have misconfigured your application! It should not " +
                    "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void start(String[] args, Class clz) {
        SpringApplication app = new SpringApplication(clz);
        ProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        //set default locale
        setLocale();
        //print start log
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.warn("The host name could not be determined, using `localhost` as fallback");
        }
        logger.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }

    /**
     * set default locale
     */
    private static void setLocale() {
        Locale.setDefault(Locale.CHINA);
        //logger.debug("jvm locale is {}",Locale.getDefault());
    }

}