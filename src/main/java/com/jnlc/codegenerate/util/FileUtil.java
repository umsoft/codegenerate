package com.jnlc.codegenerate.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件操作功能类
 * @author lisl
 *
 */
public class FileUtil {
	/**
	 * 获取指定文件下的文件信息
	 * @param targetProject
	 * @return
	 */
	public static List<File> getFiles(String filePath) {
		File file=new File(filePath);
		File[] files=file.listFiles();
		List<File> fileList=new ArrayList<File>();
		fileList= Arrays.asList(files);
		return fileList;
	}
}
