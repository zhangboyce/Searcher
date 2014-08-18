package utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;

/**
 * 从文件，磁盘路径，ClassLoader，应用上下文，流等中读取资源。
 * 
 * @author boyce
 * @verson Mar 19, 2013
 */
public class ReaderUtils {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ReaderUtils.class);

	/**
	 * 读取路径下的文件，返回byte数组
	 * 
	 * @param path
	 *            路径
	 * @return byte数组
	 */
	public byte[] read(String path) {
		if (StringUtils.isEmpty(path)) {
			return null;
		}

		File file = new File(path);

		return read(file);
	}

	/**
	 * 读取文件，返回byte数组
	 * 
	 * @param file
	 *            文件
	 * @return byte数组
	 */
	private byte[] read(File file) {
		if (null == file) {
			return null;
		}

		InputStream in = null;
		byte[] bytes = null;

		try {
			in = new FileInputStream(file);
			bytes = read(in);

		} catch (Exception e) {
			logger.error("Error.", e);
		} finally {
			IOUtils.closeQuietly(in);
		}

		return bytes;
	}

	/**
	 * 读取网络资源
	 * 
	 * @param url
	 *            网络URL
	 * @return byte数组
	 */
	public byte[] read(URL url) {
		if (null == url) {
			return null;
		}

		InputStream in = null;
		byte[] bytes = null;
		try {
			/* 此为联系获得网络资源的固定格式用法，以便后面的in变量获得url截取网络资源的输入流 */
			in = url.openStream();

			bytes = read(in);

		} catch (Exception e) {
			logger.error("Error.", e);
		} finally {

			IOUtils.closeQuietly(in);
		}

		return bytes;
	}

	/**
	 * 读取输入流，返回bytes数组
	 * 
	 * @param in
	 *            输入流
	 * @return bytes数组
	 */
	public byte[] read(InputStream in) {
		if (null == in) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		/* 将参数savePath，即将截取的图片的存储在本地地址赋值给out输出流所指定的地址 */
		byte[] buffer = new byte[4096];
		int count = 0;
		try {
			while ((count = in.read(buffer)) > 0)/* 将输入流以字节的形式读取并写入buffer中 */
			{
				out.write(buffer, 0, count);
			}

			buffer = out.toByteArray();
		} catch (IOException e) {
			logger.error("Error.", e);
		} finally {
			IOUtils.closeQuietly(out);
		}

		return buffer;
	}

}
