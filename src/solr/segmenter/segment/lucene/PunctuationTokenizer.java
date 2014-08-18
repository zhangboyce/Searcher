package com.gs.cvoud.solr.segmenter.segment.lucene;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

import com.gs.cvoud.solr.segmenter.segment.utils.Latin;
import com.gs.cvoud.util.Punctuations;

public class PunctuationTokenizer extends Tokenizer {
	private int offset;
	private int bufferIndex;
	private int dataLen;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 4096;
	private final char ioBuffer[] = new char[4096];

	public PunctuationTokenizer(Reader input) {
		super(input);
		offset = 0;
		bufferIndex = 0;
		dataLen = 0;
	}

	public final Token next(Token token) throws IOException {
		token.clear();
		int length = 0;
		int digitType = 0;
		char buffer[] = token.termBuffer();
		label0: do {
			char c;
			do {
				do {
					if (bufferIndex >= dataLen) {
						offset += dataLen;
						dataLen = input.read(ioBuffer);
						if (dataLen == -1) {
							dataLen = 0;
							if (length <= 0)
								return null;
							break label0;
						}
						bufferIndex = 0;
					}
					c = ioBuffer[bufferIndex++];
					if (Punctuations.isPunctuation(c))
						continue label0;
					if (length == buffer.length)
						buffer = token.resizeTermBuffer(1 + length);
					if (Latin.isDigit(c)) {
						if (length == 0)
							digitType = 1;
						else if (digitType != 1) {
							bufferIndex--;
							break label0;
						}
					} else if (length > 0 && digitType > 0) {
						bufferIndex--;
						break label0;
					}
					buffer[length++] = c;
				} while (digitType > 0);
				if (length == 255)
					break label0;
			} while (!isCJ(c));
			if (length > 1) {
				bufferIndex--;
				length--;
			}
			break;
		} while (length <= 0);
		token.setTermLength(length);
		return token;
	}

	public void reset(Reader input) throws IOException {
		super.reset(input);
		bufferIndex = 0;
		offset = 0;
		dataLen = 0;
	}

	public static boolean isCJ(int c) {
		return 12544 <= c && c <= 12591 || 12352 <= c && c <= 12447 || 12448 <= c && c <= 12543 || 12784 <= c
				&& c <= 12799 || 13056 <= c && c <= 13183 || 13312 <= c && c <= 19903 || 19968 <= c && c <= 40959
				|| 63744 <= c && c <= 64255 || 65381 <= c && c <= 65439;
	}

	public static boolean isCJString(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!isCJ(c))
				return false;
		}

		return true;
	}

	public static void main(String args[]) throws Exception {
		String target = "一九九五年的春天，lucy100 登上了，海拔4800m的高峰。";
		StringReader reader = new StringReader(target);
		//PunctuationTokenizer tokenizer = new PunctuationTokenizer(reader);
		// for (Token t = tokenizer.next(); t != null; t = tokenizer.next())
		// System.out.println(t.termText());

	}

	@Override
	public boolean incrementToken() throws IOException {
		return false;
	}

}
