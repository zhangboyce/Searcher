package com.gs.cvoud.solr.segmenter.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.gs.cvoud.solr.segmenter.segment.utils.Punctuations;
import com.gs.cvoud.util.ClassLoaderUtils;
import com.gs.cvoud.util.CollectionUtils;
import com.gs.cvoud.util.StringUtils;

/**
 * 字典类，加载内置的分词数据字典，停词字典和自定义的分词字典
 * 
 * @author www.yhd.com
 * @author boyce
 * @version 2013-10-16
 */
public class Dict {
	
	// 字典文件读取的编码格式
	private static final String ENCODING = "UTF-8";

	// 内置正则表达式匹配字段文件路径
	private static final String PATTERNS_FILE = "/dict/patterns.txt";

	// 内置主要分词字典路径
	private static final String DICT_FILE = "/dict/main.txt";
	
	// 内置相似词词典
	private static final String SYNONYMS_FILE = "/dict/synonyms.txt";

	// 正则表达式匹配集合
	private List<Pattern> wordPatterns;

	// 正则表达式停词集合
	private List<Pattern> stopwordPatterns;

	// 文本分词Map
	private Map<String, Boolean> words;
	
	/**
	 * 相似词，如：<上海, 上海市><魔都, 上海市>
	 */
	private Map<String, String> similarWords;
	
	/**
	 * 用户自定义分词集合
	 */
	private List<String> dics;

	// 自定义分词文件路径
	private String dictPath;

	/**
	 * 分词字典构造方法
	 * @param dictPath 自定义分词文件路径
	 * @param dics 自定义分词集合
	 * @throws IOException 异常信息
	 */
	public Dict(String dictPath, List<String> dics) throws IOException {
		this.dictPath = dictPath;
		this.dics = dics;
		this.load();
	}
	
	/**
	 * 分词字典构造方法
	 * @param dictPath 自定义分词文件路径
	 * @throws IOException 异常信息
	 */
	public Dict(String dictPath) throws IOException {
		this(dictPath, null);
	}

	/**
	 * 分词字典构造方法
	 * @param dics 自定义分词集合
	 * @throws IOException 异常信息
	 */
	public Dict(List<String> dics) throws IOException {
		this(null, dics);
	}
	
	public Dict() throws IOException {
		this(null, null);
	}
	

	/**
	 * 分词的词语信息描述
	 * 
	 * @author www.yhd.com
	 * @author boyce
	 * @version 2013-10-16
	 */
	public static class WordInfo {
		public boolean exist;
		public boolean isStopword;

		public WordInfo() {
			exist = false;
			isStopword = false;
		}
	}
	
	/**
	 * 加载分词以及停词字典
	 * 
	 * @throws IOException
	 *             IOException
	 */
	public void load() throws IOException {
		long st = System.currentTimeMillis(); 
		
		this.words = new HashMap<String, Boolean>();
		wordPatterns = new ArrayList<Pattern>();
		stopwordPatterns = new ArrayList<Pattern>();
		similarWords = new HashMap<String, String>();
		
		//加载自定义分词集合
		if (CollectionUtils.isNotEmpty(this.dics)) {
			for (String dic: dics) {
				if (StringUtils.isNotEmpty(dic) && !Punctuations.isPunctuation(dic))
					this.words.put(dic, Boolean.valueOf(true));
			}
		}
		
		//加载相似词
		BufferedReader br = new BufferedReader(new InputStreamReader(loadResource(SYNONYMS_FILE), ENCODING));
		String dic = null;
		while(StringUtils.isNotEmpty(dic = br.readLine())) {
			dic = dic.trim();
			//不是词库注释
			if (dic.length() > 0 && !dic.startsWith("#")) {
				//该词没有相似词
				if (dic.indexOf("[") == -1) {
					if (!this.words.containsKey(dic))
						this.words.put(dic, Boolean.valueOf(true));
				}
				//有相似词，如：上海市[上海,魔都]
				else {
					int start = dic.indexOf("[");
					int end = dic.indexOf("]");
					//不合法，继续下一行
					if (start == 0 || start == -1 || end == -1 || end < start)
						continue;
					else {
						//主词：上海市
						String main = dic.substring(0, start);
						//相似词：上海,魔都
						String similar = dic.substring(start+1, end);
						String[] similars = similar.split(",");
						
						if (!this.words.containsKey(main))
							this.words.put(main, Boolean.valueOf(true));
						if (similars.length != 0) {
							for (String s: similars) {
								s = s.trim();
								if (!this.words.containsKey(s))
									this.words.put(s, Boolean.valueOf(true));
								//相似词
								similarWords.put(s, main);
							}
						}
					}
				}
					
			}
		}
		
		// 加载文本分词字段
		br = new BufferedReader(new InputStreamReader(loadResource(DICT_FILE), ENCODING));
		for (String in = br.readLine(); in != null; in = br.readLine()) {
			in = in.trim();
			if (in.length() > 0 && !in.startsWith("#")) {
				String lowWord = in.toLowerCase();
				if (!this.words.containsKey(lowWord))
					this.words.put(lowWord, Boolean.valueOf(true));
			}
		}
		br.close();

		// 加载正则分词字段和停词字段
		br = new BufferedReader(new InputStreamReader(loadResource(PATTERNS_FILE), ENCODING));
		boolean isInStopZone = false;
		for (String in = br.readLine(); in != null; in = br.readLine())
			if (in.startsWith("#!RECOGNIZE"))
				isInStopZone = false;
			else if (in.startsWith("#!STOPWORD"))
				isInStopZone = true;
			else if (!in.startsWith("#") && in.trim().length() > 0) {
				Pattern pattern = Pattern.compile(in);
				if (isInStopZone)
					stopwordPatterns.add(pattern);
				else
					wordPatterns.add(pattern);
			}
		br.close();

		// 加载自定义分词文本
		if (StringUtils.isNotEmpty(dictPath) && new File(dictPath).exists()) {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(dictPath), ENCODING));
			for (String in = br.readLine(); in != null; in = br.readLine()) {
				in = in.trim();
				if (in.length() > 0 && !in.startsWith("#")) {
					String words[] = in.split("\\s+");
					String as[];
					int j = (as = words).length;
					for (int i = 0; i < j; i++) {
						String word = as[i];
						String lowWord = word.toLowerCase();
						if (!this.words.containsKey(lowWord))
							this.words.put(lowWord, Boolean.valueOf(true));
					}

				}
			}
			br.close();
		}
		System.out.println("YHD加载词典耗时：" + (System.currentTimeMillis()-st) + " ms.");
	}
	
	/**
	 * 给定一个词，获取它的标准词
	 */
	public String getStandard(String dic) {
		String standard = this.similarWords.get(dic);
		//本身就是 标准词
		if (StringUtils.isEmpty(standard)) 
			return dic;
		//获取标准词
		return standard;
	}

	/**
	 * 字段中是否包含某个词
	 * 
	 * @param word
	 *            词语
	 * @return 是否包含的词信息描述
	 */
	public WordInfo contains(String word) {
		Boolean info = words.get(word);
		WordInfo ret = new WordInfo();
		if (info != null) {
			ret.exist = true;
			ret.isStopword = !info.booleanValue();
		}
		return ret;
	}

	/**
	 * 获取所有的正则分词集合
	 */
	public List<Pattern> getAllWordPatterns() {
		return wordPatterns;
	}

	/**
	 * 获取所有的正则停词集合
	 */
	public List<Pattern> getAllStopwordPatterns() {
		return stopwordPatterns;
	}

	public void setDictPath(String dictPath) {
		this.dictPath = dictPath;
	}

	public String getDictPath() {
		return dictPath;
	}

	/**
	 * 加载classpath路径文件
	 * 
	 * @param resource
	 *            文件路径
	 * @return 文件流
	 */
	private static InputStream loadResource(String resource) {
		return ClassLoaderUtils.getResourceAsStream(resource, Dict.class);
	}

}
