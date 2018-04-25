package com.github.holyloop.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    /**
     * 密文密码
     */
    @Column(nullable = false)
    private String password;

    /**
     * 随机盐
     */
    @Column(nullable = false)
    private String salt;

    /**
     * 用户-角色关系
     */
    @OneToMany(mappedBy = "user")
    private Set<UserRoleRel> userRoleRels = new HashSet<>(0);

    public User() {
        super();
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", salt=" + salt
                + ", userRoleRels=" + userRoleRels + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Set<UserRoleRel> getUserRoleRels() {
        return userRoleRels;
    }

    public void setUserRoleRels(Set<UserRoleRel> userRoleRels) {
        this.userRoleRels = userRoleRels;
    }

}
