package com.fh.service.record.impl;

import cn.hutool.core.date.DateUtil;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.record.Profit_recordManager;
import com.fh.util.PageData;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/** 
 * 说明： 跑单收益记录
 * 创建人：
 * 创建时间：2020-04-02
 * @version
 */
@CacheConfig(cacheNames = "s0324_profitRec")
@Service("profit_recordService")
public class Profit_recordService implements Profit_recordManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	@CacheEvict(allEntries = true)
	public void save(PageData pd)throws Exception{
		pd.put("PROFIT_RECORD_ID", "");	//主键 自增
		pd.put("GMT_CREATE", DateUtil.now());	//创建时间
		dao.save("Profit_recordMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	@CacheEvict(allEntries = true)
	public void delete(PageData pd)throws Exception{
		dao.delete("Profit_recordMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@CacheEvict(allEntries = true)
	public void edit(PageData pd)throws Exception{
		dao.update("Profit_recordMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Cacheable
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("Profit_recordMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Cacheable
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Profit_recordMapper.listAll", pd);
	}

	/**列表(根据用户ID和状态)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByUserIdAndStatus(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Profit_recordMapper.listByUserIdAndStatus", pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Profit_recordMapper.findById", pd);
	}

	/**通过用户ID状态获取记录
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> findByOrderIdAndStatus(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("Profit_recordMapper.findByOrderIdAndStatus", pd);
	}

	/**通过跑单ID和状态获取已发放的总额
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData getBonusSumByOrderId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Profit_recordMapper.getBonusSumByOrderId", pd);
	}

	/**通过跑单ID和状态获取已发放的本金总额
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData getMoneySumByOrderId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Profit_recordMapper.getMoneySumByOrderId", pd);
	}

	/**通过用户ID获取今日收益总额
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData getDayEarningsSumByUserId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Profit_recordMapper.getDayEarningsSumByUserId", pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("Profit_recordMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

