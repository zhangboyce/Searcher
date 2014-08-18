package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 中文处理工具类
 * 
 * @author boyce
 * @version 2013-10-16
 */
public final class ChineseUtils {
	public static Map<Character, Integer> CHINESE_DIDIT_MAP;
	public static HashMap<Character, Integer> CHINESE_DIDIT_MAP_1;

	static {
		CHINESE_DIDIT_MAP = new HashMap<Character, Integer>();
		CHINESE_DIDIT_MAP_1 = new HashMap<Character, Integer>();
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u96F6'), Integer.valueOf(0));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u4E00'), Integer.valueOf(1));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u58F9'), Integer.valueOf(1));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u4E8C'), Integer.valueOf(2));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u8D30'), Integer.valueOf(2));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u4E09'), Integer.valueOf(3));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u56DB'), Integer.valueOf(4));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u4E94'), Integer.valueOf(5));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u4F0D'), Integer.valueOf(5));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u516D'), Integer.valueOf(6));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u4E03'), Integer.valueOf(7));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u67D2'), Integer.valueOf(7));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u516B'), Integer.valueOf(8));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u634C'), Integer.valueOf(8));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u4E5D'), Integer.valueOf(9));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u7396'), Integer.valueOf(9));
		CHINESE_DIDIT_MAP_1.put(Character.valueOf('\u5341'), Integer.valueOf(10));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u96F6'), Integer.valueOf(0));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u4E00'), Integer.valueOf(1));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u58F9'), Integer.valueOf(1));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u4E8C'), Integer.valueOf(2));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u8D30'), Integer.valueOf(2));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u4E24'), Integer.valueOf(2));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u4E09'), Integer.valueOf(3));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u53C1'), Integer.valueOf(3));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u56DB'), Integer.valueOf(4));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u8086'), Integer.valueOf(4));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u4E94'), Integer.valueOf(5));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u4F0D'), Integer.valueOf(5));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u516D'), Integer.valueOf(6));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u9646'), Integer.valueOf(6));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u4E03'), Integer.valueOf(7));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u67D2'), Integer.valueOf(7));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u516B'), Integer.valueOf(8));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u634C'), Integer.valueOf(8));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u4E5D'), Integer.valueOf(9));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u7396'), Integer.valueOf(9));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u5341'), Integer.valueOf(10));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u62FE'), Integer.valueOf(10));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u767E'), Integer.valueOf(100));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u4F70'), Integer.valueOf(100));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u5343'), Integer.valueOf(1000));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u4EDF'), Integer.valueOf(1000));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u4E07'), Integer.valueOf(10000));
		CHINESE_DIDIT_MAP.put(Character.valueOf('\u842C'), Integer.valueOf(10000));
	}

	private ChineseUtils() {
	}

	public static boolean isDigit(char c) {
		return CHINESE_DIDIT_MAP.containsKey(Character.valueOf(c));
	}

	public static int parseDigit(char buf[], int length) {
		if (length == 1) {
			Integer c = (Integer) CHINESE_DIDIT_MAP_1.get(Character.valueOf(buf[0]));
			if (c != null)
				return c.intValue();
			else
				return 0;
		}
		int d = 0;
		int d3 = 0;
		int d0 = 0;
		for (int i = 0; i < length; i++) {
			int c = ((Integer) CHINESE_DIDIT_MAP.get(Character.valueOf(buf[i]))).intValue();
			if (c < 10)
				d0 = d0 * 10 + c;
			else if (c < 10000) {
				d3 += c * (d0 != 0 ? d0 : 1);
				d0 = 0;
			} else {
				d = (d3 + d0) * c;
				d3 = 0;
				d0 = 0;
			}
		}

		return d + d3 + d0;
	}
	
	public static boolean isChinese(char c) {
		
		 return Character.toString(c).matches("[\\u4E00-\\u9FA5]+");
	}
	
	public static boolean isAllChinese(String str) {
		
		if (StringUtils.isEmpty(str))
			return false;
		char[] chars = str.toCharArray();
		for (char c: chars) {
			if (!isChinese(c))
				return false;
		}
		return true;
	}

	public static void main(String args[]) throws Exception {
		String target = "八千四百万";
		System.out.println(parseDigit(target.toCharArray(), target.length()));
	}

}
