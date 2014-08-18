package com.gs.cvoud.solr.segmenter.segment.lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

import com.gs.cvoud.solr.segmenter.segment.Segmenter;

/**
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public class ChineseTokenizer extends Tokenizer {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ChineseTokenizer.class);

	@SuppressWarnings("unused")
	private TokenStream tokenStream;

	public ChineseTokenizer(Reader in, Segmenter segmenter) {
		super(in);
		StringBuffer str = new StringBuffer();
		BufferedReader br = new BufferedReader(in);
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
		tokenStream = new ChineseTokenStream(segmenter.segment(str.toString()));
	}

	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}
}
