package com.fh.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：分布式锁, 处理幂等
 * @author Ajie
 * @date 2020/4/30 0030
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheLock {

    /** key前缀 */
    String prefix() default "";

    /** 过期秒数,默认为5秒 */
    int expire() default 5;

    /** 超时时间单位，默认为秒 */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /** Key的分隔符（默认 :）  */
    String delimiter() default ":";

}
