package com.gs.cvoud.solr;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.gs.cvoud.attachment.converter.Converter;
import com.gs.cvoud.attachment.converter.ConverterFactory;
import com.gs.cvoud.attachment.resume.ResumeType;
import com.gs.cvoud.solr.segmenter.segment.Segmenter;
import com.gs.cvoud.solr.segmenter.segment.SegmenterFactory;
import com.webssky.jcseg.core.ADictionary;
import com.webssky.jcseg.core.DictionaryFactory;
import com.webssky.jcseg.core.ISegment;
import com.webssky.jcseg.core.IWord;
import com.webssky.jcseg.core.JcsegTaskConfig;
import com.webssky.jcseg.core.SegmentFactory;

public class SegmenterUtil {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SegmenterUtil.class);

	public static abstract class ContentSegmenter {
		
		protected List<String> dics;
		
		/**
		 * 分词方法
		 * @param str 被分词对象
		 * @return 分词后的List
		 */
		 public abstract List<String> segment(String str);
		
		/**
		 * 加载自定义词组集合
		 * @param dics 自定义分词词组
		 * @return 当前对象
		 */
		 public ContentSegmenter load(List<String> dics) {
			this.dics = dics;
			return this;
		}
	}

	public static final ContentSegmenter JCSEG = new ContentSegmenter() {
		private ThreadLocal<ISegment> SEG = new ThreadLocal<ISegment>();

		private final ISegment getInstance() {
			ISegment seg = SEG.get();
			if (seg == null) {
				// 创建 JcsegTaskConfig 分词任务实例
				// 即从 jcseg.properties 配置文件中初始化的配置
				JcsegTaskConfig config = new JcsegTaskConfig();
				// 创建默认词库(即: com.webssky.jcseg.Dictionary 对象)
				// 并且依据给定的 JcsegTaskConfig 配置实例自主完成词库的加载
				ADictionary dic = DictionaryFactory.createDefaultDictionary(config);
				// 依据给定的ADictionary和JcsegTaskConfig来创建ISegment
				// 通常使用SegmentFactory#createJcseg来创建ISegment对象
				// 将config和dic组成一个Object数组给SegmentFactory.createJcseg方法
				// JcsegTaskConfig.COMPLEX_MODE表示创建ComplexSeg复杂ISegment分词对象
				// JcsegTaskConfig.SIMPLE_MODE表示创建SimpleSeg简易Isegmengt分词对象.
				// 设置要分词的内容
				try {
					seg = SegmentFactory.createJcseg(JcsegTaskConfig.COMPLEX_MODE, new Object[] { config, dic });
					SEG.set(seg);
				} catch (Exception e) {
					logger.error("Failed on initializing segmenter", e);
				}
			}
			return seg;
		}

		public List<String> segment(String str) {
			List words = new ArrayList();
			try {
				ISegment seg = getInstance();
				seg.reset(new StringReader(str));

				// 获取分词结果
				IWord word = null;
				while ((word = seg.next()) != null) {
					words.add(word.getValue());
				}
			} catch (Exception e) {
				logger.error("Failed on segmenting str" + str, e);
			}
			return words;
		}
	};

	public static final ContentSegmenter IKSEG = new ContentSegmenter() {
		public List<String> segment(String str) {
			List words = new ArrayList();
			IKSegmenter ik = new IKSegmenter(new StringReader(str), true);// 当为true时，分词器进行最大词长切分
			try {
				Lexeme lexeme = null;
				while ((lexeme = ik.next()) != null)
					words.add(lexeme.getLexemeText());
			} catch (IOException e) {
				logger.error("Failed on segmenting str" + str, e);
			}
			return words;
		}
	};

	public static final ContentSegmenter YHDSEG = new ContentSegmenter() {
		//向后最大匹配
		private Segmenter segmenter = SegmenterFactory.newBackwardMaxSegmenter(dics);;
		public List<String> segment(String str) {
			return segmenter.segment(str);
		}
		
	};

	public static void main(String[] args)  {
		Converter converter = ConverterFactory.getConverter(ResumeType.WORD2003);
		//String str = converter.convert2Text("D:\\简历集合\\解析\\51-04.doc");
		String str = "产品经理浦东";

		long t1 = System.currentTimeMillis();
		System.out.println("IKSEG: " + SegmenterUtil.IKSEG.segment(str));
		long t2 = System.currentTimeMillis();
		System.out.println("take: " + (t2 - t1) + "ms.");

		System.out.println("JCSEG: " + SegmenterUtil.JCSEG.segment(str));
		long t3 = System.currentTimeMillis();
		System.out.println("take: " + (t3 - t2) + "ms.");

		System.out.println("YHDSEG: " + SegmenterUtil.YHDSEG.segment(str));
		long t4 = System.currentTimeMillis();
		System.out.println("take: " + (t4 - t3) + "ms.");
	}
}
