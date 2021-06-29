package com.whotw.uaa.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.whotw.common.data.ApplicationRole;
import com.whotw.common.data.Constants;
import com.whotw.common.data.DistributeUniqueId;
import com.whotw.uaa.entity.AccountRoleEntity;
import com.whotw.uaa.mapper.AccountRoleRepository;
import com.whotw.uaa.rest.vo.*;
import com.whotw.utils.CommonUtil;
import com.whotw.utils.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author edison
 * @since 2019-10-18
 */
@Service
public class AccountRoleService extends ServiceImpl<AccountRoleRepository, AccountRoleEntity> {

    private static final Logger logger = LoggerFactory.getLogger(AccountRoleService.class);

    @Autowired
    private AccountRoleRepository roleRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DistributeUniqueId distributeUniqueId;

  /*  @Autowired
    private FunctionEndpointService functionEndpointService;
    @Autowired
    private AccountService accountService;*/
    @Autowired
    private RoleFunctionBindingService roleFunctionBindingService;

    @Transactional
    public Long createRole(AccountRoleVO roleVO){
        Set<Long> boundFunctionIds = new HashSet<>();
        AccountRoleEntity entity = buildEntity(roleVO, boundFunctionIds);
        save(entity);
        Long roleId = entity.getId();
        //保存角色与功能点的级联
        roleFunctionBindingService.batchSaveByRoleId(roleId, boundFunctionIds);
        //将角色刷入缓存
        redisTemplate.opsForValue().set(Constants.REDIS_KEY_ROLE_ID+entity.getId(), entity.getPermissionSum());
        return roleId;
    }

    private AccountRoleEntity buildEntity(AccountRoleVO roleVO, Set<Long> boundFunctionIds){
        AccountRoleEntity entity = null;
        try {
            entity = roleVO.toEntity();
            if(CollectionUtils.isNotEmpty(roleVO.getFunctionEndpoints())){
                Map<Long, Long> permission = new HashMap<>();
                for (FunctionAndResourceEndpoint functionAndResourceEndpoint : roleVO.getFunctionEndpoints()) {
                    Map<Long, Long> currPermission = functionAndResourceEndpoint.getResourcePermissionSumValue();
                    updatePermission(currPermission, permission);
                    boundFunctionIds.add(functionAndResourceEndpoint.getId());
                    if(CollectionUtils.isNotEmpty(functionAndResourceEndpoint.getChildren())){
                        functionAndResourceEndpoint.getChildren().forEach(f->{
                            updatePermission(f.getResourcePermissionSumValue(), permission);
                            boundFunctionIds.add(f.getId());
                        });
                    }
                }
                if(MapUtils.isNotEmpty(permission)){
                    entity.setPermissionSum(JsonUtils.writeJsonString(permission));
                }
            }
            return entity;
        } catch (JsonProcessingException e) {
            logger.error("解析权限集错误", e);
            throw new IllegalArgumentException(e);
        }
    }

    private void updatePermission(Map<Long, Long> currPermission, Map<Long, Long> permission){
        if(MapUtils.isEmpty(currPermission))
            return;
        currPermission.forEach((pos, idx)->{
            Long latestPermission = permission.get(pos);
            if(latestPermission==null)
                latestPermission = idx;
            else
                latestPermission = latestPermission | idx;
            permission.put(pos, latestPermission);
        });
    }

    @PostConstruct
    public void syncRoleInfoToCache(){
        try {
            List<AccountRoleEntity> roles = roleRepository.selectList(Wrappers.query());
            if(CollectionUtils.isEmpty(roles))
                return;
            doSyncJob(roles);
            logger.info("同步所有角色权限信息到缓存成功");
        } catch (Exception e) {
            logger.error("同步所有角色权限信息到缓存失败", e);
        }
    }

    private void doSyncJob(List<AccountRoleEntity> roles){
        Map<String, String> rolePermissionMap = roles
                .stream()
                .filter(r->StringUtils.isNotBlank(r.getPermissionSum()))
                .collect(Collectors.toMap(r->Constants.REDIS_KEY_ROLE_ID+r.getId(), AccountRoleEntity::getPermissionSum));

        redisTemplate.opsForValue().multiSet(rolePermissionMap);
    }



    public AccountRoleEntity findRoleByName(Long institutionId, String name){
        return roleRepository.selectOne(
                Wrappers
                        .<AccountRoleEntity>lambdaQuery()
                        .eq(AccountRoleEntity::getInstitutionId, institutionId)
                        .eq(AccountRoleEntity::getName, name)
        );
    }

    public List<AccountRoleEntity> listAllRoleInfoOfInstitution(Long institutionId) {
        return roleRepository.selectList(
                Wrappers
                        .<AccountRoleEntity>lambdaQuery()
                        .select(AccountRoleEntity::getId, AccountRoleEntity::getName)
                        .eq(AccountRoleEntity::getInstitutionId, institutionId)
                        .orderByAsc(AccountRoleEntity::getId)
        );
    }

}
