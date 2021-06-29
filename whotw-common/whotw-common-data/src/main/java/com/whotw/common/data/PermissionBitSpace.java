package com.whotw.common.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限位空间
 * @author EdisonXu
 * @date 2020-02-09
 */
public class PermissionBitSpace {

    private final Map<Long, Long> permissionMap;
    private ThreadLocal<ObjectMapper>  mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    public PermissionBitSpace() {
        this.permissionMap = new HashMap<>();
    }

    public PermissionBitSpace(String permissionSumString) throws IOException {
        if(StringUtils.isNotBlank(permissionSumString)){
            this.permissionMap =  convertPermissionStrToMap(permissionSumString);
        }else
            this.permissionMap = new HashMap<>();
    }

    public PermissionBitSpace(List<? extends PermissionBit> initialInfo) {
        this.permissionMap = new HashMap<>();
        batchUpdatePermissionMap(initialInfo);
    }

    public PermissionBitSpace addPermission(PermissionBit permission){
        if(permission==null)
            return this;
        updatePermissionMap(permission);
        return this;
    }

    public PermissionBitSpace batchAddPermission(List<? extends PermissionBit> permissions){
        if(permissions==null)
            return this;
        batchUpdatePermissionMap(permissions);
        return this;
    }

    public Map<Long, Long> getPermissionMap(){
        return permissionMap;
    }

    public String getPermissionMapAsString() throws JsonProcessingException {
        if(MapUtils.isEmpty(permissionMap))
            return null;
        return mapperThreadLocal.get().writeValueAsString(permissionMap);
    }

    private void updatePermissionMap(PermissionBit permissionBit){
        Long permissionPos = permissionBit.getPermissionPos();
        Long permissionIdx = permissionBit.getPermissionIdx();
        Long currentPermission = mergePermission(permissionPos, permissionIdx);
        permissionMap.put(permissionPos, currentPermission);
    }

    private Long mergePermission(Long permissionPos, Long newPermissionIdx){
        Long currentPermission = permissionMap.get(permissionPos);
        if (currentPermission == null)
            currentPermission = newPermissionIdx;

        currentPermission = currentPermission | newPermissionIdx;
        return currentPermission;
    }

    private void batchUpdatePermissionMap(List<? extends PermissionBit> endpoints){
        endpoints.forEach(e->updatePermissionMap(e));
    }

    public void mergePermissionSum(String newPermissionSum) throws IOException {
        if(StringUtils.isBlank(newPermissionSum))
            return;
        Map<Long, Long> newPermissionMap =  convertPermissionStrToMap(newPermissionSum);
        newPermissionMap.forEach((pos,idx)->{
            permissionMap.put(pos, mergePermission(pos, idx));
        });
    }

    private Map<Long, Long> convertPermissionStrToMap(String permissionSum) throws IOException {
        ObjectMapper objectMapper = mapperThreadLocal.get();
        JavaType type = objectMapper.getTypeFactory().constructMapType(HashMap.class, Long.class, Long.class);
        return mapperThreadLocal.get().readValue(permissionSum, type);
    }

    public interface PermissionBit {

        Long getPermissionPos();
        Long getPermissionIdx();

    }
}
