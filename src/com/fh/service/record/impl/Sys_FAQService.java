package com.fh.service.record.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.record.Sys_FAQManager;

/**
 * 说明： 常见问题
 * 创建人：
 * 创建时间：2020-06-22
 */
@Service("sys_faqService")
@CacheConfig(cacheNames = "s0324_Sys_FAQService")
public class Sys_FAQService implements Sys_FAQManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
	@Override
	@CacheEvict(allEntries = true)
    public void save(PageData pd) throws Exception {
        dao.save("Sys_FAQMapper.save", pd);
    }

    /**
     * 删除
     *
     * @param pd
     * @throws Exception
     */
	@Override
	@CacheEvict(allEntries = true)
    public void delete(PageData pd) throws Exception {
        dao.delete("Sys_FAQMapper.delete", pd);
    }

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
	@Override
	@CacheEvict(allEntries = true)
    public void edit(PageData pd) throws Exception {
        dao.update("Sys_FAQMapper.edit", pd);
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
	@Override
	@Cacheable
    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("Sys_FAQMapper.datalistPage", page);
    }

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
	@Override
	@Cacheable
    public List<PageData> listAll(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("Sys_FAQMapper.listAll", pd);
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
        return (PageData) dao.findForObject("Sys_FAQMapper.findById", pd);
    }

    /**
     * 批量删除
     *
     * @param ArrayDATA_IDS
     * @throws Exception
     */
	@Override
	@CacheEvict(allEntries = true)
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
        dao.delete("Sys_FAQMapper.deleteAll", ArrayDATA_IDS);
    }

}

