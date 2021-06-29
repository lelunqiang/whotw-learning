package com.whotw.uaa.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whotw.uaa.rest.vo.AccountRoleVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 初始化一些系统内置权限和功能点
 *
 * @author EdisonXu
 * @date 2019-10-20
 */
@Service
public class UaaInitializeService {

    private static final Logger logger = LoggerFactory.getLogger(UaaInitializeService.class);

    @Autowired
    RequestMappingHandlerMapping handlerMapping;
    @Autowired
    private AccountRoleService roleService;
    @Autowired
    private ResourceEndpointService resourceEndpointService;


    private static final String MAX_PERMISSION = "9223372036854775807";

    @PostConstruct
    @Transactional
    public void init() throws Exception {
        if(resourceEndpointService.count()>1) {  // 大于1说明初始化过了
            logger.info("跳过初始化");
            return;
        }

        resourceEndpointService.init();

        //创建超级管理员角色
        AccountRoleVO superAdminRole = new AccountRoleVO();
        superAdminRole.setId(1l);
        superAdminRole.setInstitutionId(1L);
        superAdminRole.setName("superadmin");
        Map<Long, String> permissionMap = new HashMap<>();
        Long max = 1000L; // 暂定1K的权限空间集，代表着63*1000个权限位
        for(long i=0;i<max;i++){
            permissionMap.put(i, MAX_PERMISSION);
        }
        superAdminRole.setPermissionSum(new ObjectMapper().writeValueAsString(permissionMap));
        roleService.createRole(superAdminRole);
        logger.info("初始化完成");
    }
}
