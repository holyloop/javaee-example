package com.github.holyloop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    /**
     * 权限字符串, 原理参考https://shiro.apache.org/permissions.html<br>
     * <br>
     * 三层结构权限可表述为"resource-type:operation:instance"<br>
     * 操作(operate)资源类型(resource-type)中的实例(instance)的权限, operation<br>
     * 分为select, insert, update, delete.<br>
     */
    @Column(name = "permission_str", nullable = false)
    private String permissionStr;

    public Permission() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissionStr() {
        return permissionStr;
    }

    public void setPermissionStr(String permissionStr) {
        this.permissionStr = permissionStr;
    }

}
