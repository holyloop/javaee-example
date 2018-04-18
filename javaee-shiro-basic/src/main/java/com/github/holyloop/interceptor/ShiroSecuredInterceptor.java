package com.github.holyloop.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;

/**
 * 所有带 {@link com.github.holyloop.interceptor.ShiroSecured ShiroSecured}
 * 注解的类和方法都经过该拦截器进行安全验证
 * 
 * @author holyloop
 */
@Interceptor
@ShiroSecured
public class ShiroSecuredInterceptor {

    @AroundInvoke
    public Object interceptShiroSecurity(InvocationContext context) throws Exception {
        Class<?> c = context.getTarget().getClass();
        Method m = context.getMethod();
        Subject subject = SecurityUtils.getSubject();

        if (!subject.isAuthenticated() && hasAnnotation(c, m, RequiresAuthentication.class)) {
            throw new UnauthenticatedException("Authentication required");
        }

        if (subject.getPrincipal() != null && hasAnnotation(c, m, RequiresGuest.class)) {
            throw new UnauthenticatedException("Guest required");
        }

        if (subject.getPrincipal() == null && hasAnnotation(c, m, RequiresUser.class)) {
            throw new UnauthenticatedException("User required");
        }

        RequiresRoles roles = getAnnotation(c, m, RequiresRoles.class);

        if (roles != null) {
            // logic and
            if (Logical.AND.equals(roles.logical())) {
                subject.checkRoles(Arrays.asList(roles.value()));
            }
            // logic or
            if (Logical.OR.equals(roles.logical())) {
                boolean hasAtLeastOneRole = false;
                for (String role : roles.value()) {
                    if (subject.hasRole(role)) {
                        hasAtLeastOneRole = true;
                        break;
                    }
                }
                if (!hasAtLeastOneRole) {
                    throw new AuthorizationException();
                }
            }
        }

        RequiresPermissions permissions = getAnnotation(c, m, RequiresPermissions.class);

        if (permissions != null) {
            // logic and
            if (Logical.AND.equals(permissions.logical())) {
                subject.checkPermissions(permissions.value());
            }
            // logic or
            if (Logical.OR.equals(permissions.logical())) {
                boolean hasAtLeastOnePermission = false;
                for (String permission : permissions.value()) {
                    if (subject.isPermitted(permission)) {
                        hasAtLeastOnePermission = true;
                        break;
                    }
                }
                if (!hasAtLeastOnePermission) {
                    throw new AuthorizationException();
                }
            }
        }

        return context.proceed();
    }

    /**
     * 被调用方法是否有指定的注解
     * 
     * @param c 被调用方法所属的类
     * @param m 被调用方法
     * @param a 目标注解
     * @return
     */
    private static boolean hasAnnotation(Class<?> c, Method m, Class<? extends Annotation> a) {
        return m.isAnnotationPresent(a) || c.isAnnotationPresent(a) || c.getSuperclass().isAnnotationPresent(a);
    }

    /**
     * 获取被调用方法指定的注解详情
     * 
     * @param c 被调用方法所属的类
     * @param m 被调用方法
     * @param a 目标注解
     * @return
     */
    private static <A extends Annotation> A getAnnotation(Class<?> c, Method m, Class<A> a) {
        return m.isAnnotationPresent(a) ? m.getAnnotation(a)
                : c.isAnnotationPresent(a) ? c.getAnnotation(a) : c.getSuperclass().getAnnotation(a);
    }
}
