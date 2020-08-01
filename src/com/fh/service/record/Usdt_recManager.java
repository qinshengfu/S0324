package com.fh.service.record;

import com.fh.entity.Page;
import com.fh.util.PageData;

import java.util.List;

/** 
 * 说明： USDT钱包记录接口
 * 创建人：
 * 创建时间：2020-03-25
 * @version
 */
public interface Usdt_recManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;

	/**提现列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> withdrawalsList(Page page)throws Exception;

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**列表(按用户id)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listByUserId(PageData pd)throws Exception;

	/**列表(按用户id和正负)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listByUserIdAndTag(PageData pd)throws Exception;

	/**列表(按用户id和类型)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listByUserIdAndType(PageData pd)throws Exception;

	/**列表(按推荐路径和状态和对方用户ID)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listByPathAndStatus(PageData pd)throws Exception;

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;

	/**根据用户ID获取今日动态收益总额
	 * @param pd
	 * @throws Exception
	 */
	PageData getDayEarningsSumByUserId(PageData pd)throws Exception;

	/**通过用户id和类型获取总和
	 * @param pd
	 * @throws Exception
	 */
	PageData findByUserIdAndType(PageData pd)throws Exception;

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

