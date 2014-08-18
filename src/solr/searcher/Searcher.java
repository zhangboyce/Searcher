package com.gs.cvoud.solr.search;

import java.util.List;

import com.gs.cvoud.solr.search.query.command.Command;
import com.gs.cvoud.solr.search.response.TalentSearchResponse;

/**
 * Solr搜索接口
 * 
 * @author boyce
 * @version 2013-7-8
 */
public interface Searcher<T> {
	public TalentSearchResponse search(Command command);

	/**
	 * 统计候选人属性，缺失属性，已填写属性的数量以及百分比，
	 */
	public TalentSearchResponse stat(Command command);

	public T search(String uniqueKey);
	
	public TalentSearchResponse search(List<String> uniqueKeys, Command command);

	public TalentSearchResponse search(List<String> uniqueKeys);
}
