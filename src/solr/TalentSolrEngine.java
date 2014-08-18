package com.gs.cvoud.solr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import com.gs.cvoud.model.Talent;
import com.gs.cvoud.solr.indexer.Indexer;
import com.gs.cvoud.solr.indexer.TalentIndexer;
import com.gs.cvoud.solr.indexer.builder.IndexBuilder;
import com.gs.cvoud.solr.search.Searcher;
import com.gs.cvoud.solr.search.TalentNearRealtimeSearcher;
import com.gs.cvoud.solr.search.TalentSearcher;
import com.gs.cvoud.solr.search.query.command.Command;
import com.gs.cvoud.solr.search.response.TalentSearchResponse;
import com.gs.cvoud.util.C;

/**
 * Talent对象的solr搜索主要接口， 实际上它是Searcher和Indexer的代理且提供配置，初始化solr，初始化segmenter 最后利用Searcher和Indexer完成solr索引的建立和搜索
 * 
 * @author boyce
 * @version 2013-7-8
 */
public class TalentSolrEngine implements Searcher<Talent>, Indexer<Talent> {
	private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TalentSolrEngine.class);

	private Indexer<Talent> indexer;
	private Searcher<Talent> searcher;
	
	@Autowired
	private IndexBuilder indexBuilder;

	/**
	 * 初始化，spring创建TalentSolrEngine对象的时候调用该init方法初始化配置信息，solr服务器信息。
	 * 
	 * @throws IOException
	 */
	public void init() throws IOException {
		CommonsHttpSolrServer server = null;
		try {
			server = new CommonsHttpSolrServer(C.SYS_SOLR_SERVER_URL);
			server.setSoTimeout(300000); // socket read timeout
			server.setConnectionTimeout(1000);
			server.setDefaultMaxConnectionsPerHost(100);
			server.setMaxTotalConnections(100);
			server.setFollowRedirects(false);
			server.setAllowCompression(true);
			server.setMaxRetries(1);

		} catch (MalformedURLException e) {
			logger.error("Error.", e);
		}

		this.indexer = new TalentIndexer(server);
		this.searcher = new TalentNearRealtimeSearcher(new TalentSearcher(server), this.indexBuilder);

	}

	public List<Long> batchAddObject(List<Talent> ts) {
		return indexer.batchAddObject(ts);
	}

	public void addObject(Talent t) {
		indexer.addObject(t);
	}

	public void updateObject(Talent t) {
		indexer.updateObject(t);
	}

	public void removeObjects(List<String> keys) {
		indexer.removeObjects(keys);
	}

	public void commit() {
		indexer.commit();
	}

	public void optimize() {
		indexer.optimize();
	}

	public void removeAll() {
		long start = System.currentTimeMillis();

		indexer.removeAll();

		long end = System.currentTimeMillis();
		logger.info("Remove all index from solr take： " + (end - start) + " ms.");
	}

	public TalentSearchResponse search(Command command) {
		return searcher.search(command);
	}

	public TalentSearchResponse stat(Command command) {
		return searcher.stat(command);
	}

	public Talent search(String uniqueKey) {
		return searcher.search(uniqueKey);
	}
	
	public TalentSearchResponse search(List<String> uniqueKeys) {
		return searcher.search(uniqueKeys);
	}
	
	public TalentSearchResponse search(List<String> uniqueKeys, Command command) {
		return searcher.search(uniqueKeys, command);
	}

	public void setIndexer(Indexer<Talent> indexer) {
		this.indexer = indexer;
	}

	public void setSearcher(Searcher<Talent> searcher) {
		this.searcher = searcher;
	}

}
