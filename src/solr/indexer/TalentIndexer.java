package com.gs.cvoud.solr.indexer;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;

import com.gs.cvoud.model.Talent;
import com.gs.cvoud.solr.TalentConvertor;
import com.gs.cvoud.util.CollectionUtils;
import com.gs.cvoud.util.ObjectUtils;

/**
 * Talent对象的索引建立者
 * 
 * @author boyce
 * @version 2013-7-11
 */
public class TalentIndexer implements Indexer<Talent> {

	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TalentIndexer.class);
	private SolrServer server;

	/**
	 * @param server
	 *            solr服务器
	 * @param convertor
	 *            转换器
	 */
	public TalentIndexer(SolrServer server) {
		if (ObjectUtils.isNull(server)) {
			throw new IllegalArgumentException("Cannot constract a indexer with null argument.");
		}
		this.server = server;
	}

	/**
	 * 批量添加索引
	 */
	public List<Long> batchAddObject(List<Talent> talentlist) {
		List<Long> excelist = new ArrayList<Long>();
		if (CollectionUtils.isEmpty(talentlist)) {
			return excelist;
		}

		List<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();
		SolrInputDocument doc = null;
		try {
			long sum = 0;
			long start = 0;
			long end = 0;
			long max = 0;
			for (Talent talent : talentlist) {
				start = System.currentTimeMillis();

				doc = TalentConvertor.toDocument(talent);
				documents.add(doc);
				excelist.add(talent.getId());

				end = System.currentTimeMillis();
				sum += (end - start);
				if ((end - start) > max) {
					max = (end - start);
				}
			}

			log.info("Convert {} talents to solr documents take: {} ms.", talentlist.size(), sum);
			log.info("Convert talent to document take max time: {} ms.", max);

			start = System.currentTimeMillis();
			server.add(documents);
			log.info("Add {} documents to solr server take : {} ms.", documents.size(),
					(System.currentTimeMillis() - start));
		} catch (Exception e) {
			log.error("Batch add talent indexs to solr error.", e);
		}

		return excelist;

	}

	/**
	 * 添加单个对象索引
	 */
	public void addObject(Talent talent) {
		try {
			SolrInputDocument doc = TalentConvertor.toDocument(talent);
			server.add(doc);
		} catch (Exception e) {
			log.error("Add solr index error.", e);
		}
	}

	/**
	 * 更新索引
	 */
	public void updateObject(Talent talent) {
		try {
			server.deleteById(Long.toString(talent.getId()));
			SolrInputDocument doc = TalentConvertor.toDocument(talent);
			server.add(doc);
		} catch (Exception e) {
			log.error("Update solr index error.", e);
		}
	}

	/**
	 * 删除索引
	 */
	public void removeObjects(List<String> talentKeys) {
		try {
			server.deleteById(talentKeys);
		} catch (Exception e) {
			log.error("Remove solr index error.", e);
		}
	}

	/**
	 * 提交索引并重建
	 */
	public void commit() {
		try {
			server.commit();
		} catch (Exception e) {
			log.error("Commit solr index error.", e);
		}
	}

	public void optimize() {
		try {
			server.optimize();
		} catch (Exception e) {
			log.error("Optimize solr index error.", e);
		}
	}

	/**
	 * 清除所有的索引
	 */
	public void removeAll() {
		try {
			server.deleteByQuery("*:*");
		} catch (Exception e) {
			log.error("Remove all solr index error.", e);
		}
	}

}
