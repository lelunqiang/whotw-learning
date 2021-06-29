package com.whotw.security.oauth2.provider.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whotw.security.core.AccountDetails;
import com.whotw.security.oauth2.provider.token.mobile.MobileAuthenticationToken;
import com.whotw.security.oauth2.provider.token.mobile.ParentMobileAuthenticationToken;
import com.whotw.security.oauth2.provider.token.wechat.WechatAuthenticationToken;
import com.whotw.common.data.Constants;
import com.whotw.common.data.SystemRole;
import com.whotw.security.domain.Account;
import com.whotw.security.domain.SecurityConstants;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author EdisonXu
 * @date 2019-07-23
 */

public class WhotwUserAuthenticationConverter implements UserAuthenticationConverter {

    private static final Logger logger = getLogger(WhotwUserAuthenticationConverter.class);

    private Collection<? extends GrantedAuthority> defaultAuthorities;

    private UserDetailsService userDetailsService;

    private ThreadLocal<ObjectMapper>  mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {

        Map<String, Object> response = new LinkedHashMap<String, Object>();
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        if(authentication instanceof CustomAuthenticationToken){
            Account account = ((CustomAuthenticationToken)authentication).getAccountDetails().getAccount();
            // 剔除掉认证信息中不必要的敏感信息
            Map<String, Object> accountInfo = new HashMap<>();
            accountInfo.put(Constants.ID, account.getId());
            accountInfo.put(SecurityConstants.HEADER_KEY_INSTITUTION_ID, account.getInstitutionId());
            accountInfo.put(SecurityConstants.HEADER_KEY_USER_ID, account.getUserId());
            accountInfo.put(SecurityConstants.HEADER_KEY_MOBILE, account.getMobile());
            accountInfo.put(SecurityConstants.HEADER_KEY_USER_NAME, account.getUserFullName());
            accountInfo.put(SecurityConstants.HEADER_KEY_SYSTEM_ROLE, account.getSystemRole());
            response.put(SecurityConstants.ACCOUNT_KEY, accountInfo);
        }else{
            response.put(USERNAME, authentication.getName());
        }
        return response;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if(map.containsKey(SecurityConstants.ACCOUNT_KEY)){
            ObjectMapper mapper = mapperThreadLocal.get();
            Account account = mapper.convertValue(map.get(SecurityConstants.ACCOUNT_KEY), Account.class);
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
            String institutionId = account.getInstitutionId()==null ? null : account.getInstitutionId().toString();
            if(authorities==null) {
                if(SystemRole.PARENT.equals(account.getSystemRole()))
                    return new ParentMobileAuthenticationToken(account.getMobile(), account.getPassword(), institutionId, account.getId());
                else if(SystemRole.FREE_USER.equals(account.getSystemRole()))
                    return new WechatAuthenticationToken(account.getUserFullName(), account.getAvatarUrl(), account.getWxOpenId(), account.getWxOpenId(), account.getInstitutionId(), null, null);
                else
                    return new MobileAuthenticationToken(account.getMobile(), account.getPassword(), institutionId);
            }else {
                if(SystemRole.PARENT.equals(account.getSystemRole()))
                    return new ParentMobileAuthenticationToken(account.getMobile(), account.getPassword(), new AccountDetails(account, new HashSet<>(authorities)), institutionId, account.getId(), authorities);
                else if(SystemRole.FREE_USER.equals(account.getSystemRole()))
                    return new WechatAuthenticationToken(account.getUserFullName(), account.getAvatarUrl(), account.getWxOpenId(), account.getWxUnionId(), account.getInstitutionId(),
                            new AccountDetails(account, new HashSet<>(authorities)), authorities);
                else
                    return new MobileAuthenticationToken(account.getMobile(), account.getPassword(), new AccountDetails(account, new HashSet<>(authorities)), institutionId, authorities);
            }
        }
        if (map.containsKey(USERNAME)) {
            Object principal = map.get(USERNAME);
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
            if (userDetailsService != null) {
                UserDetails user = userDetailsService.loadUserByUsername((String) map.get(USERNAME));
                authorities = user.getAuthorities();
                principal = user;
            }
            return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        if (!map.containsKey(AUTHORITIES)) {
            return defaultAuthorities;
        }
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }

    public void setDefaultAuthorities(Collection<? extends GrantedAuthority> defaultAuthorities) {
        this.defaultAuthorities = defaultAuthorities;
    }

}
