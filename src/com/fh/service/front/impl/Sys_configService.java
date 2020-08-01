package com.fh.service.front.impl;

import com.fh.dao.DaoSupport;
import com.fh.service.front.Sys_configManager;
import com.fh.util.PageData;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/** 
 * 说明： 系统参数配置
 * 创建人：
 * 创建时间：2020-03-25
 * @version
 */
@Service("sys_configService")
@CacheConfig(cacheNames = "S0324_Sys_configService")
public class Sys_configService implements Sys_configManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void edit(PageData pd)throws Exception{
		dao.update("Sys_configMapper.edit", pd);
	}
	
	/**清空所有表
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void deleteAllTable(PageData pd)throws Exception{
		for (Object var : pd.keySet()) {
			dao.delete("Sys_configMapper.deleteAllTable", var);
		}
	}

	/**重置序列
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void reset_seq(PageData pd)throws Exception{
		dao.update("Sys_configMapper.reset_seq", pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData findById(PageData pd)throws Exception{
		pd.put("SYS_CONFIG_ID", "1");
		return (PageData)dao.findForObject("Sys_configMapper.findById", pd);
	}

}

