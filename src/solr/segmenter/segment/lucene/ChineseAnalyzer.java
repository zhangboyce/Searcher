package com.gs.cvoud.solr.segmenter.segment.lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import com.gs.cvoud.solr.segmenter.segment.Segmenter;
import com.gs.cvoud.solr.segmenter.segment.SegmenterFactory;

/**
 * 中文分词器 lucene
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public class ChineseAnalyzer extends Analyzer {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ChineseAnalyzer.class);

	private Segmenter segmenter;

	public ChineseAnalyzer(String dictPath) throws IOException {
		segmenter = SegmenterFactory.newMixSegmenter(dictPath);
	}

	public ChineseAnalyzer(Segmenter segmenter) {
		this.segmenter = segmenter;
	}

	public TokenStream tokenStream(String fieldName, Reader reader) {
		StringBuffer str = new StringBuffer();
		BufferedReader br = new BufferedReader(reader);
		String strTmp = "";
		try {
			strTmp = br.readLine();
		} catch (IOException e) {
			logger.error("Error.", e);
		}
		while (strTmp != null) {
			str.append(strTmp);
			try {
				strTmp = br.readLine();
			} catch (IOException e) {
				logger.error("Error.", e);
			}
		}
		return new ChineseTokenStream(segmenter.segment(str.toString()));
	}

	public Segmenter getSegmenter() {
		return segmenter;
	}

	public void setSegmenter(Segmenter segmenter) {
		this.segmenter = segmenter;
	}

}
