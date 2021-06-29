package com.whotw.security;

import com.whotw.security.core.AccountDetails;
import com.whotw.security.oauth2.provider.token.CustomAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Optional;

/**
 * 安全相关的工具类
 *
 * @author EdisonXu
 * @date 2019/7/11
 */

public final class SecurityUtils {

    private static final String BASIC_ = "Basic ";

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    private SecurityUtils() {
    }

    /**
     * 获取当前登录用户的userId
     *
     * @return the login of the current user.
     */
    public static Long getCurrentUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        AccountDetails accountDetails = getAccountDetails(authentication);
        if(accountDetails!=null)
            return accountDetails.getUserId();
        return null;
    }

    /**
     * 获取当前登录用户的封装类
     *
     * @return the login of the current user.
     */
    public static AccountDetails getCurrentUserDetails() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return getAccountDetails(authentication);
    }

    public static AccountDetails getAccountDetails(Authentication authentication){
        if (authentication!=null && authentication instanceof CustomAuthenticationToken) {
            AccountDetails accountDetails = ((CustomAuthenticationToken) authentication).getAccountDetails();
            return accountDetails;
        }
        if(authentication!=null && authentication instanceof OAuth2Authentication){
            Authentication userAuth = ((OAuth2Authentication)authentication).getUserAuthentication();
            return getAccountDetails(userAuth);
        }
        return null;
    }

    public static void updateCurrentUserDetails(AccountDetails accountDetails){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication!=null && authentication instanceof CustomAuthenticationToken) {
            ((CustomAuthenticationToken) authentication).setAccountDetails(accountDetails);
            return;
        }
        if(authentication!=null && authentication instanceof OAuth2Authentication){
            Authentication userAuth = ((OAuth2Authentication)authentication).getUserAuthentication();
            updateCurrentUserDetails(accountDetails);
        }
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> authentication.getAuthorities().stream()
                        .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)))
                .orElse(false);
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the {@code isUserInRole()} method in the Servlet API.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)))
                .orElse(false);
    }

    /**
     * 获取requet(head/param)中的token
     * @param request
     * @return
     */
    public static String extractToken(HttpServletRequest request) {
        String token = extractHeaderToken(request);
        if (token == null) {
            token = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
            if (token == null) {
                logger.debug("Token not found in request parameters.  Not an OAuth2 request.");
            }
        }
        return token;
    }

    /**
     * 解析head中的token
     * 参照 {@link org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor}
     * @param request
     * @return
     */
    private static String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders("Authorization");
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }
}
