package solr.searcher.assembler;

import org.apache.solr.client.solrj.SolrQuery;
import solr.searcher.SearchRequest;

/**
 * 分页组件组装者
 * 
 * @author boyce
 * @version 2013-9-11
 */
public class PageFieldAssembler implements Assembler {
	private SearchRequest request;

	public PageFieldAssembler(SearchRequest request) {
		this.request = request;
	}

	/**
	 * 根据request组装分页组件
	 */
	public void assembly(SolrQuery params) {
		// 设置页码和PageSize
		params.setStart(request.getPageOffset());
		params.setRows(request.getPageSize());
		// params.setSortField(request.getSortField(), request.getOrder());
	}

}
