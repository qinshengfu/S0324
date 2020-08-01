package com.fh.controller.rest;

import com.fh.annotation.CacheLock;
import com.fh.controller.base.BaseController;
import com.fh.entity.MemUser;
import com.fh.entity.result.R;
import com.fh.util.Const;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 功能描述：分布式时锁实现幂等
 * @author Ajie
 * @date 2020/4/30 0030
 */
@RestController
@RequestMapping("release/api/lock")
public class LockController extends BaseController {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    @GetMapping
    @CacheLock(prefix = "s0324")
    public R lockTest() {

        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER);
        PageData pd = new PageData();
        pd.put("user", user);
        pd.put("count", ATOMIC_INTEGER.incrementAndGet());
        return R.ok().data(pd);
    }

}
