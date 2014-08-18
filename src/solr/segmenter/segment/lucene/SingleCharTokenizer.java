package com.gs.cvoud.solr.segmenter.segment.lucene;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

import com.gs.cvoud.solr.segmenter.segment.utils.Punctuations;

public class SingleCharTokenizer extends Tokenizer {
	private int offset;
	private int bufferIndex;
	private int dataLen;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 4096;
	private final char ioBuffer[] = new char[4096];

	public SingleCharTokenizer(Reader input) {
		super(input);
		offset = 0;
		bufferIndex = 0;
		dataLen = 0;
	}

	public final Token next(Token token) throws IOException {
		token.clear();
		int start = bufferIndex;
		int length = 0;
		int numOfP = 0;
		char buffer[] = token.termBuffer();
		do {
			if (bufferIndex >= dataLen) {
				offset += dataLen;
				dataLen = input.read(ioBuffer);
				if (dataLen == -1) {
					dataLen = 0;
					if (length <= 0)
						return null;
					break;
				}
				bufferIndex = 0;
			}
			char c = ioBuffer[bufferIndex++];
			if (!Punctuations.isPunctuation(c)) {
				if (length == buffer.length)
					buffer = token.resizeTermBuffer(1 + length);
				buffer[length++] = c;
				if (length != 255)
					;
				break;
			}
			numOfP++;
		} while (length <= 0);
		token.setTermLength(length);
		token.setStartOffset(start + numOfP);
		token.setEndOffset(start + numOfP + length);
		return token;
	}

	public void reset(Reader input) throws IOException {
		super.reset(input);
		bufferIndex = 0;
		offset = 0;
		dataLen = 0;
	}

	public static void main(String args[]) throws IOException {
		String target = "L, UCY登上了海拔4800米的高峰.";
		StringReader reader = new StringReader(target);
		SingleCharTokenizer tokenizer = new SingleCharTokenizer(reader);
		// for (Token t = tokenizer.next(); t != null; t = tokenizer.next())
		// System.out.println(t.termText());

	}

	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

}
