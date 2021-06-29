package com.whotw.common.data;

import java.util.Set;

/**
 * @author EdisonXu
 * @date 2019-12-25
 */
public interface TreeNode<T extends TreeNode> extends Comparable<TreeNode> {

    Long getId();
    Long getRootId();
    Long getParentId();
    Set<T> getChildren();
    void setChildren(Set<T> children);

    @Override
    default int compareTo(TreeNode o){
        return this.getId().compareTo(o.getId());
    }
}
