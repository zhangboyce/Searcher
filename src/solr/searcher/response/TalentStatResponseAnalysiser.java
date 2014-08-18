package com.gs.cvoud.solr.search.response;

import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import com.gs.cvoud.solr.annotation.Field;
import com.gs.cvoud.util.CollectionUtils;
import com.gs.cvoud.util.ObjectUtils;

/**
 * 对solr搜索的响应对象QueryResponse解析的接口，统计solr搜索数据。
 * 
 * @author boyce
 * @version 2013-9-16
 */
public class TalentStatResponseAnalysiser {
	/**
	 * solr响应对象
	 */
	private QueryResponse queryResponse;

	public TalentStatResponseAnalysiser(QueryResponse queryResponse) {
		if (ObjectUtils.hasNull(queryResponse)) {
			throw new IllegalArgumentException(
					"Cannot constract a TalentQueryResponseAnalysiser beacause the null param.");
		}
		this.queryResponse = queryResponse;
	}

	/**
	 * 分布统计
	 */
	public DistributionResultStat analyDistributionResultStat(List<FilterItemGroup> filterItemGroups) {
		// 候选人分布统计
		DistributionResultStat distributionResultStat = new DistributionResultStat(this.getTotal());
		for (Field field : DistributionResultStat.STAT_ORIGINAL_FIELD) {
			this.statDistribution(filterItemGroups, field, distributionResultStat);
		}
		return distributionResultStat;
	}

	// 分布统计
	private void statDistribution(List<FilterItemGroup> filterItemGroups, Field field,
			DistributionResultStat distributionResultStat) {
		
		FilterItemGroup filterItemGroup = null;
		for (FilterItemGroup group: filterItemGroups) {
			if (group.getKey().equals(field.getName()))
				filterItemGroup = group;
		}
		
		if (ObjectUtils.isNotNull(filterItemGroup)) {
			List<FilterItem> filterItems = filterItemGroup.getFilterItems();
			// 某个域下没有信息
			if (CollectionUtils.isEmpty(filterItems))
				return;

			for (FilterItem item : filterItems) {
				// Map中的key不为默认的空值key
				distributionResultStat.addStatField(field, item.getKey(), item.getValue());
			}
		}
	}

	/**
	 * 获取响应对象中的结果集条数
	 * 
	 * @return solr响应对象中的结果集总数
	 */
	private int getTotal() {
		SolrDocumentList list = queryResponse.getResults();
		if (CollectionUtils.isNotEmpty(list)) {
			return (int) list.getNumFound();
		}

		return 0;
	}

}
