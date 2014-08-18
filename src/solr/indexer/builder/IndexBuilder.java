package com.gs.cvoud.solr.indexer.builder;

import java.util.List;

/**
 * 索引建立者，实际上这个接口不是一个纯粹意义上的索引建立者，真正的索引建立接口应该是Indexer， Indexer会要求直接和solr服务器建立连接，提交索引。
 * 而IndexBuilder只是利用Indexer建立索引，当然也要依赖ObjectBuilder获取简历索引的资源信息等 所以IndexBuilder更像是一个简历索引的统筹管理者。
 * 
 * @author boyce
 * @version 2013-7-11
 */
public interface IndexBuilder {
	/**
	 * 构建索引
	 * 
	 * @param isPage
	 *            是否分页
	 * @return
	 */
	public void rebuildIndex();

	/**
	 * 更新索引
	 * 
	 * @return
	 */
	public void updateIndex();

	public void updateIndex(Long userId);

	public void deleteByIds(List<String> ids);

	public void deleteById(String id);

	public void deleteAllIndex();
	
	public void releaseLock();
}
