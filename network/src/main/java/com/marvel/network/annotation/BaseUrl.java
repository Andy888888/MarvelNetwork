package com.marvel.network.annotation;

import com.marvel.network.interceptor.BaseUrlInterceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述:设置baseUrl注解.
 * <p>
 *
 * @author yanwenqiang.
 * @date 2019/1/22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BaseUrl {
    /**
     * 设置baseUrl
     *
     * @return 返回baseUrl
     */
    String value() default "";

    /**
     * 设置baseUrl，通过传入一个实现BaseUrlInterceptor接口的类来实现动态设置baseUrl，避开注解必须是常量值.
     *
     * @return 返回一个继承BaseUrlInterceptor的类
     */
    Class<? extends BaseUrlInterceptor> dynamic() default BaseUrlInterceptor.class;
}
