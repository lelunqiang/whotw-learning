package com.whotw.redis.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * @author EdisonXu
 * @date 2020-03-20
 */
public class RedisKeyUtils {

    public static final String DELIMITER = ":";

    public static String buildKey(String prefix, String... name){
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isNotBlank(prefix))
                sb.append(prefix).append(DELIMITER);
        int size = name.length;
        Assert.isTrue(size>0, "Must provide at least one name!");
        for(int i=0;i<size;i++){
            sb.append(name[i]);
            if(i<size-1)
                sb.append(DELIMITER);
        }
        return sb.toString();
    }


}
