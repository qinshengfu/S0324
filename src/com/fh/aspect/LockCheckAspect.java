package com.fh.aspect;

import com.fh.annotation.CacheLock;
import com.fh.dao.RedisDao;
import com.fh.entity.MemUser;
import com.fh.entity.result.R;
import com.fh.util.*;
import org.apache.shiro.session.Session;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 功能描述：分布式锁 切面类
 *
 * @author Ajie
 * @date 2020/4/30 0030
 */
@Aspect
@Component
public class LockCheckAspect {

    /**
     * lua
     */
    private static final String RELEASE_LOCK_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @Resource(name = "redisTemplate")
    private RedisTemplate<Object, Object> redisTemplate;

    // 增强带有CacheLock注解的方法
    @Pointcut("@annotation(com.fh.annotation.CacheLock)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = RequestUtil.getHttpServletRequest();
        PageData pd = new PageData(request);

        // 可以根据业务获取用户唯一的个人信息，例如手机号码
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        String userName = "";
        if (user != null) {
            userName = user.getUSER_NAME();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        CacheLock cacheLock = signatureMethod.getAnnotation(CacheLock.class);
        String prefix = cacheLock.prefix();
        if (StringUtil.isBlank(prefix)) {
            throw new RuntimeException("缓存锁定前缀不能为空");
        }
        // 分隔符
        String delimiter = cacheLock.delimiter();
        // 拼接 key
        StringBuilder sb = new StringBuilder();
        // 先拼接前缀和用户
        sb.append(prefix).append(delimiter).append(userName).append(delimiter);
        for (Object key : pd.keySet()) {
            sb.append(key).append(delimiter).append(pd.get(key));
        }
        final String lockKey = sb.toString();

        final String uuid = cn.hutool.core.lang.UUID.fastUUID().toString();
        // 获取锁
        boolean success = tryLock(lockKey, uuid, cacheLock.expire(), cacheLock.timeUnit());
        if (success) {
            // 获取锁成功
            try {
                // aop代理链执行的方法
                return joinPoint.proceed();
            } finally {
                /*  最后记得释放锁
                 * 据key从redis中获取value */
                String value = redisTemplate.opsForValue().get(lockKey).toString();
                if (uuid.equals(value)) {
                    // 解锁
                    boolean isSuccess = releaseLock(lockKey, uuid);
                    if (isSuccess) {
                        redisTemplate.delete(lockKey);
                    }
                }
            }
        } else {
            // 添加锁失败，认为是重复提交的请求
            return R.error().code(403).message("重复请求，请稍后再试");
        }
    }

    /**
     * 该加锁方法仅针对单实例 Redis 可实现分布式加锁
     * 对于 Redis 集群则无法使用
     * <p>
     * 支持重复，线程安全
     *
     * @param lockKey  加锁键
     * @param clientId 加锁客户端唯一标识(采用UUID)
     * @param seconds  锁过期时间
     * @param unit     过期单位
     * @return 成功返回 true
     */
    public boolean tryLock(String lockKey, String clientId, int seconds, TimeUnit unit) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, clientId);
            if (result) {
                redisTemplate.expire(lockKey, seconds, unit);
            }
            return result;
        });
    }


    /**
     * 与 tryLock 相对应，用作释放锁
     *
     * @param lockKey
     * @param UUID
     */
    public Boolean releaseLock(String lockKey, String UUID) {

        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            Object result = jedis.eval(RELEASE_LOCK_LUA_SCRIPT, Collections.singletonList(lockKey),
                    Collections.singletonList(UUID));
            return "0".equals(result.toString());
        });
    }


}
