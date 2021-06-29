package com.whotw.common.data;

import org.apache.commons.lang3.StringUtils;

/**
 * 用户对应的应用角色，可在前端进行角色切换
 *
 * @author EdisonXu
 * @date 2019-07-22
 */

public enum SystemRole {

    PRINCIPAL("校长"), //校长
    TEACHER("老师"),   //老师
    PARENT("家长"),     //家长
    FREE_USER("免费用户");

    private String description;

    SystemRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static SystemRole fromDescription(String description){
        if(StringUtils.isEmpty(description))
            return null;
        for(SystemRole each: SystemRole.values()){
            if(each.description.equals(description))
                return each;
        }
        return null;
    }

    public static SystemRole from(String value){
        if(StringUtils.isEmpty(value))
            return null;
        for(SystemRole each: SystemRole.values()){
            if(each.name().equals(value))
                return each;
        }
        return null;
    }
}
