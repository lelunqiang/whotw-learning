package com.whotw.mysql.custom;

import com.whotw.common.data.DistributeUniqueId;

/**
 * @author EdisonXu
 * @date 2019-09-05
 */
public enum IdGenerator {

    instance;

    DistributeUniqueId uniqueId;

    public Long getId() {
        return uniqueId.nextId();
    }

    public void init(DistributeUniqueId uniqueId){
        this.uniqueId = uniqueId;
    }

    public String getIdStr() {
        return uniqueId.nextId().toString();
    }
}
