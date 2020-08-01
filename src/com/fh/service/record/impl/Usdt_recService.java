package com.fh.service.record.impl;

import cn.hutool.core.date.DateUtil;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.record.Usdt_recManager;
import com.fh.util.PageData;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/** 
 * 说明： USDT钱包记录
 * 创建人：
 * 创建时间：2020-03-25
 * @version
 */
@CacheConfig(cacheNames = "s0324_userRec")
@Service("usdt_recService")
public class Usdt_recService implements Usdt_recManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@CacheEvict(allEntries = true)
	public void save(PageData pd)throws Exception{
		pd.put("USDT_REC_ID", "");	//主键 自增
		pd.put("GMT_CREATE", DateUtil.now());	//创建时间
		pd.put("GMT_MODIFIED", DateUtil.now());	//更新时间
		dao.save("Usdt_recMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	@CacheEvict(allEntries = true)
	public void delete(PageData pd)throws Exception{
		dao.delete("Usdt_recMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@CacheEvict(allEntries = true)
	public void edit(PageData pd)throws Exception{
		dao.update("Usdt_recMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Cacheable
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("Usdt_recMapper.datalistPage", page);
	}

	/**提现列表
	 * @param page
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> withdrawalsList(Page page)throws Exception{
		return (List<PageData>)dao.findForList("Usdt_recMapper.withdrawalsListPage", page);
	}

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Cacheable
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Usdt_recMapper.listAll", pd);
	}

	/**列表(按用户id)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByUserId(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Usdt_recMapper.listByUserId", pd);
	}

	/**列表(按用户id和正负)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByUserIdAndTag(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Usdt_recMapper.listByUserIdAndTag", pd);
	}

	/**列表(按用户id和类型)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByUserIdAndType(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Usdt_recMapper.listByUserIdAndType", pd);
	}

	/**列表(按推荐路径和状态和对方用户ID)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByPathAndStatus(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Usdt_recMapper.listByPathAndStatus", pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Cacheable
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Usdt_recMapper.findById", pd);
	}

	/**根据用户ID获取今日动态收益总额
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData getDayEarningsSumByUserId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Usdt_recMapper.getDayEarningsSumByUserId", pd);
	}

	/**通过用户id和类型获取总和
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData findByUserIdAndType(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Usdt_recMapper.findByUserIdAndType", pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("Usdt_recMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

