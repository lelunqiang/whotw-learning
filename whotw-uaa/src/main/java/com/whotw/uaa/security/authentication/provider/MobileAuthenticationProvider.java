package com.whotw.uaa.security.authentication.provider;

import com.whotw.security.core.AccountDetails;
import com.whotw.security.exception.AccountNotFoundException;
import com.whotw.security.oauth2.provider.token.mobile.MobileAuthenticationToken;
import com.whotw.uaa.security.core.userdetails.AccountDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * @author EdisonXu
 * @date 2019-07-24
 */

public class MobileAuthenticationProvider implements AuthenticationProvider {

    /**
     * The plaintext password used to perform
     * PasswordEncoder#matches(CharSequence, String)}  on when the user is
     * not found to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private static final Logger logger = LoggerFactory.getLogger(MobileAuthenticationProvider.class);

    private PasswordEncoder passwordEncoder;

    /**
     * The password used to perform
     * {@link PasswordEncoder#matches(CharSequence, String)} on when the user is
     * not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private volatile String userNotFoundEncodedPassword;

    protected AccountDetailsService accountDetailsService;
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    protected GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public MobileAuthenticationProvider() {
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        MobileAuthenticationToken token = (MobileAuthenticationToken)authentication;
        String mobile = (String) token.getPrincipal();
        String password = token.getCredentials()==null? null : (String)token.getCredentials();
        String institutionId = token.getInstitutionId();
        boolean isAdminLogin = token.isAdminLogin();

        AccountDetails existAccount = null;
        try {
            // 根据传入手机号尝试寻找账户信息
            existAccount = retrieveAccounts(mobile, institutionId, isAdminLogin, (MobileAuthenticationToken) authentication);
        } catch (AccountNotFoundException notFound) {
            logger.debug("", notFound);
            throw new AccountNotFoundException("该账号不存在");
        }
        if(StringUtils.isNotBlank(password)){
            String rawPassword = existAccount.getPassword();
            //检查数据库中的用户是否有密码
            if(rawPassword!=null){
                // 如果带有密码，则尝试密码是否匹配
                if (!passwordEncoder.matches(password, rawPassword)) {
                    logger.debug("Authentication failed: password does not match stored value");

                    throw new BadCredentialsException(messages.getMessage(
                            "MobileAuthenticationProvider.badCredentials",
                            "账号密码不匹配"));
                }
            }else{
                logger.debug("Authentication failed: password not set yet");

                throw new BadCredentialsException(messages.getMessage(
                        "MobileAuthenticationProvider.badCredentials",
                        "您尚未设置登录密码"));
            }
        }
        // 把非法的账号过滤掉
        checkValidity(existAccount);
        /*List<AccountDetails> availableAccounts = filterValidAccountDetails(mobile, existAccount);
        if(availableAccounts.size()==0)
            throw new NoValidAccountException("你的账户存在异常，请联系客服或管理员");*/
        // 为了使用JWT时，XshUserAuthenticationConverter.convertUserAuthentication转化的Map中不含Authority，导致根据jwt token
        // 调用extractAuthentication中因为不含Authority认证不成功，此处必须提供Authority
        MobileAuthenticationToken result = new MobileAuthenticationToken(mobile, password, existAccount, institutionId,
                existAccount.getAuthorities());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /*protected List<AccountDetails> filterValidAccountDetails(String mobile, List<AccountDetails> exsitAccounts) {
        List<AccountDetails> result = new ArrayList<>();
        exsitAccounts.forEach(each->{
            try{
                checkValidity(each);
                result.add(each);
            }catch (Exception e){
                //do nothing here
            }
        });
        logger.debug("Mobile {} has {}/{} valid account", mobile, result.size(), exsitAccounts.size());
        return result;
    }*/

    protected final AccountDetails retrieveAccounts(String mobile, String institutionId, boolean isAdminLogin,
                                                MobileAuthenticationToken authentication)
            throws AuthenticationException {
        prepareTimingAttackProtection();
        try {
            AccountDetails loadedAccount = accountDetailsService.loadAccountByMobile(mobile, institutionId, isAdminLogin);
            if (loadedAccount==null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedAccount;
        }
        catch (AccountNotFoundException ex) {
            //mitigateAgainstTimingAttack(authentication);
            throw ex;
        }
        catch (InternalAuthenticationServiceException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    protected void checkValidity(AccountDetails accountDetails) {
        Long accountId = accountDetails.getAccountId();
        if (!accountDetails.isAccountNonLocked()) {
            logger.debug("Account[accountId=" + accountId +"] is locked");

            throw new LockedException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.locked",
                    "Account is locked"));
        }

        if (!accountDetails.isEnabled()) {
            logger.debug("Account[accountId=" + accountId +"] is disabled");

            throw new DisabledException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.disabled",
                    "Account is disabled"));
        }

        if (!accountDetails.isAccountNonExpired()) {
            logger.debug("Account[accountId=" + accountId +"] is expired");

            throw new AccountExpiredException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.expired",
                    "User account has expired"));
        }
        if (!accountDetails.isCredentialsNonExpired()) {
            logger.debug("Account[accountId=" + accountId +"] credentials have expired");

            throw new CredentialsExpiredException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                    "User credentials have expired"));
        }
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(MobileAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
     * not set, the password will be compared using {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
     *
     * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
     * types.
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public AccountDetailsService getAccountDetailsService() {
        return accountDetailsService;
    }

    public void setAccountDetailsService(AccountDetailsService accountDetailsService) {
        this.accountDetailsService = accountDetailsService;
    }
}
