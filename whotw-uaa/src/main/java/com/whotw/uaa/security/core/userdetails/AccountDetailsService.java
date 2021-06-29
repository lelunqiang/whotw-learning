package com.whotw.uaa.security.core.userdetails;

import com.whotw.common.data.ApplicationRole;
import com.whotw.security.core.AccountDetails;
import com.whotw.security.domain.Account;
import com.whotw.uaa.entity.AccountRoleEntity;
import com.whotw.uaa.rest.dto.AccountDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author EdisonXu
 * @date 2019-07-22
 */
@Service
public class AccountDetailsService implements UserDetailsService {

    private static final Logger logger = getLogger(AccountDetailsService.class);

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    /*@Autowired
    private AccountService accountService;*/

    /**
     * @return the messages
     */
    protected MessageSourceAccessor getMessages() {
        return this.messages;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public AccountDetails loadAccountByMobile(String mobile, String institutionId, boolean isAdminLogin) {
        /*AccountDTO account = accountService.findByMobileAndInstitutionId(mobile, institutionId, isAdminLogin);
        if(account==null)
            throw new AccountNotFoundException(
                    this.messages.getMessage("loadAccountByMobile.notFound",
                            new Object[] {mobile}, "根据手机号"+mobile+"无法找到账户信息")
            );*/
        AccountDTO account = new AccountDTO();
        return buildAccountDetail(account);
    }

    public AccountDetails loadAccountByMobile(String mobile, String institutionId) {
        return loadAccountByMobile(mobile, institutionId, false);
    }

    public List<AccountDetails> checkUserExistsByWxId(AccountDetails toCheck) {
        if (toCheck == null)
            return Collections.emptyList();
        String unionId = toCheck.getWxUnionId();
        String openId = toCheck.getWxOpenId();
        List<AccountDTO> accounts = null;
        /*if(StringUtils.isNotBlank(unionId)) {
            accounts = accountService.findByWxUnionId(unionId);
            if(accounts == null || accounts.isEmpty())
                throw new AccountNotFoundException(
                        this.messages.getMessage("loadAccountByUnionId.notFound",
                                new Object[] {unionId}, "根据微信UnionId"+unionId+"无法找到账户信息")
                );
        }else if(StringUtils.isNotBlank(openId)) {
            accounts = accountService.findByWxOpenId(openId);
            if(accounts == null || accounts.isEmpty())
                throw new AccountNotFoundException(
                        this.messages.getMessage("loadAccountByOpenId.notFound",
                                new Object[] {openId}, "根据微信OpenId"+openId+"无法找到账户信息")
                );
        }*/
        return buildAccountDetails(accounts);
    }

    private List<AccountDetails> buildAccountDetails(List<AccountDTO> accounts) {
        List<AccountDetails> result = new ArrayList<>();
        if (accounts == null || accounts.isEmpty())
            return result;
        for (AccountDTO accountDTO : accounts) {
            result.add(buildAccountDetail(accountDTO));
        }
        return result;
    }

    private AccountDetails buildAccountDetail(AccountDTO accountDTO) {
        ModelMapper modelMapper = new ModelMapper();
        accountDTO.setApplicationRoles(Arrays.asList(new AccountRoleEntity(123L,"账户名",4234234L,"机构名")));
        Account account = modelMapper.map(accountDTO, Account.class);
        List<AccountRoleEntity> roles = accountDTO.getApplicationRoles();
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (CollectionUtils.isNotEmpty(roles)) {
            roles.forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getId().toString()));
            });
            //TODO: 支持多角色
            AccountRoleEntity roleEntity = roles.get(0);
            account.setApplicationRole(new ApplicationRole(roleEntity.getId(), roleEntity.getName()));
        }
        return new AccountDetails(account, authorities);
    }
}
