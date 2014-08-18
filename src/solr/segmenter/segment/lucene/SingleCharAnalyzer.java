package com.gs.cvoud.solr.segmenter.segment.lucene;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

public class SingleCharAnalyzer extends Analyzer {
	public SingleCharAnalyzer() {
	}

	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new SingleCharTokenizer(reader);
	}
}
