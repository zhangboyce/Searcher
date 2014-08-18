package com.gs.cvoud.solr.segmenter.segment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gs.cvoud.solr.segmenter.model.Dict;
import com.gs.cvoud.solr.segmenter.segment.impl.BackwardMaxSegmenter;
import com.gs.cvoud.solr.segmenter.segment.impl.ForwardMinSegmenter;
import com.gs.cvoud.solr.segmenter.segment.impl.MixedSegmenter;

/**
 * Segmenter 工厂方法
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public final class SegmenterFactory {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SegmenterFactory.class);
	private SegmenterFactory() {
	}

	/**
	 * 构造向后最大匹配分词
	 * @param dictPath 自定义分词文件路径
	 * @return Segmenter
	 * @throws IOException
	 */
	public static Segmenter newBackwardMaxSegmenter(String dictPath) {
		try {
			Dict dict = new Dict(dictPath);
			return new BackwardMaxSegmenter(dict);
		} catch (Exception e) {
			logger.error("System Error", e);
		}
		return null;
	}
	
	/**
	 * 构造向后最大匹配分词
	 * @param dics 自定义分词List集合
	 * @return Segmenter
	 * @throws IOException
	 */
	public static Segmenter newBackwardMaxSegmenter(List<String> dics) {
		try {
			final Dict dict = new Dict(dics);
			return new BackwardMaxSegmenter(dict);
		} catch (Exception e) {
			logger.error("System Error", e);
		}
		return null;
	}

	/**
	 * 构造向后最大匹配分词
	 * @return Segmenter
	 * @throws IOException
	 */
	public static Segmenter BackwardMaxSegmenter() {
		try {
			final Dict dict = new Dict();
			return new BackwardMaxSegmenter(dict);
		} catch (Exception e) {
			logger.error("System Error", e);
		}
		return null;
	}

	/**
	 * 构造向前最小匹配分词
	 * @param dics 自定义分词List集合
	 * @return Segmenter
	 * @throws IOException
	 */
	public static Segmenter newBackwardForwardMinSegmenter(List<String> dics) {
		try {
			final Dict dict = new Dict(dics);
			return new ForwardMinSegmenter(dict);
		} catch (Exception e) {
			logger.error("System Error", e);
		}
		return null;
	}
	
	/**
	 * 构造向前最小匹配分词
	 * @param dictPath 自定义分词文件
	 * @return Segmenter
	 * @throws IOException
	 */
	public static Segmenter newBackwardForwardMinSegmenter(String dictPath) {
		try {
			final Dict dict = new Dict(dictPath);
			return new ForwardMinSegmenter(dict);
		} catch (Exception e) {
			logger.error("System Error", e);
		}
		return null;
	}

	/**
	 * 构造向前最小匹配分词
	 * @return Segmenter
	 * @throws IOException
	 */
	public static Segmenter BackwardForwardMinSegmenter() {
		try {
			final Dict dict = new Dict();
			return new ForwardMinSegmenter(dict);
		} catch (Exception e) {
			logger.error("System Error", e);
		}
		return null;
	}

	/**
	 * 构造向后最大匹配和向前最小匹配的混合分词
	 * @param dictPath 自定义分词文件路径
	 * @return Segmenter
	 * @throws IOException
	 */
	public static Segmenter newMixSegmenter(String dictPath) {
		return newMixSegmenter(dictPath, null);
	}
	
	/**
	 * 构造向后最大匹配和向前最小匹配的混合分词
	 * @param dics 自定义分词List集合
	 * @return Segmenter
	 * @throws IOException
	 */
	public static Segmenter newMixSegmenter(List<String> dics) {
		return newMixSegmenter(null, dics);
	}

	public static Segmenter newMixSegmenter() {
		return newMixSegmenter(null, null);
	}

	/**
	 * 构造向后最大匹配和向前最小匹配的混合分词
	 * @param dictPath 自定义分词文件路径
	 * @param dics 自定义分词List集合
	 * @return Segmenter
	 * @throws IOException
	 */
	private static Segmenter newMixSegmenter(String dictPath, List<String> dics) {
		try {
			final Dict dict = new Dict(dictPath, dics);
			return new MixedSegmenter(new ArrayList<Segmenter>() {
				private static final long serialVersionUID = -1248353935799975518L;
				{
					add(new ForwardMinSegmenter(dict));
					add(new BackwardMaxSegmenter(dict));
				}
			});
		} catch (Exception e) {
			logger.error("System Error", e);
		}
		return null;
	}
}
