package com.gs.cvoud.solr.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import com.gs.cvoud.model.Talent;
import com.gs.cvoud.solr.TalentConvertor;
import com.gs.cvoud.solr.search.query.command.Command;
import com.gs.cvoud.solr.search.response.TalentSearchResponse;
import com.gs.cvoud.util.CollectionUtils;
import com.gs.cvoud.util.ObjectUtils;

/**
 * 候选人搜索，在solr中搜索
 * 
 * @author boyce
 * @version 2013-7-11
 */
public class TalentSearcher implements Searcher<Talent> {
	private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TalentSearcher.class);

	private SolrServer server;

	/**
	 * @param server
	 *            solr服务器
	 * @param convertor
	 *            转换器
	 * @param solrConfig
	 *            solr服务器配置信息
	 */
	public TalentSearcher(SolrServer server) {
		if (ObjectUtils.isNull(server)) {
			throw new IllegalArgumentException("Cannot constract a searcher with null argument.");
		}

		this.server = server;
	}

	/**
	 * 根据SearchRequest请求搜索SearchResponse响应对像
	 */
	public TalentSearchResponse search(Command command) {
		QueryResponse response = this.query(command.convert());
		if (ObjectUtils.isNull(response))
			return null;
		
		TalentSearchResponse resp = new TalentSearchResponse();
		resp.analysis(response);
		return resp;
	}

	/**
	 * 根据SearchRequest请求搜索SearchResponse响应对像
	 */
	public TalentSearchResponse stat(Command command) {
		QueryResponse response = this.query(command.convert());
		if (ObjectUtils.isNull(response))
			return null;
		
		TalentSearchResponse resp = new TalentSearchResponse();
		resp.analysisStat(response);
		return resp;
	}

	/**
	 * 根据id搜索talent
	 */
	public Talent search(String uniqueKey) {
		SolrQuery params = new SolrQuery();
		params.setQuery("id:" + uniqueKey);
		QueryResponse response = this.query(params);
		if (ObjectUtils.isNull(response))
			return null;
		
		Talent talent = null;
		SolrDocumentList list = response.getResults();
		if (CollectionUtils.isNotEmpty(list)) {
			talent = TalentConvertor.toObject(list.get(0));
		}
		return talent;
	}

	/**
	 * 根据id集合搜索
	 */
	public TalentSearchResponse search(List<String> uniqueKeys, Command command) {
		if (CollectionUtils.isNotEmpty(uniqueKeys)) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0, s = uniqueKeys.size(); i < s; i++) {
				if (i != s - 1)
					builder.append("id:").append(uniqueKeys.get(i)).append(" OR ");
				else
					builder.append("id:").append(uniqueKeys.get(i));
			}

			TalentSearchResponse resp = new TalentSearchResponse();
			SolrQuery params = null;
			if (ObjectUtils.isNull(command)) {
				params = new SolrQuery();
				params.setQuery(builder.toString());
			}
			else 
				params = command.convert(builder.toString());
			
			QueryResponse response = this.query(params);
			resp.analysis(response);
			return resp;
		}
		return null;
	}
	
	public TalentSearchResponse search(List<String> uniqueKeys) {
		return this.search(uniqueKeys, null);
	}

	// 根据 SolrQuery 查询 QueryResponse
	private QueryResponse query(SolrQuery solrQuery) {
		try {
			logger.info("Begain to query from solr: " + solrQuery.toString());
			long start = System.currentTimeMillis();

			QueryResponse response = server.query(solrQuery);

			long end = System.currentTimeMillis();
			logger.info("End to query from solr and take: " + (end - start) + " ms. And result: ("
					+ response.getResults().getNumFound() + ")个.");
			return response;

		} catch (SolrServerException e) {
			logger.error("Error.", e);
		}

		return null;
	}

	public static void main(String[] args) {
		TalentSearcher searcher = new TalentSearcher(null);
		List<String> uniqueKeys = new ArrayList<String>();
		uniqueKeys.add("22222");
		uniqueKeys.add("33333");
		uniqueKeys.add("44444");
		uniqueKeys.add("55555");
		searcher.search(uniqueKeys);
	}
}
