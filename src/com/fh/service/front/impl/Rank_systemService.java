package com.fh.service.front.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import com.fh.util.Tools;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.front.Rank_systemManager;

/** 
 * 说明： 等级制度
 * 创建人：
 * 创建时间：2020-06-15
 * @version
 */
@Service("rank_systemService")
@CacheConfig(cacheNames = "s0324_RankSystem")
public class Rank_systemService implements Rank_systemManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void save(PageData pd)throws Exception{
		pd.put("GMT_CREATE", DateUtil.now());	//创建时间
		pd.put("GMT_MODIFIED", DateUtil.now());	//更新时间
		dao.save("Rank_systemMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void delete(PageData pd)throws Exception{
		dao.delete("Rank_systemMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void edit(PageData pd)throws Exception{
		pd.put("GMT_MODIFIED", DateUtil.now());	//更新时间
		dao.update("Rank_systemMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("Rank_systemMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Rank_systemMapper.listAll", pd);
	}

	/**列表(根据人数排序)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listAllByNumPeople(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Rank_systemMapper.listAllByNumPeople", pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Rank_systemMapper.findById", pd);
	}

	/**获取最小直推人数的等级且大于0
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData findByMinRank(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Rank_systemMapper.findByMinRank", pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("Rank_systemMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

