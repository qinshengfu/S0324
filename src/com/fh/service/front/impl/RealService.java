package com.fh.service.front.impl;

import cn.hutool.core.date.DateUtil;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.front.RealManager;
import com.fh.util.PageData;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 说明： 实名认证表
 * 创建人：李海杰
 * 创建时间：2020-04-09
 */
@Service("realService")
@CacheConfig(cacheNames = "s0324_real")
public class RealService implements RealManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void save(PageData pd) throws Exception {
        pd.put("REAL_ID", "");    //主键 自增
        pd.put("GMT_CREATE", DateUtil.now());    //创建时间
        pd.put("GMT_MODIFIED", DateUtil.now());    //更新时间
        dao.save("RealMapper.save", pd);
    }

    /**
     * 删除
     *
     * @param pd
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void delete(PageData pd) throws Exception {
        dao.delete("RealMapper.delete", pd);
    }

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void edit(PageData pd) throws Exception {
        pd.put("GMT_MODIFIED", DateUtil.now());
        dao.update("RealMapper.edit", pd);
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @Cacheable
    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("RealMapper.datalistPage", page);
    }

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
    @Cacheable
    public List<PageData> listAll(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("RealMapper.listAll", pd);
    }

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("RealMapper.findById", pd);
    }

    /**
     * 通过用户id获取数据
     *
     * @param pd
     * @throws Exception
     */
    @Override
    @Cacheable
    public PageData findByUserId(PageData pd) throws Exception {
        return (PageData) dao.findForObject("RealMapper.findByUserId", pd);
    }

    /**
     * 批量删除
     *
     * @param ArrayDATA_IDS
     * @throws Exception
     */
    @Cacheable
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
        dao.delete("RealMapper.deleteAll", ArrayDATA_IDS);
    }

}

