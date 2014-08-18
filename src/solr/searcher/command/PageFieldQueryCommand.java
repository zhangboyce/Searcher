package solr.searcher.command;

import solr.searcher.SearchRequest;
import solr.searcher.assembler.PageFieldAssembler;

/**
 * 分组查询命令
 * 
 * @author boyce
 * @version 2013-9-11
 */
public class PageFieldQueryCommand extends AbstractCommand {

	public PageFieldQueryCommand(SearchRequest request) {
		//提供默认分组，分页组件
		this.addAssembler(new PageFieldAssembler(request));
	}
}
