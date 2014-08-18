package utils;

import java.io.*;

/**
 * 对象操作工具类
 * 
 * @author boyce
 * @since version1.0 May 3, 2013
 */
public final class ObjectUtils {
	/**
	 * Object的深度复制
	 * 
	 * @param src
	 *            需要被复制的目标Object
	 * @return 返回复制出来的Object
	 * @throws java.io.IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> T deepCopy(T src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

		objectOutputStream.writeObject(src);

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

		@SuppressWarnings("unchecked")
		T dest = (T) objectInputStream.readObject();

		return dest;
	}

	/**
	 * 判断两个对象是否equals
	 * 
	 * @param obj1
	 * @param obj2
	 * @return 是否equals
	 */
	public static boolean equals(Object obj1, Object obj2) {
		if (obj1 == obj2) {
			return true;
		}

		if (null == obj1 || null == obj2) {
			return false;
		}

		return obj1.equals(obj2);
	}

	/**
	 * 判断两个对象是否不equals
	 * 
	 * @param obj1
	 * @param obj2
	 * @return 是否不equals
	 */
	public static boolean notEquals(Object obj1, Object obj2) {
		return !equals(obj1, obj2);
	}

	/**
	 * 计算一个对象的hashcode值
	 * 
	 * @param obj
	 *            计算对象
	 * @return hashCode值
	 */
	public static int hashCode(Object obj) {
		if (null == obj) {
			return 0;
		}

		// 八种基本类型的hashcode计算方式
		if (obj instanceof Boolean) {
			return (Boolean) obj ? 1 : 0;
		}

		if (obj instanceof Byte || obj instanceof Character || obj instanceof Short || obj instanceof Integer) {
			return (int) (Integer) obj;
		}

		if (obj instanceof Long) {
			return (int) ((Long) obj ^ ((Long) obj >>> 32));
		}

		if (obj instanceof Float) {
			return Float.floatToIntBits((Float) obj);
		}

		if (obj instanceof Double) {
			long l = Double.doubleToLongBits((Double) obj);

			return (int) (l ^ (l >>> 32));
		}

		// 对象类型直接返回其hashcode值
		return obj.hashCode();
	}

	/**
	 * 判断对象是否为null
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		return null == obj;
	}

	/**
	 * 判断对象数组中是否有对象为null
	 * 
	 * @param objs
	 * @return true/存在null对象; false/不存在null对象
	 */
	public static boolean hasNull(Object... objs) {
		boolean hasNull = false;

		if (isNull(objs)) {
			return true;
		}

		for (Object obj : objs) {
			if (isNull(obj)) {
				hasNull = true;
				break;
			}
		}

		return hasNull;
	}

	/**
	 * 判断对象数组中是否全部对象都不为null
	 * 
	 * @param objs
	 * @return true/不存在null对象; false/存在null对象
	 */
	public static boolean hasNotNull(Object... objs) {
		return !hasNull(objs);
	}

	/**
	 * 判断对象数组中的对象是否全为null
	 * 
	 * @param objs
	 * @return true/全为null; false/不是全为null
	 */
	public static boolean isAllNull(Object... objs) {
		boolean isAllNull = true;

		if (isNull(objs)) {
			return true;
		}

		for (Object obj : objs) {
			if (isNotNull(obj)) {
				isAllNull = false;
				break;
			}
		}

		return isAllNull;
	}

	/**
	 * 判断对象数组中的对象是否全不为null
	 * 
	 * @param objs
	 * @return true/全不为null; false/不是全不为null
	 */
	public static boolean isAllNotNull(Object... objs) {
		boolean isAllNotNull = true;

		if (isNull(objs)) {
			return false;
		}

		for (Object obj : objs) {
			if (isNull(obj)) {
				isAllNotNull = false;
				break;
			}
		}

		return isAllNotNull;
	}

	/**
	 * 判断对象数组中的对象是否不是全为null
	 * 
	 * @param objs
	 * @return true/不是全为null; false/是全为null
	 */
	public static boolean isNotAllNull(Object... objs) {
		return !isAllNull(objs);
	}

	/**
	 * 判断对象是否不为null
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}

}
