package com.whotw.common.data;

import java.io.Serializable;

/**
 * 用户角色
 *
 * @author EdisonXu
 * @date 2019-07-30
 */
public class ApplicationRole implements Serializable {

    private static final long serialVersionUID = -6097180428354166767L;

    public static final String ROLE_ADMIN_DESC = "机构管理员";
    public static final String ROLE_PRINCIPAL_DESC = "校长";
    public static final String ROLE_TEACHER_DESC = "老师";
    public static final String ROLE_PART_TIME_TEACHER_DESC = "兼职老师";
    public static final String ROLE_FINANCIAL_OFFICER_DESC = "财务人员";
    public static final String ROLE_SALES_DESC = "销售人员";
    public static final String ROLE_CONSULTANT_DESC = "学管师";

    public static final String[] DEFAULT_ROLES =
            {
                    ROLE_ADMIN_DESC, ROLE_PRINCIPAL_DESC, ROLE_TEACHER_DESC, ROLE_PART_TIME_TEACHER_DESC,
                    ROLE_FINANCIAL_OFFICER_DESC, ROLE_SALES_DESC, ROLE_CONSULTANT_DESC
            };

    protected Long id;
    protected String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ApplicationRole() {
    }

    public ApplicationRole(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    @Override
    public String toString() {
        return "ApplicationRole{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }

}
