package com.gs.cvoud.solr.segmenter.segment;

import java.util.List;

/**
 * Segmenter 接口
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public interface Segmenter {
	/**
	 * 分词
	 * 
	 * @param resource
	 *            需要被分词的原始字符串
	 * @return 分词后的字符串集合
	 */
	public List<String> segment(String resource);

	/**
	 * 分词库中是否包含某个词语
	 * 
	 * @param target
	 *            目标词语
	 * @return 是否包含 true/包含 false/不包含
	 */
	public boolean existDict(String target);
}
