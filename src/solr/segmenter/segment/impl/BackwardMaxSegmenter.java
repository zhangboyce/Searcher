package com.gs.cvoud.solr.segmenter.segment.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.gs.cvoud.solr.segmenter.model.Dict;
import com.gs.cvoud.solr.segmenter.model.Dict.WordInfo;
import com.gs.cvoud.solr.segmenter.segment.PatternMarker;
import com.gs.cvoud.solr.segmenter.segment.Segmenter;
import com.gs.cvoud.solr.segmenter.segment.utils.Punctuations;

/**
 * 向后最大匹配分段
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public class BackwardMaxSegmenter implements Segmenter {
	private Dict dict;
	private static final int MAX_WINDOW_SIZE = 20;

	public BackwardMaxSegmenter(Dict dict) {
		this.dict = dict;
	}

	public List<String> segment(String sentence) {
		if (sentence == null || sentence.length() == 0)
			return Collections.emptyList();

		sentence = sentence.toLowerCase();
		List<String> ret = new LinkedList<String>();
		PatternMarker saver = new PatternMarker(dict, sentence);
		int tail = sentence.length();
		while (tail > 0) {
			int head;
			if (saver.isSaved(tail - 1)) {
				head = saver.getSavedZoneHead(tail - 1);
				int offset = tail - head;
				boolean isMatchDict = false;
				if (offset < MAX_WINDOW_SIZE) {
					int tempHead = tail - MAX_WINDOW_SIZE;
					if (tempHead < 0)
						tempHead = 0;
					for (; tempHead < head; tempHead++) {
						String word = sentence.substring(tempHead, tail);
						WordInfo info = dict.contains(word);
						if (!info.exist)
							continue;
						//分词去掉符号
						if (!info.isStopword && !Punctuations.isPunctuation(word))
							ret.add(0, dict.getStandard(word));
						tail = tempHead;
						isMatchDict = true;
						break;
					}

				}
				if (isMatchDict)
					continue;
				if (!saver.isStopword(tail - 1)) {
					String word = sentence.substring(head, tail);
					
					//分词去掉符号
					if (!Punctuations.isPunctuation(word)) {
						ret.add(0, dict.getStandard(word));
						tail = head;
						continue;
					}
				}
			}
			head = tail - MAX_WINDOW_SIZE;
			if (head < 0)
				head = 0;
			for (; head < tail; head++) {
				String word = sentence.substring(head, tail);
				WordInfo info = dict.contains(word);
				if (!info.exist && word.length() != 1)
					continue;
				
				//分词去掉符号
				if (!info.isStopword && !Punctuations.isPunctuation(word))
					ret.add(0, dict.getStandard(word));
				tail = head;
				break;
			}

		}

		return new ArrayList<String>(ret);
	}

	public boolean existDict(String input) {
		WordInfo info = dict.contains(input);
		return info.exist;
	}

}
