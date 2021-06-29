package com.whotw.uaa.security.authentication.provider;

import com.whotw.common.data.SystemRole;
import com.whotw.security.core.AccountDetails;
import com.whotw.security.domain.Account;
import com.whotw.security.exception.AccountNotFoundException;
import com.whotw.security.oauth2.provider.token.mobile.ParentMobileAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;

import java.util.HashSet;
import java.util.Set;

/**
 * @author EdisonXu
 * @date 2019-07-24
 */
public class ParentMobileAuthenticationProvider /*implements AuthenticationProvider*/ {

    /*private static final Logger logger = LoggerFactory.getLogger(ParentMobileAuthenticationProvider.class);

    protected ParentBindingService parentBindingService;
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    protected GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public ParentMobileAuthenticationProvider(ParentBindingService parentBindingService) {
        this.parentBindingService = parentBindingService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        ParentMobileAuthenticationToken token = (ParentMobileAuthenticationToken)authentication;
        String mobile = (String) token.getPrincipal();
        String password = token.getCredentials()==null? null : (String)token.getCredentials();
        String institutionId = token.getInstitutionId();
        Long parentBindingId = token.getParentBindId();

        AccountDetails existAccount = null;
        try {
            if(parentBindingId!=null)
                existAccount = retrieveAccounts(parentBindingId);
            else
                // 根据传入手机号尝试寻找账户信息
                existAccount = retrieveAccounts(mobile, institutionId, (ParentMobileAuthenticationToken) authentication);
        } catch (AccountNotFoundException notFound) {
            logger.debug("", notFound);
            throw new AccountNotFoundException("该账号不存在");
        }

        // 为了使用JWT时，XshUserAuthenticationConverter.convertUserAuthentication转化的Map中不含Authority，导致根据jwt token
        // 调用extractAuthentication中因为不含Authority认证不成功，此处必须提供Authority
        ParentMobileAuthenticationToken result = new ParentMobileAuthenticationToken(mobile, password, existAccount, institutionId,
                existAccount.getAccountId(),
                existAccount.getAuthorities());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ParentMobileAuthenticationToken.class.isAssignableFrom(authentication);
    }

    protected final AccountDetails retrieveAccounts(Long parentBindingId){
        ParentBinding parentBinding = parentBindingService.getById(parentBindingId);
        if(parentBinding ==null){
            throw new AccountNotFoundException("无法根据id["+parentBindingId+"]找到绑定信息");
        }
        Account account = buildAccountDetail(parentBinding.getMobile(), parentBinding);
        Set<GrantedAuthority> authorities = new HashSet<>();
        // authorities不能为空
        authorities.add(new SimpleGrantedAuthority("parents"));
        return new AccountDetails(account, authorities);
    }
    protected final AccountDetails retrieveAccounts(String mobile, String institutionIdStr,
                                                    ParentMobileAuthenticationToken authentication)
            throws AuthenticationException {
        try {
            Long institutionId = institutionIdStr==null ? null : Long.valueOf(institutionIdStr);
            ParentBinding parentBinding = parentBindingService.findOneByMobileAndInstId(mobile, institutionId);
            if(parentBinding ==null){
                throw new AccountNotFoundException(
                        this.messages.getMessage("parentAccount.notFound",
                                new Object[] {mobile}, "根据手机号"+mobile+"无法找到账户信息")
                );
            }
            Account account = buildAccountDetail(mobile, parentBinding);
            Set<GrantedAuthority> authorities = new HashSet<>();
            // authorities不能为空
            authorities.add(new SimpleGrantedAuthority("parents"));
            return new AccountDetails(account, authorities);
        }
        catch (AccountNotFoundException ex) {
            //mitigateAgainstTimingAttack(authentication);
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    private Account buildAccountDetail(String mobile, ParentBinding parentBinding) {
        Account account = new Account();
        account.setId(parentBinding.getId());
        account.setMobile(mobile);
        account.setUserFullName(parentBinding.getFullName());
        account.setUserId(parentBinding.getStudentId());
        account.setInstitutionId(parentBinding.getInstitutionId());
        account.setInstitutionName(parentBinding.getInstitutionName());
        account.setSystemRole(SystemRole.PARENT);
        account.setRelation(parentBinding.getRelation().name());
        account.setQrCode(parentBinding.getQrCode());
        return account;
    }
*/
}
