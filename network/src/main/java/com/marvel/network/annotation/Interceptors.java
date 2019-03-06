package com.marvel.network.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import okhttp3.Interceptor;

/**
 * 描述：Interceptor的设置
 * <p>
 *
 * @author yanwenqiang
 * @date 2019/1/21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Interceptors {

    /**
     * 设置请求interceptor
     *
     * @return 返回一个继承Interceptor的类
     */
    Class<? extends Interceptor>[] value();
}
