package solr.searcher.command;

import solr.searcher.SearchRequest;
import solr.searcher.assembler.FacetFieldAssembler;
import solr.searcher.assembler.HighlightFieldAssembler;
import solr.searcher.assembler.KeyWordQueryAssembler;
import solr.searcher.assembler.PageFieldAssembler;

/**
 * 关键字查询命令
 * 
 * @author boyce
 * @version 2013-9-11
 */
public class KeyWordQueryCommand extends AbstractCommand {

	public KeyWordQueryCommand(SearchRequest request) {
		this.addAssembler(new FacetFieldAssembler(request));
		this.addAssembler(new HighlightFieldAssembler());
		this.addAssembler(new PageFieldAssembler(request));
		this.addAssembler(new KeyWordQueryAssembler(request));
	}
}
