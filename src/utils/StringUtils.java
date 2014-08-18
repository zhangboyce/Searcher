package utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String 工具类
 * 
 * @author boyce
 * @verson Mar 19, 2013
 */
public final class StringUtils {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(StringUtils.class);

	/**
	 * 字符串是否为空
	 * 
	 * @param source 源字符串
	 * @return 是否为空
	 */
	public static boolean isEmpty(final String source) {
		return null == source || source.trim().length() == 0;
	}

	/**
	 * 字符串是否不为空
	 * 
	 * @param source 源字符串
	 * @return 是否不为空
	 */
	public static boolean isNotEmpty(final String source) {
		return !isEmpty(source);
	}

	/**
	 * 若字符串为null, 则输出空字符串
	 * 
	 * @param src
	 *            src : 源字符串
	 * @return String;
	 */
	public static String nvl(final String src) {
		return src == null ? "" : src;
	}

	/**
	 * 若字符串为null, 则输出空字符串
	 * 
	 * @param src
	 *            src : 源字符串
	 * @parma String target: 若src=null， 则输出 target
	 * @return String;
	 */
	public static String nvl(final String src, String target) {
		return src == null ? nvl(target) : src;
	}

	/**
	 * 将一个字符串首字母大写
	 * 
	 * @param source
	 *            源字符串
	 * @return 首字母变大写的目标字符串
	 */
	public static String firstLetterToUpper(final String source) {
		if (isEmpty(source)) {
			return source;
		}

		// A ~ Z : 65 ~ 90; a ~ z : 97 ~ 122
		char[] array = source.toCharArray();

		// 字符串的第一个字母是小写的英文字母
		if (array[0] >= 97 && array[0] <= 122) {
			array[0] -= 32;

			return String.valueOf(array);
		}

		return source;

	}

	/**
	 * 去掉指定字符串后number个字符
	 * 
	 * @param source
	 *            源字符串
	 * @param number
	 *            需要取消的字符个数
	 * @return 去掉后number为的字符串
	 */
	public static String cutLastLetter(final String source, final int number) {
		if (isEmpty(source) || number <= 0) {
			return source;
		}

		if (number >= source.length()) {
			return "";
		}

		return source.substring(0, source.length() - number);
	}

	/**
	 * toString 工具类，打印实体对象信息
	 * 
	 * @param entity
	 *            打印目标对象
	 * @param entity
	 * @return 对象实体信息
	 */
	@SuppressWarnings("rawtypes")
	public static String toString(final Object entity, final String... barringFieldNames)
			 {

		Class cls = entity.getClass();

		// 获取实体所有的公共方法
		List<Method> methods = CollectionUtils.convertArray2List(cls.getMethods());

		// 参数中指定不需要toString的属性
		List<String> barringMethodNames = null;
		if (null != barringFieldNames && barringFieldNames.length != 0) {
			barringMethodNames = new ArrayList<String>();

			for (String barringFieldName : barringFieldNames) {
				barringMethodNames.add("get" + firstLetterToUpper(barringFieldName));
			}
		}

		boolean hasBarring = !CollectionUtils.isEmpty(barringMethodNames);
		List<Method> barringMethods = new ArrayList<Method>();

		// 去掉非get开头的方法和不需要打印的属性的get方法
		for (Method method : methods) {
			// 非get开头的方法
			if (!method.getName().startsWith("get")) {
				barringMethods.add(method);
				continue;
			}
			if (hasBarring) {
				// 参数指定不需要打印的方法
				for (String barringMethodName : barringMethodNames) {
					if (method.getName().equals(barringMethodName)) {
						barringMethods.add(method);
						barringMethodNames.remove(barringMethodName);

						break;
					}
				}
			}
		}

		StringBuffer buffer = new StringBuffer(cls.getSimpleName() + ": [");

		methods.removeAll(barringMethods);

		// 得到所有私有field
		Field[] fields = cls.getDeclaredFields();
		for (Method method : methods) {
			String mn = method.getName();
			for (Field field : fields) {
				String name = field.getName().toString();
				if (mn.equals("get" + firstLetterToUpper(name))) {
					Object objV = null;
					String value = "";
					try {
						objV = method.invoke(entity);

						if (null == objV) {
							value = "";
						} else if (objV.getClass().isArray()) {
							value = CollectionUtils.toString((Object[]) objV);
						} else {
							value = objV.toString();
						}
					} catch (Exception e) {
						logger.error("Error.", e);
					}

					buffer.append(name + "=\"" + value + "\", ");
				}
			}
		}

		return buffer.substring(0, buffer.length() - 2) + "]";
	}

	/**
	 * 获取文件名扩展名（小写,不带点的）
	 * 
	 * @param fileName
	 *            :文件名
	 * @return
	 */
	public static String getFileExtName(String fileName) {
		if (fileName == null) {
			return "";
		} else {
			int lastIndex = fileName.lastIndexOf(".");
			return (lastIndex == -1) ? "" : fileName.substring(lastIndex + 1).toLowerCase();
		}
	}

	/**
	 * 转换为HTML文字编码.<br>
	 */
	public static String txtToHtml(String txt) {
		if (txt == null) {
			return "";
		}
		return txt.trim().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;")
				.replaceAll("'", "&#039;").replaceAll(" ", "&nbsp;").replaceAll("\r\n", "<br>")
				.replaceAll("\r", "<br>").replaceAll("\n", "<br>");
	}

	/**
	 * 
	 */
	public static boolean contains(String[] strs, String str) {
		boolean contains = false;

		outer: for (String s : strs) {
			if (isEmpty(s)) {
				continue outer;
			}

			if (str.trim().equals(s.trim())) {
				contains = true;
				break;
			}
		}

		return contains;
	}

	/**
	 * <b>功能： </b>return string==null?"":string.trim()
	 * 
	 */
	public static String stringTrim(String string) {
		return string == null ? "" : string.trim();
	}

	/**
	 * 根据正则，获得匹配内容
	 * 
	 * @param content
	 * @param format
	 * @return
	 */
	public static List<String> re(String content, String format) {
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(content);
		Map<String, String> emailMap = new HashMap<String, String>();
		while (matcher.find()) {
			emailMap.put(matcher.group(), matcher.group());
		}
		Iterator<String> iterator = emailMap.values().iterator();
		List<String> alist = new ArrayList();
		while (iterator.hasNext()) {
			alist.add(iterator.next());
		}
		return alist;
	}

	public static String encodeFileName(String fileName) {
		String returnFileName = "";
		if (StringUtils.isEmpty(fileName)) {
			return returnFileName;
		}
		try {
			returnFileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			logger.error("Error.", e);
		}

		return returnFileName;
	}

	/**
	 * 将string[]转换成Long类型的数组
	 * 
	 * @param strs
	 *            String类型的数组
	 * @return Long类型的数组
	 */
	public static Long[] parseToLong(String... strs) {
		if (ObjectUtils.isNotNull(strs) && strs.length != 0) {
			Long[] longs = new Long[strs.length];
			for (int i = 0; i < strs.length; i++) {
				try {
					Long lg = Long.parseLong(strs[i]);
					longs[i] = lg;
				} catch (NumberFormatException exception) {
					longs[i] = -1L;
				}
			}

			return longs;
		}

		return null;
	}

	// 获取指定路径下的html文件的encoding
	public static String getEncodingFromHtmlContent(String content) {
		String regex = "";//".+(?:charset|encoding) ?= ?\"?'?([\\w\\d-_ ]+).*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);

		String encoding = null;
		while (matcher.find()) {
			encoding = matcher.group(1);
		}
		return encoding;
	}

	/**
	 * 删除中文汉字之间的空白
	 * 
	 * @return 删除空白之后的内容
	 */
	public static String deleteBlankInterChinese(String resource) {
		if (isEmpty(resource)) {
			return resource;
		}

		String regex = "[\\u4e00-\\u9fa5]+([\\t| ]+)[\\u4e00-\\u9fa5]+";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(resource);

		while (matcher.find()) {
			resource = resource.replaceAll(matcher.group(1), "");
		}

		return resource;
	}
	
	/**
	 * 截取字符串前length个字符的字串
	 */
	public static String cut(String resource, int length) {
		if (isEmpty(resource) || length <=0 )
			return resource;
		
		int l = resource.length();
		if (l <= length)
			return resource;
		
		return resource.substring(0, length);
	}
	
	public static String valueOf(Object obj) {
		return (obj == null) ? null : obj.toString();
	} 

	public static void main(String[] args) {
		String content = "我  说  的   	多了，爱	的	却	少了，我	们	的	仇 we 恨	也 hello 更	多	了We talk much, we莫	名的	哭泣 love 想	你	的	时候 only a little, and we hate too much";
		System.out.println(deleteBlankInterChinese(content));
	}
	
}
