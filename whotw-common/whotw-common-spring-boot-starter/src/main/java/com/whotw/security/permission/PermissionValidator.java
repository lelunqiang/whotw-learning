package com.whotw.security.permission;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限位检测方法
 *
 * @author EdisonXu
 * @date 2020-02-07
 */
public class PermissionValidator {

    private static final Logger logger = LoggerFactory.getLogger(PermissionValidator.class);
    private ThreadLocal<ObjectMapper>  mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    /**
     * 根据传入的权限位字符串，解析成权限空间与权限位
     * @param permissionSumString
     * @return
     */
    public List<Map<Long, Long>> parsePermissionMap(String permissionSumString, boolean throwException) throws AccessDeniedException {
        if(StringUtils.isBlank(permissionSumString))
            return null;
        ObjectMapper objectMapper = mapperThreadLocal.get();
        JavaType type = objectMapper.getTypeFactory().constructMapType(HashMap.class, Long.class, Long.class);
        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
        try {
            return mapperThreadLocal.get().readValue(permissionSumString, listType);
        } catch (Exception e) {
            logger.error("权限集解析失败，禁止访问", e);
            if(throwException)
                throw new AccessDeniedException("无权限访问");
        }
        return null;
    }

    public boolean doPermissionCheck(Long[] permission, List<Map<Long, Long>> permissionSums, boolean throwException) throws AccessDeniedException {
        if(CollectionUtils.isEmpty(permissionSums)) {
            // 有权限位，而未匹配上，则说明无权访问
            return false;
        }
        for (Map<Long, Long> posPermissionSum : permissionSums) {
            if(MapUtils.isEmpty(posPermissionSum))
                continue;

            Long permissionPos = permission[0];
            Long permissionIdx = permission[1];

            try {
                Long permissionSum = posPermissionSum.get(permissionPos);
                if(permissionSum==null && permissionIdx==null) //如果该权限空间位里没有权限位合集，且该资源对应的权限位也没有合集，说明不需要权限
                    return true;
                if(logger.isDebugEnabled()){
                    logger.debug("待检查权限位：{}-{}", permissionPos, Long.toBinaryString(permissionIdx));
                    logger.debug("所有权限信息: {}", permissionSum==null ? permissionSum : Long.toBinaryString(permissionSum));
                }
                if(permissionSum!=null && (permissionIdx == (permissionSum & permissionIdx))) //按位与，如果结果仍然是资源权限位本身，说明有权限
                    return true;
            } catch (Exception e) {
                logger.error("权限匹配检查失败，禁止访问", e);
                if(throwException)
                    throw new AccessDeniedException("无权限访问");
            }
        }
        return false;
    }

}
