package com.gs.cvoud.solr.segmenter.segment.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gs.cvoud.solr.segmenter.model.Dict;
import com.gs.cvoud.solr.segmenter.model.Dict.WordInfo;
import com.gs.cvoud.solr.segmenter.segment.PatternMarker;
import com.gs.cvoud.solr.segmenter.segment.Segmenter;
import com.gs.cvoud.solr.segmenter.segment.utils.Punctuations;

/**
 * 向前最小匹配分段
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public class ForwardMinSegmenter implements Segmenter {

	private Dict dict;

	public ForwardMinSegmenter(Dict dict) {
		this.dict = dict;
	}

	public List<String> segment(String sentence) {
		if (sentence == null || sentence.length() == 0)
			return Collections.emptyList();
		sentence = sentence.toLowerCase();
		PatternMarker saver = new PatternMarker(dict, sentence);
		List<String> ret = new ArrayList<String>();
		for (int head = 0; head < sentence.length();)
			if (saver.isSaved(head)) {
				int tail = saver.getSavedZoneTail(head) + 1;
				if (!saver.isStopword(head)) {
					String word = sentence.substring(head, tail);
					if (!Punctuations.isPunctuation(word))
						ret.add(dict.getStandard(word));
				}
				head = tail;
			} else {
				int tail = head + 2;
				if (tail > sentence.length())
					tail = sentence.length();
				boolean matched = false;
				for (; tail < head + 8 && tail <= sentence.length(); tail++) {
					String word = sentence.substring(head, tail);
					WordInfo info = dict.contains(word);
					if (!info.exist)
						continue;
					matched = true;
					if (!info.isStopword && !Punctuations.isPunctuation(word))
						ret.add(dict.getStandard(word));
					head = tail;
					break;
				}

				if (!matched) {
					tail = head + 1;
					String word = sentence.substring(head, tail);
					WordInfo info = dict.contains(word);
					if (!info.isStopword && !Punctuations.isPunctuation(word))
						ret.add(dict.getStandard(word));
					head = tail;
				}
			}

		return new ArrayList<String>(ret);
	}

	public boolean existDict(String input) {
		WordInfo info = dict.contains(input);
		return info.exist;
	}

}
