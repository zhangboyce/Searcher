package com.gs.cvoud.solr.segmenter.segment.lucene;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;

/**
 * 标点符号分词器
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public class PunctuationAnalyzer extends Analyzer {
	public PunctuationAnalyzer() {
	}

	public TokenStream tokenStream(String fieldName, Reader reader) {
		PunctuationTokenizer tokenStream = new PunctuationTokenizer(reader);
		TokenStream result = new LowerCaseFilter(Version.LUCENE_30, tokenStream);
		return result;
	}
}
