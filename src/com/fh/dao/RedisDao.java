package com.fh.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;


/** 
 * 说明： Redis缓存API
 * 创建人：FH Q313596790
 * 修改时间：2018-05-2
 * @version 1.0
 */
public interface RedisDao {
	
	/**新增(存储字符串)
	 * @param key 键
	 * @param value 值
	 * @return boolean
	 */
	public boolean addString(String key, String value);
	
	/**拼接字符串
	 * @param key 键
	 * @param value 值
	 * @return boolean
	 */
	public boolean appendString(String key, String value);
	
	/**新增(存储Map)
	 * @param key 键
	 * @param map 值
	 * @return String
	 */
	public String addMap(String key, Map<String, String> map);
	
	/**获取map
	 * @param key 键
	 * @return map 集合
	 */
	public Map<String,String> getMap(String key);
	
	/**新增(存储List)
	 * @param key 键
	 * @param list 值
	 */
	public void addList(String key, List<String> list);
	
	/**获取List
	 * @param key 键
	 * @return list 集合
	 */
	public List<String> getList(String key);
	
	/**新增(存储set)
	 * @param key 键
	 * @param set 值
	 */
	public void addSet(String key, Set<String> set);
	
	/**获取Set
	 * @param key 键
	 * @return set<string> 值
	 */
	public Set<String> getSet(String key);
	
	/**删除
	 * @param key 键
	 */
	public boolean delete(String key); 
	
	/**删除多个 
	 * @param keys 键
	 */
	public void delete(List<String> keys);

	/**清空缓存数据库
	 */
	void deleteAll();

	/**修改
	 * @param key 键
	 * @param value 值
	 * @return boolean
	 */
	public boolean eidt(String key, String value);
	
	/**通过ket获取数据
	 * @param keyId 键
	 * @return string
	 */
	public String get(String keyId);



}
