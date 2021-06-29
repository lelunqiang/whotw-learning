package com.whotw.uaa.security.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whotw.uaa.entity.RoleFunctionBinding;
import com.whotw.uaa.mapper.RoleFunctionBindingRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author EdisonXu
 * @date 2019-10-20
 */
@Service
public class RoleFunctionBindingService extends ServiceImpl<RoleFunctionBindingRepository, RoleFunctionBinding> {

    private static final Logger logger = LoggerFactory.getLogger(RoleFunctionBindingService.class);

    public void batchDeleteByRoleId(Long roleId){
        remove(Wrappers.<RoleFunctionBinding>lambdaQuery().eq(RoleFunctionBinding::getRoleId, roleId));
    }

    public void batchDeleteByFunctionId(Long functionId){
        remove(Wrappers.<RoleFunctionBinding>lambdaQuery().eq(RoleFunctionBinding::getFunctionEndpointId, functionId));
    }

    public void batchDeleteByFunctionIds(List<Long> functionIds){
        remove(Wrappers.<RoleFunctionBinding>lambdaQuery().in(RoleFunctionBinding::getFunctionEndpointId, functionIds));
    }


    public List<RoleFunctionBinding> listByRoleIds(List<Long> roleIds){
        return lambdaQuery()
                .in(RoleFunctionBinding::getRoleId, roleIds)
                .list();
    }

    public void batchSaveByRoleId(Long roleId, Set<Long> boundFunctionIds){
        if(CollectionUtils.isNotEmpty(boundFunctionIds)){
            List<RoleFunctionBinding> roleFunctionBindings = boundFunctionIds
                    .stream()
                    .map(i->new RoleFunctionBinding(roleId, i))
                    .collect(Collectors.toList());
            saveBatch(roleFunctionBindings);
        }
    }

    public void batchUpdateByRoleId(Long roleId, Set<Long> boundFunctionIds){
        if(CollectionUtils.isNotEmpty(boundFunctionIds)){
            Set<RoleFunctionBinding> roleFunctionBindings = boundFunctionIds
                    .stream()
                    .map(i->new RoleFunctionBinding(roleId, i))
                    .collect(Collectors.toSet());
            batchDeleteByRoleId(roleId);
            saveBatch(roleFunctionBindings);
        }
    }
}
