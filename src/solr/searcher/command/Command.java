package solr.searcher.command;

import org.apache.solr.client.solrj.SolrQuery;
import solr.searcher.assembler.Assembler;

/**
 * 查询条件命令接口
 * 
 * @author boyce
 * @version 2013-9-11
 */
public interface Command {
	/**
	 * 转换获取SolrQuery查询条件
	 */
	public SolrQuery convert();
	
	/**
	 * 转换获取SolrQuery查询条件
	 */
	public SolrQuery convert(String query);
	
	/**
	 * 添加一个assembler
	 * @param assembler
	 */
	public void addAssembler(Assembler assembler);
	
	/**
	 * 添加一组assembler
	 * @param assemblers
	 */
	public void addAssemblers(Assembler[] assemblers);
}
