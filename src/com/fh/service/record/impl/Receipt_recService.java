package com.fh.service.record.impl;

import cn.hutool.core.date.DateUtil;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.record.Receipt_recManager;
import com.fh.util.PageData;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/** 
 * 说明： 跑单记录
 * 创建人：
 * 创建时间：2020-03-25
 * @version
 */
@CacheConfig(cacheNames = "s0324_receiptRec")
@Service("receipt_recService")
public class Receipt_recService implements Receipt_recManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	@CacheEvict(allEntries = true)
	public void save(PageData pd)throws Exception{
		pd.put("RECEIPT_REC_ID", "");	//主键 自增
		pd.put("GMT_CREATE", DateUtil.now());	//创建时间
		dao.save("Receipt_recMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	@CacheEvict(allEntries = true)
	public void delete(PageData pd)throws Exception{
		dao.delete("Receipt_recMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@CacheEvict(allEntries = true)
	public void edit(PageData pd)throws Exception{
		dao.update("Receipt_recMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Cacheable
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("Receipt_recMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@Cacheable
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Receipt_recMapper.listAll", pd);
	}
	
	/**列表(根据用户ID)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public List<PageData> listByUserId(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Receipt_recMapper.listByUserId", pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Cacheable
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Receipt_recMapper.findById", pd);
	}

	/**根据用户id获取最新一条记录
	 * @param pd
	 * @throws Exception
	 */
	@Override
	@Cacheable
	public PageData getLatestRecord(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Receipt_recMapper.getLatestRecord", pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("Receipt_recMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

