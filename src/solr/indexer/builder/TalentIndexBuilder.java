package com.gs.cvoud.solr.indexer.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.gs.cvoud.dao.TalentDao;
import com.gs.cvoud.model.Talent;
import com.gs.cvoud.service.TalentService;
import com.gs.cvoud.solr.TalentSolrEngine;
import com.gs.cvoud.util.CollectionUtils;

/**
 * 索引建立
 * 
 * @author boyce
 * @version 2013-7-11
 */
public class TalentIndexBuilder implements IndexBuilder {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(TalentIndexBuilder.class);

	// 更新索引，重建索引每次操作的条数
	private static final int PAGE_SIZE = 100;
	private static final AtomicBoolean BUILDING = new AtomicBoolean(false);

	private TalentDao talentDao;
	private TalentService talentService;
	private TalentSolrEngine talentSolrEngine;
	
	/**
	 * 释放同步锁
	 */
	public void releaseLock() {
		BUILDING.set(false);
		LOG.info("Release the lock successfully.");
	}

	/**
	 * 重建索引方法，两种方式取数据，一种一次全部取出，一种按分页取数据
	 * 
	 * @param isPage
	 *            是否分页
	 * @return
	 * @throws Exception
	 */
	public void rebuildIndex() {
		
		try {
			// 重建所有的索引需要清空updateTalent表中的内容
			talentDao.deleteAllUpdateTalent();

			// 将所有的talent记录添加到updatetalent中
			talentDao.addAllTalentToUpdate();
			LOG.info("Add all talent to updateTalent.");

			this.buildIndex();
		} catch (Exception e) {
			LOG.error("Rebuild index error.", e);
		} 
	}

	/**
	 * 定时更新索引，更新策略为将update表中的Talent都从索引里删除，然后再重新添加进索引；
	 * 
	 * @return
	 * @throws Exception
	 */
	public void updateIndex() {
		try {
			this.buildIndex();
		} catch (Exception e) {
			LOG.error("Rebuild index error.", e);
		} 
	}

	/**
	 * 更新某个用户的talent
	 */
	public void updateIndex(Long userId) {
		try {
			long start = System.currentTimeMillis();
			List<Talent> talents = talentService.getUpdateTalentByUserId(userId);
			if (CollectionUtils.isNotEmpty(talents)) {
				List<String> idStrs = new ArrayList<String>();
				List<Long> ids = new ArrayList<Long>();
				for (Talent talent : talents) {
					idStrs.add(String.valueOf(talent.getId()));
					ids.add(talent.getId());
				}
				LOG.info("【step1】: Query {} talents from db and take {} ms.", talents.size(),
						System.currentTimeMillis() - start);

				start = System.currentTimeMillis();
				talentSolrEngine.removeObjects(idStrs);
				LOG.info("【step2】: Delete {} talent indexs from solr and take {} ms.", idStrs.size(),
						System.currentTimeMillis() - start);

				start = System.currentTimeMillis();
				List<Long> ret = talentSolrEngine.batchAddObject(talents);
				LOG.info("【step3】: Batch add {} talent indexs to solr and take {} ms.", ret.size(),
						System.currentTimeMillis() - start);

				// 提交索引
				start = System.currentTimeMillis();
				talentSolrEngine.commit();
				LOG.info("【step4】: Commit {} talent indexs to solr and take {} ms.", ret.size(),
						System.currentTimeMillis() - start);

				// 删除数据库已经更新的talent记录
				talentDao.deleteUpdateTalentByTalentIds(ids);
			}
		} catch (Exception e) {
			LOG.error("Rebuild index error.", e);
		} 
	}

	// 构建索引
	private void buildIndex() throws Exception {
		try {
			// 根据当前时间点获取信息
			Date currDate = new Date();
			// 获取updateTalent表中的所有talent的id
			Map<String, Object> params = getUpdateParams(currDate);
			List<Long> ids = talentDao.getUpdateTalentId(params);
			List<String> idStrs = null;

			int sum = ids.size();
			long s = System.currentTimeMillis();
			while (CollectionUtils.isNotEmpty(ids)) {
				long start = System.currentTimeMillis();
				// 获取updateTalent表中的所有talent的id对应的Talent对象
				List<Talent> talentList = talentService.getByIds(ids);
				if (CollectionUtils.isEmpty(talentList)) {
					continue;
				}

				idStrs = new ArrayList<String>();
				for (Talent talent : talentList) {
					idStrs.add(String.valueOf(talent.getId()));
				}
				LOG.info("【step1】: Query {} talents from db and take {} ms.", talentList.size(),
						System.currentTimeMillis() - start);

				start = System.currentTimeMillis();
				talentSolrEngine.removeObjects(idStrs);
				LOG.info("【step2】: Delete {} talent indexs from solr and take {} ms.", idStrs.size(),
						System.currentTimeMillis() - start);

				start = System.currentTimeMillis();
				List<Long> ret = talentSolrEngine.batchAddObject(talentList);
				LOG.info("【step3】: Batch add {} talent indexs to solr and take {} ms.", ret.size(),
						System.currentTimeMillis() - start);

				// 提交索引
				start = System.currentTimeMillis();
				talentSolrEngine.commit();
				LOG.info("【step4】: Commit {} talent indexs to solr and take {} ms.", ret.size(),
						System.currentTimeMillis() - start);

				// 删除数据库已经更新的talent记录
				talentDao.deleteUpdateTalentByTalentIds(ids);
				ids = talentDao.getUpdateTalentId(getUpdateParams(currDate));
				sum += ids.size();
			}
			
			if (sum !=0)
				LOG.info("Build {}个 indexes complated and take {} ms.", sum, System.currentTimeMillis() - s);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据id批量删除索引
	 */
	public void deleteByIds(List<String> ids) {
		if (CollectionUtils.isNotEmpty(ids)) {
			synchronized (TalentIndexBuilder.class) {
				talentSolrEngine.removeObjects(ids);
				talentSolrEngine.commit();

				LOG.info("Remove talents from solr, the talents' ids are " + ids);
			}
		}
	}

	/**
	 * 根据id删除索引
	 */
	public void deleteById(String id) {
		List<String> ids = new ArrayList<String>();
		ids.add(id);
		this.deleteByIds(ids);
	}

	// 获取每页更新索引的参数
	private static Map<String, Object> getUpdateParams(Date cuttentDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", 0);
		params.put("limit", PAGE_SIZE);
		params.put("date", cuttentDate);

		return params;
	}

	public void deleteAllIndex() {
		talentSolrEngine.removeAll();
	}

	public void setTalentDao(TalentDao talentDao) {
		this.talentDao = talentDao;
	}

	public void setTalentSolrEngine(TalentSolrEngine talentSolrEngine) {
		this.talentSolrEngine = talentSolrEngine;
	}

	public void setTalentService(TalentService talentService) {
		this.talentService = talentService;
	}

}
