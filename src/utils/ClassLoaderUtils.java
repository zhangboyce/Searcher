package utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 类加载器工具类
 * 
 * @author boyce
 * @verson Mar 19, 2013
 */
public final class ClassLoaderUtils {
	/**
	 * 加载资源
	 * 
	 * @param resourceName
	 *            资源名称
	 * @param callingClass
	 *            调用类
	 * @return URL
	 */
	public static URL getResource(String resourceName, Class<?> callingClass) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);

		if (url == null) {
			url = ClassLoaderUtils.class.getClassLoader().getResource(resourceName);
		}

		if (url == null) {
			ClassLoader cl = callingClass.getClassLoader();

			if (cl != null) {
				url = cl.getResource(resourceName);
			}
		}

		if ((url == null) && (resourceName != null)
				&& ((resourceName.length() == 0) || (resourceName.charAt(0) != '/'))) {
			return getResource('/' + resourceName, callingClass);
		}

		return url;
	}

	/**
	 * 以流的方式加载资源
	 * 
	 * @param resourceName
	 *            资源名称
	 * @param callingClass
	 *            调用类
	 * @return InputStream
	 */
	public static InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
		URL url = getResource(resourceName, callingClass);

		try {
			return (url != null) ? url.openStream() : null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 根据指定类名加载一个类
	 * 
	 * @param className
	 *            类名
	 * @param callingClass
	 *            调用者
	 * @return 加载的类
	 * @throws ClassNotFoundException
	 *             找不到类异常
	 */
	public static Class<?> loadClass(String className, Class<?> callingClass) throws ClassNotFoundException {
		try {
			// 返回当前线程的上下文ClassLoader
			return Thread.currentThread().getContextClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			try {
				/*
				 * Class.forName("xx.xx")等同于Class.forName("xx.xx",true,CALLClass.class.getClassLoader())，
				 * 第二个参数(bool)表示装载类的时候是否初始化该类，即调用类的静态块的语句及初始化静态成员变量。
				 * 
				 * ClassLoader loader = Thread.currentThread.getContextClassLoader();
				 * //也可以用(ClassLoader.getSystemClassLoader()) Class cls = loader.loadClass("xx.xx");
				 * //这句话没有执行初始化，其实与Class.forName("xx.xx"，false，loader)是一致的，只是loader.loadClass("xx.xx")执行的是更底层的操作。
				 * 只有执行cls.NewInstance()才能够初始化类，得到该类的一个实例
				 */
				return Class.forName(className);
			} catch (ClassNotFoundException ex) {
				try {
					return ClassLoaderUtils.class.getClassLoader().loadClass(className);
				} catch (ClassNotFoundException exc) {
					return callingClass.getClassLoader().loadClass(className);
				}
			}
		}
	}

	/**
	 * 打印当前线程的类加载器结构
	 */
	public static void printClassLoader() {
		System.out.println("ClassLoaderUtils.printClassLoader");
		printClassLoader(Thread.currentThread().getContextClassLoader());
	}

	/**
	 * 打印指定classLoader的类加载器结构
	 */
	public static void printClassLoader(ClassLoader cl) {
		System.out.println("ClassLoaderUtils.printClassLoader(cl = " + cl + ")");

		if (cl != null) {
			printClassLoader(cl.getParent());
		}
	}
}
