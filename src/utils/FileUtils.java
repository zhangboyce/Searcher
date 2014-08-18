package utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作工具类
 * 
 * @author boyce
 * @verson Mar 19, 2013
 */
public class FileUtils {

	/**
	 * 日志记录器
	 */
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FileUtils.class);

	private static HashMap<String, String> contentTypeMap = new HashMap<String, String>();
	static {
		contentTypeMap.put("*", "application/octet-stream");
		contentTypeMap.put("doc", "application/msword");
		contentTypeMap.put("docx", "application/msword");
		contentTypeMap.put("xls", "application/vnd.ms-excel");
		contentTypeMap.put("xlsx", "application/vnd.ms-excel");
		contentTypeMap.put("ppt", "application/vnd.ms-powerpoint");
		contentTypeMap.put("pptx", "application/vnd.ms-powerpoint");
		contentTypeMap.put("pdf", "application/pdf");
		contentTypeMap.put("txt", "text/plain");
		contentTypeMap.put("rtf", "application/rtf");

		contentTypeMap.put("xml", "text/xml");
		contentTypeMap.put("swf", "application/x-shockwave-flash");

	}

	/**
	 * 判断文件或者目录是否存在
	 */
	public static boolean exists(String filePath) {
		return ObjectUtils.isNotNull(filePath) && new File(filePath).exists();
	}

	/**
	 * 判断文件或者目录是否不存在
	 */
	public static boolean notExists(String filePath) {
		return !exists(filePath);
	}

	/**
	 * 通过文件名或扩展名，获得contentType
	 * 
	 * @param fileName
	 *            :文件名abc.doc 或扩展名 .doc或 doc
	 * @return
	 */
	public static String getContentType(String fileName) {
		if (fileName == null) {
			return contentTypeMap.get("*");
		}
		int lastIndex = fileName.lastIndexOf(".");
		if (lastIndex != -1) {
			fileName = fileName.substring(lastIndex + 1);
		}
		String contentType = contentTypeMap.get(fileName.toLowerCase());
		if (contentType == null) {
			return contentTypeMap.get("*");
		} else {
			return contentType;
		}
	}

	/**
	 * 将内容写到指定的文件中
	 * 
	 * @param filePath
	 *            写入文件的虚拟路径
	 * @param content
	 *            需要写入的文件内容
	 * @param charset
	 *            文件内容从字符到字节的转码方式
	 * @return 是否写入成功
	 */
	public static boolean writeFile(String filePath, String content, String charset) {
		return writeFile(new File(filePath), content, charset);
	}

	/**
	 * 将内容写到指定的文件中
	 * 
	 * @param file
	 *            写入文件的虚拟路径
	 * @param content
	 *            需要写入的文件内容
	 * @param charset
	 *            文件内容从字符到字节的转码方式
	 * @return 是否写入成功
	 */
	public static boolean writeFile(File file, String content, String charset) {
		if (ObjectUtils.isNull(file) || StringUtils.isEmpty(content))
			return false;

		try {
			IOUtils.write(content, new FileOutputStream(file), charset);
		} catch (Exception e) {
			logger.error("Failed to write file[{}].", file.getPath(), e);
			return false;
		}
		return true;
	}

	/**
	 * 读入字符文件内容
	 * 
	 * @param filePath
	 *            ：文件路径
	 * @return 读取的字符串
	 */
	public static String readFile(String filePath, String charset) {
		try {
			return IOUtils.toString(new FileInputStream(filePath), charset);
		} catch (Exception e) {
			logger.error("Failed to read file[{}].", filePath, e);
		}
		return "";
	}

	/**
	 * 读入字符文件内容
	 * 
	 * @param filePath
	 *            ：文件路径
	 * @return 读取的字符串
	 * @throws java.io.IOException
	 */
	public static String readFile(String filePath) {
		return readFile(filePath, null);
	}

	/**
	 * 压缩指定文件
	 *
	 * @param zipFiles
	 * @param targetFile
	 */
	public static void zip(Map<String, InputStream> zipFiles, String targetFile) {
		if (zipFiles.size() == 0 || StringUtils.isEmpty(targetFile)) {
			return;
		}

		String dictory = targetFile.substring(0, targetFile.lastIndexOf(File.separator));
		File dictoryFile = new File(dictory);
		dictoryFile.mkdirs();

		ZipOutputStream out = null;
		InputStream inputStream = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(targetFile));
			for (String fileName : zipFiles.keySet()) {
				inputStream = zipFiles.get(fileName);
				if (ObjectUtils.isNotNull(inputStream)) {
					try {
						ZipEntry zipEntry = new ZipEntry(fileName);
						out.putNextEntry(zipEntry);
						IOUtils.copy(inputStream, out);
					} catch (Exception e) {
						logger.error("Zip file error.", e);
					} finally {
						IOUtils.closeQuietly(inputStream);
					}
				}
			}
		} catch (FileNotFoundException e1) {
			logger.error("Zip file error.", e1);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * 解压zip文件
	 *
	 * @param zipFileName
	 *            待解压的zip文件路径，例如：c:\\a.zip
	 * @param outputDirectory
	 *            解压目标文件夹,例如：c:\\a\
	 */

	public static void unZip(String zipFileName, String outputDirectory) throws Exception {
		ZipFile zipFile = new ZipFile(zipFileName);
		try {
			Enumeration<?> e = zipFile.entries();
			ZipEntry zipEntry = null;
			createDirectory(outputDirectory, "");

			while (e.hasMoreElements()) {
				zipEntry = (ZipEntry) e.nextElement();

				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File(outputDirectory + File.separator + name);
					f.mkdir();

					logger.debug("创建目录：" + outputDirectory + File.separator + name);
				} else {
					String fileName = zipEntry.getName();
					fileName = fileName.replace('\\', '/');
					if (fileName.indexOf("/") != -1) {
						createDirectory(outputDirectory, fileName.substring(0, fileName.lastIndexOf("/")));
						fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
					}
					File f = new File(outputDirectory + File.separator + zipEntry.getName());
					f.createNewFile();
					InputStream in = zipFile.getInputStream(zipEntry);
					FileOutputStream out = new FileOutputStream(f);
					byte[] by = new byte[1024];
					int c;
					while ((c = in.read(by)) != -1) {
						out.write(by, 0, c);
					}
					in.close();
					out.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			zipFile.close();

			logger.debug("解压完成！");

		}


	}

	/**
	 * 创建指定路径的目录
	 *
	 * @param directory
	 * @param subDirectory
	 */
	private static void createDirectory(String directory, String subDirectory) {
		String dir[];
		File fl = new File(directory);
		try {
			if (StringUtils.isEmpty(subDirectory) && !fl.exists()) {
				fl.mkdir();
			} else if (!StringUtils.isEmpty(subDirectory)) {
				dir = subDirectory.replace('\\', '/').split("/");
				for (int i = 0; i < dir.length; i++) {
					File subFile = new File(directory + File.separator + dir[i]);

					if (!subFile.exists())
						subFile.mkdir();

					directory += File.separator + dir[i];
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *
	 * 拷贝文一个文件到另外一个文件夹
	 *
	 * @param resourceFilePath
	 *            源文件
	 * @param desDirector
	 *            目标文件夹
	 * @return 临时文件路径
	 */
	public static String copyFile2Director(String resourceFilePath, String desDirector) {
		if (StringUtils.isEmpty(resourceFilePath) || StringUtils.isEmpty(desDirector)) {
			return null;
		}

		File resourceFile = new File(resourceFilePath);
		if (!resourceFile.exists() || !resourceFile.isFile()) {
			return null;
		}

		return copyFile2Director(resourceFilePath, resourceFile.getName(), desDirector);
	}

	/**
	 * 拷贝一个文件到另一个文件夹
	 *
	 * @param resourceFilePath
	 *            源文件路径
	 * @param targetFileName
	 *            目标文件名称
	 * @param desDirector
	 *            拷贝的目标目录
	 * @return 新文件路径
	 */
	private static String copyFile2Director(String resourceFilePath, String targetFileName, String desDirector) {

		if (StringUtils.isEmpty(resourceFilePath) || StringUtils.isEmpty(targetFileName)
				|| StringUtils.isEmpty(desDirector)) {
			return null;
		}

		File resourceFile = new File(resourceFilePath);
		File targetDirectory = new File(desDirector);

		// 如果源文件不存在或者不是一个文件
		if (!resourceFile.exists() || !resourceFile.isFile()) {
			return null;
		}

		targetDirectory.mkdirs();
		logger.info("Copy：" + resourceFile.getAbsolutePath() + "-->" + desDirector + "/");
		FileInputStream input = null;
		FileOutputStream output = null;
		try {

			input = new FileInputStream(resourceFile);
			output = new FileOutputStream(desDirector + File.separator + targetFileName);
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}

			output.flush();
		} catch (Exception e) {
			logger.error("Error.", e);
		} finally {
			try {
				if (ObjectUtils.isNotNull(output)) {
					output.close();
				}
				if (ObjectUtils.isNotNull(input)) {
					input.close();
				}
			} catch (IOException e) {
				logger.error("Error.", e);
			}
		}

		return desDirector + File.separator + targetFileName;
	}

	/**
	 *
	 * 删除文件夹
	 *
	 * @param folderPath
	 *            folderPath 文件夹完整绝对路径
	 *
	 */
	public static void delFolder(String folderPath) throws Exception {
		// 删除完里面所有内容
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);

		// 删除空文件夹
		myFilePath.delete();
		logger.info("Delete file: " + myFilePath.getAbsolutePath());
	}

	/**
	 *
	 * 删除指定文件夹下所有文件
	 *
	 * @param path
	 *            文件夹完整绝对路径
	 */

	public static boolean delAllFile(String path) throws Exception {

		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}

		if (!file.isDirectory()) {
			return flag;
		}

		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				f.delete();
				logger.info("Delete file: " + f.getAbsolutePath());
			} else {
				// 先删除文件夹里面的文件
				delAllFile(path + File.separator + f.getName());
				delFolder(path + File.separator + f.getName());
				flag = true;

			}
		}
		return flag;
	}

	/**
	 * 获得zip 压缩编码格式
	 *
	 * @param sourceFileName
	 * @return
	 */
	public static String getZipEncode(String sourceFileName) {
		String encode = "GBK";
		try {
			org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(sourceFileName);
			Enumeration<?> e = zipFile.getEntries();
			org.apache.tools.zip.ZipEntry zipEntry;
			while (e.hasMoreElements()) {
				zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
				int platformI = zipEntry.getPlatform();// 获得平台
				encode = getZipEncode(platformI);
				break;
			}
		} catch (IOException e) {
			logger.error("Get zip encoding error.", e);
		}
		return encode;
	}

	public static int getZipFileCount(org.apache.tools.zip.ZipFile zipFile) {
		if (ObjectUtils.isNull(zipFile)) {
			return 0;
		}
		int count = 0;
		Enumeration<?> e = zipFile.getEntries();
		org.apache.tools.zip.ZipEntry zipEntry = null;
		while (e.hasMoreElements()) {
			zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
			if (!zipEntry.isDirectory())
				count++;
		}
		return count;
	}

	/**
	 * 根据平台ID 获得编码格式
	 * 
	 * @param encodeI
	 * @return
	 */
	private static String getZipEncode(int encodeI) {
		String encode = "GBK";
		switch (encodeI) {
		case 0:
			encode = "GBK";
			break;
		case 3:
			encode = "UTF-8";
			break;
		default:
			break;
		}
		return encode;
	}
}
