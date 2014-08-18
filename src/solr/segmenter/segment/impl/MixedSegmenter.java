package com.gs.cvoud.solr.segmenter.segment.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.gs.cvoud.solr.segmenter.segment.Segmenter;

/**
 * 组合分段
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public class MixedSegmenter implements Segmenter {
	private List<Segmenter> segmenters;

	public MixedSegmenter(List<Segmenter> segmenters) {
		this.segmenters = segmenters;
	}

	public List<String> segment(String sentence) {
		Set<String> ret = new LinkedHashSet<String>();
		for (Segmenter segmenter : segmenters)
			ret.addAll(segmenter.segment(sentence));

		return new ArrayList<String>(ret);
	}

	public boolean existDict(String input) {
		boolean exist = false;
		for (Segmenter segmenter : segmenters) {
			if (segmenter.existDict(input)) {
				exist = true;
				break;
			}
		}
		return exist;
	}

}
