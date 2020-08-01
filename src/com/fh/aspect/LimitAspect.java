package com.fh.aspect;

import com.fh.annotation.Limit;
import com.fh.entity.result.R;
import com.fh.util.RequestUtil;
import com.fh.util.StringUtil;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：Spring自定义注解+redis实现接口限流
 *
 * @author Ajie
 * @date 2020/4/28 0028
 */
@Aspect
@Component
public class LimitAspect {

    private final RedisTemplate<Object, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(LimitAspect.class);

    public LimitAspect(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * 功能描述：绑定自定义注解
     *
     * @author Ajie
     * @date 2020/4/28 0028
     */
    @Pointcut("@annotation(com.fh.annotation.Limit)")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = RequestUtil.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        Limit limit = signatureMethod.getAnnotation(Limit.class);
        LimitType limitType = limit.limitType();
        String key = limit.key();
        if (StringUtil.isEmpty(key)) {
            if (limitType == LimitType.IP) {
                key = StringUtil.getIp(request);
            } else {
                key = signatureMethod.getName();
            }
        }

        ImmutableList<Object> keys = ImmutableList.of(StringUtil.join(limit.prefix(), "_", key, "_", request.getRequestURI().replaceAll("/", "_")));

        long count = checkWithRedis(limit, keys.toString());
        if (count <= limit.count()) {
            logger.info("第{}次访问key为 {}，描述为 [{}] 的接口", count, keys, limit.name());
            return joinPoint.proceed();
        } else {
            return R.error().message("请求过于频繁,超出限制!");
        }

    }

    /**
     * 以redis实现请求记录
     *
     * @param limit 注解参数
     * @param key 存入Redis的键名
     */
    private long checkWithRedis(Limit limit, String key) {
        long count = redisTemplate.opsForValue().increment(key, 1);
        if (count == 1) {
            redisTemplate.expire(key, limit.period(), TimeUnit.SECONDS);
        }
        return count;
    }


}
