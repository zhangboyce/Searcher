package com.gs.cvoud.solr.segmenter.segment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gs.cvoud.solr.segmenter.model.Dict;

/**
 * 将一段文本职能符合正则的分词和停词字段标记下标
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public class PatternMarker {
	private boolean saved[];
	private boolean stoped[];

	/**
	 * 创建PatternMarker
	 * 
	 * @param dict
	 *            字典对象
	 * @param sentence
	 *            目标字符串
	 */
	public PatternMarker(Dict dict, String sentence) {
		saved = new boolean[map(sentence.length())];
		stoped = new boolean[map(sentence.length())];

		// 标记所有的分词词语
		for (Pattern regex : dict.getAllWordPatterns()) {
			for (Matcher matcher = regex.matcher(sentence); matcher.find();) {
				for (int i = map(matcher.start()); i <= map(matcher.end() - 1); i++) {
					saved[i] = true;
					stoped[i] = false;
				}

			}

		}

		// 标记所有的停词词语
		for (Pattern regex : dict.getAllStopwordPatterns()) {
			for (Matcher matcher = regex.matcher(sentence); matcher.find();) {
				for (int i = map(matcher.start()); i <= map(matcher.end() - 1); i++) {
					saved[i] = true;
					stoped[i] = true;
				}

			}

		}

	}

	public boolean isSaved(int index) {
		int mapedIndex = map(index);
		if (mapedIndex >= 0 && mapedIndex < saved.length)
			return saved[mapedIndex];
		else
			return false;
	}

	public boolean isStopword(int index) {
		int mapedIndex = map(index);
		if (mapedIndex >= 0 && mapedIndex < saved.length)
			return stoped[mapedIndex];
		else
			return false;
	}

	public int getSavedZoneHead(int index) {
		int mapedIndex = map(index);
		if (mapedIndex >= 0 && mapedIndex < saved.length) {
			for (int i = index; i >= 0; i--)
				if (!saved[map(i) - 1])
					return i;

			return 0;
		} else {
			return index;
		}
	}

	public int getSavedZoneTail(int index) {
		int mapedIndex = map(index);
		if (mapedIndex >= 0 && mapedIndex < saved.length) {
			for (int i = index; i < saved.length; i++)
				if (!saved[map(i) + 1])
					return i;

			return saved.length - 1;
		} else {
			return index;
		}
	}

	private static int map(int index) {
		return 2 * index + 1;
	}

}
