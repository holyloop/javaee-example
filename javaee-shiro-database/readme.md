# Java EE 7基于数据库的Apache Shiro配置

[上一篇文章](https://www.cnblogs.com/holyloop/p/8909336.html)我介绍了在Java EE环境中配置Shiro的基本方法, 但是在真正的开发过程中我们基本上不会使用基于配置文件的用户角色配置, 大多数情况下我们会将用户, 角色和权限存储在数据库中, 然后我们告诉Shiro去数据库中取数据, 这样的配置更灵活且功能更强大.

这样使Shiro能读数据库(或LDAP, 文件系统等)的组件叫做[Realm](https://shiro.apache.org/realm.html), 可以把Realm看作是一个安全专用的DAO, 下面我详细介绍一下如何配置Realm:

(所用到的技术和上一篇文章中的类似, 此外, 我们用到了JPA, 项目源码参考我的[Github](https://github.com/holyloop/javaee-example/tree/master/javaee-shiro-database))

## 添加依赖

在上一篇文章所用的依赖基础上, 我们还需要添加:

```xml
<dependency>
  <groupId>org.hibernate.javax.persistence</groupId>
  <artifactId>hibernate-jpa-2.1-api</artifactId>
  <version>1.0.0.Final</version>
</dependency>
<dependency>
  <groupId>org.jboss.spec.javax.ejb</groupId>
  <artifactId>jboss-ejb-api_3.2_spec</artifactId>
  <version>1.0.0.Final</version>
  <scope>provided</scope>
</dependency>
<dependency>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-lang3</artifactId>
  <version>3.7</version>
</dependency>
```

## 添加用户等实体

### ER图

这里我将用户(User), 角色(Role), 权限(Permission)的关系定义为:

![](image/er.png)

为简单起见, ER图省略了表的字段, 这里仅说明表之间的关系: 一个用户可以有多个角色, 一个角色有多个权限.

### JPA实体

`src/main/java/com/github/holyloop/entity/User.java`:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(unique = true, nullable = false)
private Long id;

@Column(nullable = false, unique = true)
private String username;

@Column(nullable = false)
private String password;

@Column(nullable = false)
private String salt;

@OneToMany(mappedBy = "user")
private Set<UserRoleRel> userRoleRels = new HashSet<>(0);
```

这里的 `password` 为密文密码, `salt` 为哈希加密用到的盐, 我们让每个用户都有一个随机盐.

`src/main/java/com/github/holyloop/entity/Role.java`:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(unique = true, nullable = false)
private Long id;

@Column(name = "role_name", nullable = false, unique = true)
private String roleName;

@OneToMany(mappedBy = "role")
private Set<UserRoleRel> userRoleRels = new HashSet<>(0);

@OneToMany(mappedBy = "role")
private Set<RolePermissionRel> rolePermissionRels = new HashSet<>(0);
```

`roleName` 如名字所述, 为权限名.

`src/main/java/com/github/holyloop/entity/Permission.java`:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(unique = true, nullable = false)
private Long id;

@Column(name = "permission_str", nullable = false)
private String permissionStr;
```

`permissionStr` 为权限字符串, 我一般偏爱将权限字符串定义为 `resource-type:operation:instance` , 意为**操作**(operation)**资源**(resource-type)**实例**(instance)的权限, 例如: `org:update:1` 即为"更新id为1的组织"权限. 这种组织权限的方式较灵活, 能精确到**资源实例**, 在对权限要求教高的系统中可以使用这种方式, 一般对于权限不要求精确到资源实例的系统用传统的[RBAC](https://en.wikipedia.org/wiki/Role-based_access_control)即可满足要求. 关于权限的详细定义可参考Shiro的[这个文档](https://shiro.apache.org/permissions.html).

剩下的两个中间关系表较简单, 这里不详细说明.

## 修改shiro.ini

`src/main/webapp/WEB-INF/shiro.ini`:

```ini
[main]
authc.loginUrl = /login

credentialsMatcher = org.apache.shiro.authc.credential.Sha256CredentialsMatcher
credentialsMatcher.storedCredentialsHexEncoded = false
credentialsMatcher.hashIterations = 1024

dbRealm = com.github.holyloop.secure.shiro.realm.DBRealm
dbRealm.credentialsMatcher = $credentialsMatcher

securityManager.realms = $dbRealm

[urls]
/index.html = anon
/login = authc
```

比较之前的配置, 我删掉了 `[users]` 和 `[roles]` 块, 因为这些数据我们将从数据库中加载. 此外, 我配置了Shiro的密码匹配器 `credentialsMatcher` , 它将用sha256来做密码匹配; 然后还有我们这篇文章的重点 `dbRealm` , 这个Realm将由我们自己来实现.
