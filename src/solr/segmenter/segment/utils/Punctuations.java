package com.gs.cvoud.solr.segmenter.segment.utils;

/**
 * 标点符号处理工具类
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public final class Punctuations {
	private Punctuations() {
	}

	/**
	 * 是否是ASCII编码下的标点符号
	 */
	public static boolean isASCIIPunctuation(int c) {
		return Latin.isLatin(c) && !Latin.isDigit(c) && !Latin.isLetter(c);
	}

	/**
	 * 是否是Latin1编码下的标点符号
	 */
	public static boolean isLatin1Punctuation(int c) {
		return c >= 160 && c <= 191;
	}

	/**
	 * 是否是unicode编码中的表达符号
	 * 
	 * @param b
	 *            表示 Unicode 规范中字符块的一系列字符子集
	 */
	public static boolean isGeneralPunctuation(Character.UnicodeBlock b) {
		return Character.UnicodeBlock.GENERAL_PUNCTUATION == b;
	}

	/**
	 * 是否其他的特殊符号
	 * 
	 * @param c
	 */
	public static boolean isSupplementalPunctuation(int c) {
		return c >= 11776 && c <= 11903;
	}

	public static boolean isVerticalForm(int c) {
		return c >= 65040 && c <= 65055;
	}

	public static boolean isCJKPunctuation(Character.UnicodeBlock b) {
		return Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION == b;
	}

	public static boolean isFullwidthASCIIPunctuation(Character.UnicodeBlock b) {
		return Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS == b;
	}

	/**
	 * 是否为标点符号，包含任意类型的表达符号
	 */
	public static boolean isPunctuation(int c) {
		if (isASCIIPunctuation(c) || isLatin1Punctuation(c) || isSupplementalPunctuation(c) || isVerticalForm(c))
			return true;
		Character.UnicodeBlock b = Character.UnicodeBlock.of(c);
		return isGeneralPunctuation(b) || isCJKPunctuation(b) || isFullwidthASCIIPunctuation(b);
	}

	/**
	 * 是否为标点符号，包含任意类型的表达符号
	 */
	public static boolean isPunctuation(String s) {
		return s.length() == 1 && isPunctuation(s.charAt(0));
	}

}
