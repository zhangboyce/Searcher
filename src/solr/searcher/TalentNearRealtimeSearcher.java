package com.gs.cvoud.solr.search;

import java.util.List;

import com.gs.cvoud.model.Talent;
import com.gs.cvoud.service.BaseService;
import com.gs.cvoud.solr.indexer.builder.IndexBuilder;
import com.gs.cvoud.solr.search.query.command.Command;
import com.gs.cvoud.solr.search.response.TalentSearchResponse;
import com.gs.cvoud.util.ObjectUtils;

/**
 * talent 近实时搜索
 * 
 * @author boyce
 * @version 2013-11-12
 */
public class TalentNearRealtimeSearcher implements Searcher<Talent> {
	private Searcher<Talent> searcher;
	private IndexBuilder builder;

	public TalentNearRealtimeSearcher(Searcher<Talent> searcher, IndexBuilder builder) {
		if (ObjectUtils.hasNull(searcher, builder))
			throw new NullPointerException("Cannot constract a TalentNearRealtimeSearcher with null param.");

		this.searcher = searcher;
		this.builder = builder;
	}

	public TalentSearchResponse search(Command command) {
		this.updateIndex();
		TalentSearchResponse response = searcher.search(command);
		return response;
	}

	public Talent search(String uniqueKey) {
		this.updateIndex();
		Talent talent = searcher.search(uniqueKey);
		return talent;
	}

	public TalentSearchResponse stat(Command command) {
		this.updateIndex();
		TalentSearchResponse response = searcher.stat(command);
		return response;
	}
	
	private void updateIndex() {
		// 获取当前用户id
		Long currentUserId = BaseService.getCurrentUser().getId();
		// 更新该用户的索引
		builder.updateIndex(currentUserId);
	}
	
	public TalentSearchResponse search(List<String> uniqueKeys) {
		this.updateIndex();
		return searcher.search(uniqueKeys);
	}
	
	public TalentSearchResponse search(List<String> uniqueKeys, Command command) {
		this.updateIndex();
		return searcher.search(uniqueKeys, command);
	}
	
}
