package com.fh.service.record;

import com.fh.entity.Page;
import com.fh.util.PageData;

import java.util.List;

/** 
 * 说明： 跑单收益记录接口
 * 创建人：
 * 创建时间：2020-04-02
 * @version
 */
public interface Profit_recordManager{

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
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;

	/**列表(根据用户ID和状态)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listByUserIdAndStatus(PageData pd)throws Exception;

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;

	/**通过用户ID状态获取记录
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> findByOrderIdAndStatus(PageData pd)throws Exception;

	/**通过跑单ID和状态获取已发放的总额
	 * @param pd
	 * @throws Exception
	 */
	PageData getBonusSumByOrderId(PageData pd)throws Exception;

	/**通过跑单ID和状态获取已发放的本金总额
	 * @param pd
	 * @throws Exception
	 */
	PageData getMoneySumByOrderId(PageData pd)throws Exception;

	/**通过用户ID获取今日收益总额
	 * 	 * @param pd
	 * @throws Exception
	 */
	PageData getDayEarningsSumByUserId(PageData pd)throws Exception;

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

