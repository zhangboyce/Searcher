package com.gs.cvoud.solr.segmenter.segment.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.gs.cvoud.util.ClassLoaderUtils;

/**
 * 拼音帮助类
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public class PinYinHelper {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PinYinHelper.class);

	private Map<Character, List<String>> charMap;
	private Map<String, String> multiMap;
	private static PinYinHelper instance = new PinYinHelper();

	private static final String PINYIN = "/dict/pinyin.txt";
	private static final String DUOYINZI_ZUCI = "/dict/duoYinZi_ZuCi.txt";

	public PinYinHelper() {
		charMap = new LinkedHashMap<Character, List<String>>();
		multiMap = new HashMap<String, String>();
		try {
			reload();
		} catch (Exception e) {
			logger.error("Error.", e);
		}
	}

	public static PinYinHelper getInstance() {
		return instance;
	}

	/**
	 * 加载拼音处理文件
	 */
	public synchronized void reload() throws Exception {
		InputStream f1 = ClassLoaderUtils.getResourceAsStream(PINYIN, getClass());
		InputStream f2 = ClassLoaderUtils.getResourceAsStream(DUOYINZI_ZUCI, getClass());
		if (f1 == null)
			throw new IllegalArgumentException(PINYIN + " not found.");
		if (f2 == null)
			throw new IllegalArgumentException(DUOYINZI_ZUCI + " not found.");
		BufferedReader br = new BufferedReader(new InputStreamReader(f1, "utf-8"));
		String s;
		String input = "";
		for (; (input = br.readLine()) != null;) {
			char ch = input.charAt(0);
			s = input.substring(2, input.length() - 1);
			if (!charMap.containsKey(Character.valueOf(ch))) {
				List<String> spell = new LinkedList<String>();
				spell.add(s);
				charMap.put(Character.valueOf(ch), spell);
			} else {
				List<String> spell = charMap.get(Character.valueOf(ch));
				if (!spell.contains(s))
					spell.add(s);
			}
		}
		br.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(f2, "utf-8"));
		int t = 0;
		while ((s = in.readLine()) != null) {
			String ch = s.substring(0, 1);
			String arr[] = s.substring(4).split("/");
			String as[];
			int j = (as = arr).length;
			for (int i = 0; i < j; i++) {
				String temp = as[i];
				t = temp.indexOf("]");
				if (t > 0) {
					String py = (new StringBuilder(String.valueOf(ch))).append(temp.substring(1, t)).toString();
					String word = temp.substring(t + 1).trim();
					if (word.length() > 0)
						multiMap.put(py, word);
				}
			}

		}
		in.close();

		f1.close();
		f2.close();

	}

	public String[] cn2Spell(String str) {
		int len = str.length();
		String py = "";
		List<String> pinyin = new LinkedList<String>();
		int i;
		for (i = 0; i < len; i++) {
			Character ch = Character.valueOf(str.charAt(i));
			if (charMap.get(ch) == null) {
				py = (new StringBuilder(String.valueOf(py))).append(ch).toString();
			} else {
				if (py.length() > 0) {
					pinyin.add(py);
					py = "";
				}
				List<String> L = charMap.get(ch);
				if (L.size() == 1)
					pinyin.add((String) L.get(0));
				else if (L.size() > 1) {
					int j;
					for (j = 0; j < L.size(); j++) {
						String s1 = (String) L.get(j);
						String multiWords = (String) multiMap.get((new StringBuilder()).append(ch).append(s1)
								.toString());
						if (multiWords == null
								|| (i + 2 > str.length() || !multiWords.contains(str.substring(i, i + 2)))
								&& (i < 1 || !multiWords.contains(str.substring(i - 1, i + 1))))
							continue;
						pinyin.add(s1);
						break;
					}

					if (j == L.size())
						pinyin.add((String) L.get(0));
				}
			}
		}

		if (i == len && py.length() > 0)
			pinyin.add(py);
		return (String[]) pinyin.toArray(new String[0]);
	}

	public static String fuzzyTranslate(String spell) {
		StringBuffer str = new StringBuffer(spell);
		if (spell.endsWith("en") || spell.endsWith("in"))
			str.append("-g");
		else if (spell.endsWith("eng") || spell.endsWith("ing"))
			str.insert(spell.length() - 2, "-");
		if (spell.startsWith("ch") || spell.startsWith("sh") || spell.startsWith("zh"))
			str.insert(1, "-");
		else if (spell.startsWith("c") || spell.startsWith("s") || spell.startsWith("z"))
			str.insert(1, "-h");
		return str.toString();
	}

	public static void main(String args[]) {
		String input = "lucy100 登上了，海拔4800m的高峰。";
		PinYinHelper helper = getInstance();
		try {
			String pinyin[] = helper.cn2Spell(input);
			String args1[];
			int j = (args1 = pinyin).length;
			for (int i = 0; i < j; i++) {
				String s = args1[i];
				System.out.println((new StringBuilder(String.valueOf(s))).append(" ").toString());
			}

		} catch (Exception e) {
			logger.error("Error.", e);
		}
		String s = "chen";
		System.out.println(fuzzyTranslate(s));
	}

}
