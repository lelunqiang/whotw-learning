package com.whotw.security.domain.audit;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author EdisonXu
 * @date 2019-09-05
 */
public class Operator implements Serializable {

    private static final long serialVersionUID = 5866205823946854239L;

    private Long id;
    private String fullName;

    public Operator() {
    }

    public Operator(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operator operator = (Operator) o;
        return Objects.equals(id, operator.id) &&
                Objects.equals(fullName, operator.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName);
    }

    @Override
    public String toString() {
        return "Auditor{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
