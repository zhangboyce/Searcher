package com.gs.cvoud.solr.indexer;

import java.util.List;

/**
 * solr索引接口，提供向solr服务器提交索引的接口
 * 
 * @author boyce
 * @version 2013-7-8
 */
public interface Indexer<T> {
	/**
	 * 批量添加索引
	 * 
	 * @param ts
	 * @return
	 */
	public List<Long> batchAddObject(List<T> ts);

	/**
	 * 添加单个对象索引
	 * 
	 * @param t
	 * @return
	 */
	public void addObject(T t);

	/**
	 * 更新索引
	 * 
	 * @param t
	 * @return
	 */
	public void updateObject(T t);

	/**
	 * 根据Id批量删除所有
	 * 
	 * @param keys
	 */
	public void removeObjects(List<String> keys);

	/**
	 * 提交索引
	 */
	public void commit();

	/**
	 * 优化索引
	 */
	public void optimize();

	/**
	 * 清楚solr的全部索引
	 */
	public void removeAll();

}
