package com.gs.cvoud.solr.segmenter.segment.lucene;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

/**
 * 中文的TokenStream
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public class ChineseTokenStream extends TokenStream {
	private List<String> tokens;
	private int start;
	private int index;

	public ChineseTokenStream(List<String> tokens) {
		this.tokens = tokens;
		start = 0;
		index = 0;
	}

	public Token next() throws IOException {
		if (index >= tokens.size()) {
			return null;
		} else {
			String word = (String) tokens.get(index++);
			int end = start + word.length();
			Token token = new Token(word, start, end);
			start = end;
			return token;
		}
	}

	public boolean incrementToken() throws IOException {
		return false;
	}
}
