package com.fh.service.front;

import com.fh.util.PageData;

/** 
 * 说明： 系统参数配置接口
 * 创建人：
 * 创建时间：2020-03-25
 * @version
 */
public interface Sys_configManager{

	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd)throws Exception;

	/**清空所有表
	 * @param pd
	 * @throws Exception
	 */
	void deleteAllTable(PageData pd)throws Exception;

	/**重置序列
	 * @param pd
	 * @throws Exception
	 */
	void reset_seq(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd)throws Exception;
	

}

