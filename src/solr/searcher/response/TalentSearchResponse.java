package com.gs.cvoud.solr.search.response;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.response.QueryResponse;

import com.gs.cvoud.model.Talent;
import com.gs.cvoud.util.CollectionUtils;
import com.gs.cvoud.util.ObjectUtils;
import com.gs.cvoud.util.StringUtils;

public class TalentSearchResponse {
	private int totalCount;// 符合条件的结果个数

	private List<Talent> talentList;
	private List<FilterItemGroup> filterItemGroups;

	/**
	 * 候选人分布统计
	 */
	private DistributionResultStat distributionResultStat;

	/**
	 * 统计数据，统计候选人没有标注各种属性的数量，标注各种属性的数量。
	 * 
	 * @param response
	 *            solr查询返回的response
	 */
	public void analysisStat(QueryResponse response) {
		TalentQueryResponseAnalysiser analysiser = new TalentQueryResponseAnalysiser(response);
		TalentStatResponseAnalysiser statAnalysiser = new TalentStatResponseAnalysiser(response);

		this.totalCount = analysiser.getTotal();
		if (totalCount <= 0) {
			return;
		}

		this.filterItemGroups = analysiser.analyzeFilterItemGroup();
		this.distributionResultStat = statAnalysiser.analyDistributionResultStat(filterItemGroups);
	}

	/**
	 * 候选人搜索，对QueryResponse进行解析，解析成候选人列表，总数，属性分组信息，高亮信息等。
	 * 
	 * @param response
	 *            solr查询返回的response
	 */
	public void analysis(QueryResponse response) {
		if (ObjectUtils.hasNull(response)) {
			return;
		}

		TalentQueryResponseAnalysiser analysiser = new TalentQueryResponseAnalysiser(response);

		this.totalCount = analysiser.getTotal();
		this.talentList = analysiser.analyObjectList();
		this.filterItemGroups = analysiser.analyzeFilterItemGroup();
		
		// 输出高亮结果集
		Map<String, Map<String, List<String>>> highlightMap = analysiser.analyHighlightMap();
		if (CollectionUtils.isNotEmpty(talentList) && ObjectUtils.isNotNull(highlightMap) && highlightMap.size() != 0) {
			for (Talent talent : this.talentList) {
				Map<String, List<String>> content = highlightMap.get(String.valueOf(talent.getId()));
				if (content.size() != 0) {
					StringBuilder builder = new StringBuilder();
					INNER: for (Entry<String, List<String>> contentEntry : content.entrySet()) {
						List<String> listContent = contentEntry.getValue();
						if (CollectionUtils.isEmpty(listContent))
							continue INNER;

						for (String string : listContent)
							builder.append(string);

						builder.append("<br/>");
					}

					talent.setHighlightContent(StringUtils.deleteBlankInterChinese(builder.toString()));
				}
			}
		}
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<Talent> getTalentList() {
		return talentList;
	}

	public void setTalentList(List<Talent> talentList) {
		this.talentList = talentList;
	}

	public DistributionResultStat getDistributionResultStat() {
		return distributionResultStat;
	}

	public void setDistributionResultStat(DistributionResultStat distributionResultStat) {
		this.distributionResultStat = distributionResultStat;
	}

	public List<FilterItemGroup> getFilterItemGroups() {
		return filterItemGroups;
	}

	public void setFilterItemGroups(List<FilterItemGroup> filterItemGroups) {
		this.filterItemGroups = filterItemGroups;
	}

}
