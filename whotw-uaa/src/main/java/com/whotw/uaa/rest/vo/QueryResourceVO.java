package com.whotw.uaa.rest.vo;

import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author EdisonXu
 * @date 2019-12-02
 */
@ApiModel("资源点查询筛选条件")
public class QueryResourceVO implements Serializable {

    private static final long serialVersionUID = -7557849143372732825L;

    private String description;
    private String url;
    private String method;
    private Integer type;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getType() {
        return type;
    }

    public QueryResourceVO setType(Integer type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("description", description)
                .append("url", url)
                .append("method", method)
                .append("type", type)
                .toString();
    }
}
