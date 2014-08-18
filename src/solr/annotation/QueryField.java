package solr.annotation;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * 可以用户solr搜索的域
 * @author boyce
 * @version 2013-12-18
 */
public interface QueryField {
	/**
	 * 添加过滤查询条件
	 */
	void addFilterQuery(SolrQuery solrQuery, String param);
	
	/**
	 * 添加查询分组域
	 * @param solrQuery solr的query参数对象
	 */
	void addFacetField(SolrQuery solrQuery);
}
