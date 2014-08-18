package com.gs.cvoud.solr.segmenter.segment.impl;

import java.util.List;

import com.gs.cvoud.solr.segmenter.segment.Segmenter;

/**
 * 片段
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public class WordSliceSegmenter implements Segmenter {
	private Segmenter originSegmenter;

	public WordSliceSegmenter(Segmenter originSegmenter) {
		this.originSegmenter = originSegmenter;
	}

	public List<String> segment(String sentence) {
		List<String> list = originSegmenter.segment(sentence);
		if (list == null)
			return null;
		if (list.isEmpty())
			return list;
		int count = list.size();
		for (int i = 0; i < count; i++) {
			String wordString = (String) list.get(i);
			int wordLength = wordString.length();
			if (wordLength >= 2) {
				char firstChar = wordString.charAt(0);
				char lastChar = wordString.charAt(wordLength - 1);
				if (firstChar > '\177') {
					for (int n = wordLength - 1; n >= 1; n--) {
						for (int p = 0; p <= wordLength - n; p++)
							list.add(wordString.substring(p, p + n));

					}

				} else if (firstChar >= 'a' && firstChar <= 'z' && lastChar >= '0' && lastChar <= '9') {
					int p;
					for (p = wordLength - 2; p >= 0; p--) {
						char pchar = wordString.charAt(p);
						if (pchar < '0' || pchar > '9')
							break;
					}

					list.add(wordString.substring(p + 1));
				} else if (firstChar >= '0' && firstChar <= '9' && lastChar >= 'a' && lastChar <= 'z') {
					int p;
					for (p = 1; p < wordLength; p++) {
						char pchar = wordString.charAt(p);
						if (pchar < '0' || pchar > '9')
							break;
					}

					list.add(wordString.substring(0, p));
				}
			}
		}

		return list;
	}

	public boolean existDict(String input) {
		return false;
	}

}
