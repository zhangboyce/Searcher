package solr.searcher.command;

import solr.searcher.SearchRequest;
import solr.searcher.assembler.Assembler;
import solr.searcher.assembler.FacetFieldAssembler;
import solr.searcher.assembler.PageFieldAssembler;

/**
 * 分组查询命令
 * 
 * @author boyce
 * @version 2013-9-11
 */
public class FacetFieldQueryCommand extends AbstractCommand {

	public FacetFieldQueryCommand(SearchRequest request) {
		//提供默认分组，分页组件
		this.addAssemblers(new Assembler[]{
                new FacetFieldAssembler(request),
                new PageFieldAssembler(request)});
	}

	//构造方法传入组件
	public FacetFieldQueryCommand(SearchRequest request, Assembler[] assemblers) {
		this.addAssemblers(assemblers);
	}
}
