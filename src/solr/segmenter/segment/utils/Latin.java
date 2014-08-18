package com.gs.cvoud.solr.segmenter.segment.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 拉丁文工具类
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public final class Latin {
	public static final char LATIN_START = 32;
	public static final char LATIN_END = 126;
	public static final char FULLWIDTH_OFFSET = 65248;
	public static final char DIGIT_ZERO = 48;
	public static final char DIGIT_NINE = 57;
	public static final char LATIN_CAPITAL_LETTER_A = 65;
	public static final char LATIN_CAPITAL_LETTER_Z = 90;
	public static final char LATIN_SMALL_LETTER_A = 97;
	public static final char LATIN_SMALL_LETTER_Z = 122;

	private Latin() {
	}

	public static boolean isLatin(int c) {
		return c >= 32 && c <= 126;
	}

	public static boolean isLatin1(char c) {
		return c >= '\300' && c <= '\377';
	}

	public static boolean isFullwidth(int c) {
		c -= 65248;
		return isLatin(c);
	}

	public static boolean isDigit(int c) {
		return c >= 48 && c <= 57;
	}

	public static boolean isFullwidthDigit(int c) {
		c -= 65248;
		return isDigit(c);
	}

	public static boolean isInteger(String s) {
		for (int i = 0; i < s.length(); i++)
			if (!isDigit(s.charAt(i)))
				return false;

		return true;
	}

	public static boolean isFullwidthInteger(String s) {
		for (int i = 0; i < s.length(); i++)
			if (!isFullwidthDigit(s.charAt(i)))
				return false;

		return true;
	}

	public static boolean isLetter(int c) {
		return c >= 65 && c <= 90 || c >= 97 && c <= 122;
	}

	public static boolean isFullwidthLetter(int c) {
		c -= 65248;
		return isLetter(c);
	}

	public static boolean isLatinString(String s, boolean includeFullwidth) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!isLatin(c) && (!includeFullwidth || !isFullwidth(c)))
				return false;
		}

		return true;
	}

	public static Map<Character, Character> charMap() {
		Map<Character, Character> ret = new HashMap<Character, Character>();
		for (int c = 32; c <= 126; c++)
			ret.put(Character.valueOf((char) (c + 65248)), Character.valueOf((char) c));

		return ret;
	}

	public static void main(String args[]) {
		String s = "english1000";
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isLetter(c))
				System.out.print((new StringBuilder(String.valueOf(c))).append("-L ").toString());
			else if (isDigit(c))
				System.out.print((new StringBuilder(String.valueOf(c))).append("-D ").toString());
		}

	}
}
