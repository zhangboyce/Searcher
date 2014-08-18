package solr.annotation;

import org.apache.solr.client.solrj.SolrQuery;
import utils.ObjectUtils;

/**
 * 对加入solr索引的copy域的枚举描述，主要提供分组查询，分组查询在solr中对应的Field 是分词的string类型
 * 
 * @author boyce
 * @version 2013-9-11
 */
public enum CopyField implements Field, QueryField {
	PROVINCE_COPY("provinceCopy"), 
	INDUSTRY_COPY("industryCopy"),
	FUNCTION_COPY("functionCopy"), 
	TAGS_COPY("tagsCopy"), 
	DEGREE_COPY("degreeCopy"),
	CITY_COPY("degreeCopy");

	/**
	 * Field Name 在solr中建立域的名称，用于根据Field精确搜索，分组搜索等
	 */
	private String name;

	private CopyField(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void addFacetField(SolrQuery solrQuery) {
		if (ObjectUtils.isNull(solrQuery))
			return;
		solrQuery.addFacetField(this.name);
	}
	
	public void addFilterQuery(SolrQuery solrQuery, String param) {
		if (ObjectUtils.hasNull(solrQuery, param))
			return;
		solrQuery.addFilterQuery(this.name + ":\"" + param + "\"");
	}
}
