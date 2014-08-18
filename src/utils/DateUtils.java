package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理工具类
 * 
 * @author boyce
 * @version 2013-9-2
 */
public final class DateUtils {

	/**
	 * 获取指定日期字符串，指定格式
	 * 
	 * @param type
	 *            格式类型
	 * @param date
	 *            需要格式化的日期对象
	 * @return 格式化后的字符串
	 */
	public static String toString(Date date, String type) {

		// 格式化当前系统日期
		SimpleDateFormat dateFm = new SimpleDateFormat(type);
		String dateTime = dateFm.format(date);

		return dateTime;
	}

}
