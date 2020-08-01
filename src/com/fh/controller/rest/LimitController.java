package com.fh.controller.rest;

import com.fh.annotation.Limit;
import com.fh.entity.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 功能描述：限流注解测试
 * @author Ajie
 * @date 2020/4/28 0028
 */
@RestController
@RequestMapping(value = "release/api/limit")
public class LimitController {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    /**
     * 功能描述：下面配置说明该接口 60秒内最多只能访问 6次，保存到redis的键名为 s0324前缀+ip+接口路径，
     * @author Ajie
     * @date 2020/4/28 0028
     */
    @GetMapping
    @Limit(name = "测试限流注解", prefix = "s0324", period = 60, count = 6)
    public R limitTest() {
        return R.ok().data("count", ATOMIC_INTEGER.incrementAndGet());
    }

}
