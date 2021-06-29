package com.whotw.uaa.security.service;

import com.whotw.common.exception.BusinessException;
import com.whotw.security.domain.SecurityConstants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * Created by Edison
 * On 2019-07-13 22:09
 */
public class CacheableJdbcClientDetailsService extends JdbcClientDetailsService {

    public CacheableJdbcClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Cacheable(value = SecurityConstants.CACHE_CLIENT_KEY, key="#clientId")
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        return super.loadClientByClientId(clientId);
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        super.addClientDetails(clientDetails);
    }

    @CachePut(value = SecurityConstants.CACHE_CLIENT_KEY, key="#clientDetails.clientId")
    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        throw new BusinessException("请勿用该方法更新client信息");
        //super.updateClientDetails(clientDetails);
    }

    @CachePut(value = SecurityConstants.CACHE_CLIENT_KEY, key="#clientDetails.clientId")
    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        throw new BusinessException("请勿用该方法更新client信息");
        //super.updateClientSecret(clientId, secret);
    }

    @CacheEvict(value = SecurityConstants.CACHE_CLIENT_KEY, key="#clientId")
    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        throw new BusinessException("请勿用该方法删除client信息");
        //super.removeClientDetails(clientId);
    }

}
