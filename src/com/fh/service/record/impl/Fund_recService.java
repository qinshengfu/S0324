package com.fh.service.record.impl;

import cn.hutool.core.date.DateUtil;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.record.Fund_recManager;
import com.fh.util.PageData;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/** 
 * 说明： 分享基金钱包记录
 * 创建人：
 * 创建时间：2020-03-25
 * @version
 */
@CacheConfig(cacheNames = "s0324_fundRec")
@Service("fund_recService")
public class Fund_recService implements Fund_recManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	@CacheEvict(allEntries = true)
	@Override
	public void save(PageData pd)throws Exception{
		pd.put("FUND_REC_ID", "");	//主键
		pd.put("GMT_CREATE", DateUtil.now());	//创建时间
		pd.put("GMT_MODIFIED", DateUtil.now());	//更新时间
		dao.save("Fund_recMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("Fund_recMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@CacheEvict(allEntries = true)
	public void edit(PageData pd)throws Exception{
		dao.update("Fund_recMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Cacheable
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("Fund_recMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Cacheable
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Fund_recMapper.listAll", pd);
	}

	/**列表(根据用户ID)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByUserId(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Fund_recMapper.listByUserId", pd);
	}

	/**列表(根据用户id和类型)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByUserIdAndType(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Fund_recMapper.listByUserIdAndType", pd);
	}

	/**列表(根据用户id和状态)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByUserIdAndStatus(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Fund_recMapper.listByUserIdAndStatus", pd);
	}

	/**列表(按推荐路径和状态和对方用户ID)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByPathAndStatus(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Fund_recMapper.listByPathAndStatus", pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Cacheable
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Fund_recMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("Fund_recMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

