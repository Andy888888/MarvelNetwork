package com.marvel.network.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import okhttp3.Interceptor;

/**
 * 描述:NetWorkInterceptor设置.
 * <p>
 *
 * @author yanwenqiang.
 * @date 2019/1/22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NetworkInterceptors {
    /**
     * 设置netWorkInterceptor
     *
     * @return 返回一个继承 Interceptors 的类
     */
    Class<? extends Interceptor>[] value();
}
