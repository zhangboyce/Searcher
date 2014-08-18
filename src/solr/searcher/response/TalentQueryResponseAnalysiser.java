package com.gs.cvoud.solr.search.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.gs.cvoud.model.Talent;
import com.gs.cvoud.solr.TalentConvertor;
import com.gs.cvoud.solr.annotation.AgeRangeFacetLevel;
import com.gs.cvoud.solr.annotation.OriginalField;
import com.gs.cvoud.solr.annotation.RangeFacetLevel;
import com.gs.cvoud.solr.annotation.SalaryRangeFacetLevel;
import com.gs.cvoud.solr.annotation.WorkYearRangeFacetLevel;
import com.gs.cvoud.solr.common.SolrHelper;
import com.gs.cvoud.util.CollectionUtils;
import com.gs.cvoud.util.ObjectUtils;

/**
 * 对solr搜索的响应对象QueryResponse解析的接口，可以将solr响应对象中的solr原生对象解析成Map集合等。
 * 
 * @author boyce
 * @version 2013-7-10
 */
public class TalentQueryResponseAnalysiser {
	/**
	 * solr响应对象
	 */
	private QueryResponse queryResponse;

	public TalentQueryResponseAnalysiser(QueryResponse queryResponse) {
		if (ObjectUtils.hasNull(queryResponse)) {
			throw new IllegalArgumentException(
					"Cannot constract a TalentQueryResponseAnalysiser beacause the null param.");
		}
		this.queryResponse = queryResponse;
	}

	/**
	 * 获取响应对象中的结果集条数
	 * 
	 * @return solr响应对象中的结果集总数
	 */
	public int getTotal() {
		SolrDocumentList list = queryResponse.getResults();
		if (CollectionUtils.isNotEmpty(list)) {
			return (int) list.getNumFound();
		}

		return 0;
	}

	/**
	 * 解析响应对象，获取JavaBean集合
	 * 
	 * @return JavaBean集合
	 */
	public List<Talent> analyObjectList() {
		SolrDocumentList list = queryResponse.getResults();

		// 输出得到Talent结果集talentlist
		List<Talent> talentlist = new ArrayList<Talent>();
		for (int i = 0; i < list.size(); i++) {
			SolrDocument doc = list.get(i);
			Talent talent = (Talent) TalentConvertor.toObject(doc);
			talentlist.add(talent);
		}

		return talentlist;
	}
	/**
	 * 解析响应对象，获取FacetField的Map集合
	 * 
	 * @return FacetField的Map集合
	 */
	@SuppressWarnings("rawtypes")
	public List<FilterItemGroup> analyzeFilterItemGroup() {
		// 输出facet结果，这部分是非数值字段的Facet
		List<FacetField> facets = queryResponse.getFacetFields();
		if (CollectionUtils.isEmpty(facets)) 
			return null;

		//声明对应的item对象
		List<FilterItemGroup> filterItemGroups = new ArrayList<FilterItemGroup>();
		FilterItemGroup filterItemGroup = null;
		FilterItem filterItem = null;
		List<FilterItem> filterItems = null;
		
		//遍历solr返回的FacetField集合
		for (FacetField facet : facets) {
			
			//一个FacetField对应的是一个分组集合，如行业的分组集合
			//一个FacetField.Count对应的是一个具体的分组条目和数量，如行业分组下面的：互联网:20
			String facetName = facet.getName();
			List<FacetField.Count> facetCounts = facet.getValues();
			if (CollectionUtils.isEmpty(facetCounts))
				continue;
			
			//创建一个FilterItemGroup对象
			filterItemGroup = new FilterItemGroup(facetName);
			filterItems = new ArrayList<FilterItem>();
			for (FacetField.Count count : facetCounts) {
				filterItem = new FilterItem(count.getName(), (int) count.getCount());
				filterItems.add(filterItem);
			}
			filterItemGroup.setFilterItems(SolrHelper.sortFilterItemByKey(filterItems));
			filterItemGroups.add(filterItemGroup);
		}
		
		// 输出数值字段的Facet结果，分别为年薪、年龄、工作年限三个字段
		List<RangeFacet> rangeFacets = queryResponse.getFacetRanges();
		if (CollectionUtils.isNotEmpty(rangeFacets)) {
			for (RangeFacet rfacet : rangeFacets) {
				List<RangeFacet.Count> facetCounts = rfacet.getCounts();
				if (CollectionUtils.isEmpty(facetCounts)) 
					continue;
				
				String facetFieldName = rfacet.getName();
				filterItemGroup = new FilterItemGroup(facetFieldName);
				
				// 年薪字段的Facet结果
				if (facetFieldName.equals(OriginalField.SALARY.getName())) {
					filterItems = this.countRangeFacet(OriginalField.SALARY, rfacet,
							new RangeFacetLevelGetter() {
								public RangeFacetLevel getRangeFacetLevel(int value) {
									return SalaryRangeFacetLevel.getRangeFacetLevelByValue(value);
								}
							});
					filterItemGroup.setFilterItems(filterItems);
				}

				// 年龄字段的Facet结果
				else if (facetFieldName.equals(OriginalField.BIRTH_YEAR.getName())) {
					filterItems = this.countRangeFacet(OriginalField.BIRTH_YEAR, rfacet,
							new RangeFacetLevelGetter() {
								public RangeFacetLevel getRangeFacetLevel(int value) {
									return AgeRangeFacetLevel.getRangeFacetLevelByValue(value);
								}
							});
					filterItemGroup.setFilterItems(filterItems);

				}
				// 工作年限字段的Facet结果
				else if (facetFieldName.equals(OriginalField.GRADUATE_YEAR.getName())) {
					filterItems = this.countRangeFacet(OriginalField.GRADUATE_YEAR, rfacet,
							new RangeFacetLevelGetter() {
								public RangeFacetLevel getRangeFacetLevel(int value) {
									return WorkYearRangeFacetLevel.getRangeFacetLevelByValue(value);
								}
							});
					filterItemGroup.setFilterItems(filterItems);
				}
				
				filterItemGroups.add(filterItemGroup);
			}
		}
		
		return SolrHelper.sortFilterItemGroupByKey(filterItemGroups);

	}

	// 计算分组数量
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<FilterItem> countRangeFacet(OriginalField field, RangeFacet rfacet, RangeFacetLevelGetter getter) {
		
		List<RangeFacet.Count> facetCounts = rfacet.getCounts();
		Map<RangeFacetLevel, Integer> map = new HashMap<RangeFacetLevel, Integer>();
		for (RangeFacet.Count count : facetCounts) {
			if (count.getCount() > 0) {
				// 获取solr分区间范围
				int value = Integer.parseInt(count.getValue());
				// 获取该值存在的区间范围
				RangeFacetLevel level = getter.getRangeFacetLevel(value);
				
				Integer sumCount = map.get(level);
				map.put(level, ObjectUtils.isNull(sumCount) ? count.getCount() : count.getCount()
						+ sumCount);
			}
		}
		Map<RangeFacetLevel, Integer> sorted = SolrHelper.sortRangeFacetLevelByKey(map);
		List<FilterItem> filterItems = new ArrayList<FilterItem>();
		for (Entry<RangeFacetLevel, Integer> entry: sorted.entrySet()) {
			filterItems.add(new FilterItem(entry.getKey().getName(), entry.getValue()));
		}
		return filterItems;
	}

	private static interface RangeFacetLevelGetter {
		public RangeFacetLevel getRangeFacetLevel(int value);
	}

	/**
	 * 解析响应对象，获取高亮的Map集合
	 * 
	 * @return 高亮的Map集合
	 */
	public Map<String, Map<String, List<String>>> analyHighlightMap() {
		Map<String, Map<String, List<String>>> map = queryResponse.getHighlighting();
		return map;
	}

}
