package com.fh.service.record.impl;

import cn.hutool.core.date.DateUtil;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.record.Score_recManager;
import com.fh.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/** 
 * 说明： 娱乐积分钱包记录
 * 创建人：
 * 创建时间：2020-03-25
 * @version
 */
@Service("score_recService")
public class Score_recService implements Score_recManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void save(PageData pd)throws Exception{
		pd.put("SCORE_REC_ID", "");	//主键 自增
		pd.put("GMT_CREATE", DateUtil.now());	//创建时间
		pd.put("GMT_MODIFIED", DateUtil.now());	//更新时间
		dao.save("Score_recMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("Score_recMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("Score_recMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("Score_recMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Score_recMapper.listAll", pd);
	}

	/**列表(根据用户ID)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listByUserId(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Score_recMapper.listByUserId", pd);
	}

	/**列表(根据用户ID和类型)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listByUserIdAndType(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Score_recMapper.listByUserIdAndType", pd);
	}

	/**列表(根据用户id和状态)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listByUserIdAndStatus(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Score_recMapper.listByUserIdAndStatus", pd);
	}

	/**列表(按推荐路径和状态和对方用户ID)
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public List<PageData> listByPathAndStatus(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("Score_recMapper.listByPathAndStatus", pd);
	}



	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("Score_recMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("Score_recMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

