package solr.searcher.command;

import org.apache.solr.client.solrj.SolrQuery;
import solr.searcher.assembler.Assembler;
import utils.CollectionUtils;
import utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象查询条件命令，既是一个Command也是一个Assembler
 * 
 * @author boyce
 * @version 2013-9-11
 */
public abstract class AbstractCommand implements Command, Assembler {
	
	protected List<Assembler> assemblers = new ArrayList<Assembler>();
	
	/**
	 * 实现接Command口方法
	 */
	public SolrQuery convert() {
		return this.convert("*:*");
	}
	
	public SolrQuery convert(String query) {
		SolrQuery params = new SolrQuery();

		// 默认是 *:*
		params.setQuery(query);
		this.assembly(params);
		return params;
	}
	
	/**
	 * 组装
	 */
	public void assembly(SolrQuery params) {
		if (CollectionUtils.isNotEmpty(assemblers)) {
			for (Assembler assembler: assemblers) {
				assembler.assembly(params);
			}
		}
	}

	public void addAssembler(Assembler assembler) {
		if (ObjectUtils.isNotNull(assembler))
			assemblers.add(assembler);
	}
	
	public void addAssemblers(Assembler[] assemblers) {
		if (ObjectUtils.isNotNull(assemblers) && assemblers.length != 0) {
			for (Assembler assembler: assemblers)
				this.addAssembler(assembler);
		}
	}
}
