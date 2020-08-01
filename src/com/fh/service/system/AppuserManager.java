package com.fh.service.system;

import com.fh.entity.Page;
import com.fh.util.PageData;

import java.util.List;


/** 会员接口类
 * @author fh313596790qq(青苔)
 */
public interface AppuserManager {
	
	/**列出某角色下的所有会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllAppuserByRorlid(PageData pd) throws Exception;
	
	/**会员列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listPdPageUser(Page page)throws Exception;
	
	/**通过用户名获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUsername(PageData pd)throws Exception;
	
	/**通过邮箱获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByEmail(PageData pd)throws Exception;
	
	/**通过编号获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByNumber(PageData pd)throws Exception;
	
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveU(PageData pd)throws Exception;
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteU(PageData pd)throws Exception;
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editU(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUiId(PageData pd)throws Exception;
	
	/**全部会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllUser(PageData pd)throws Exception;
	
	/**批量删除用户
	 * @param USER_IDS
	 * @throws Exception
	 */
	public void deleteAllU(String[] USER_IDS)throws Exception;
	
	/**获取会员总数
	 * @throws Exception
	 */
	PageData getAppUserCount(String value)throws Exception;

	/**获取今日注册会员总数
	 * @throws Exception
	 */
	PageData getAppDayUserCount(String value)throws Exception;

	/**累积排单金额统计
	 * @throws Exception
	 */
	PageData getBuyCount(String value)throws Exception;

	/**今日排单金额统计
	 * @throws Exception
	 */
	PageData getDayBuyCount(String value)throws Exception;

	/**累积提现金额统计
	 * @throws Exception
	 */
	PageData getSellCount(String value)throws Exception;

	/**今日提现金额统计
	 * @throws Exception
	 */
	PageData getDaySellCount(String value)throws Exception;

	/**累积匹配金额统计
	 * @throws Exception
	 */
	PageData getMatchCount(String value)throws Exception;

	/**今日匹配金额统计
	 * @throws Exception
	 */
	PageData getDayMatchCount(String value)throws Exception;
}

