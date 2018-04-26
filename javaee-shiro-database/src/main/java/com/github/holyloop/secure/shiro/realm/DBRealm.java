package com.github.holyloop.secure.shiro.realm;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;

import com.github.holyloop.entity.User;
import com.github.holyloop.service.UserService;

public class DBRealm extends AuthorizingRealm {

    private UserService userService;

    public DBRealm() {
        userService = lookupUserService();
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String username = StringUtils.trim((String) principals.getPrimaryPrincipal());

        Set<String> roles = userService.listRolesByUsername(username);
        Set<String> permissions = userService.listPermissionsByUsername(username);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String username = StringUtils.trim((String) token.getPrincipal());
        if (StringUtils.isEmpty(username)) {
            throw new AuthenticationException();
        }

        User user = userService.findOneByUsername(username);
        if (user == null) {
            throw new AuthenticationException();
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, user.getPassword(),
                new SimpleByteSource(Base64.decode(user.getSalt())), getName());

        return authenticationInfo;
    }

    @SuppressWarnings("rawtypes")
    private UserService lookupUserService() {
        UserService userService = null;

        try {
            BeanManager beanManager = InitialContext.doLookup("java:comp/BeanManager");
            @SuppressWarnings("unchecked")
            Bean<UserService> userServiceBean = (Bean) beanManager.getBeans(UserService.class).iterator().next();
            CreationalContext context = beanManager.createCreationalContext(userServiceBean);
            userService = (UserService) beanManager.getReference(userServiceBean, UserService.class, context);
        } catch (NamingException e) {
            throw new AuthenticationException("can not load UserService");
        }

        return userService;
    }

}
