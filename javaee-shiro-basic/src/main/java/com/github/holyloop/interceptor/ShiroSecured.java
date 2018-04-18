package com.github.holyloop.interceptor;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

/**
 * 该注解可在类及方法上使用, 被注解的元素将经过拦截器
 * {@link com.github.holyloop.interceptor.ShiroSecuredInterceptor ShiroSecuredInterceptor}
 * 
 * @author holyloop
 */
@InterceptorBinding
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface ShiroSecured {

}
