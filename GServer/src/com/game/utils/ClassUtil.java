package com.game.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/***
 *  类扫描工具
 * @author 3461
 */
public class ClassUtil {

	private final static String JAR_TEXT = ".jar";
	private final static String CLASS_TEXT = ".class";
	private final static String DOT = ".";
	private final static String NONE = "";
	private final static String ZIP_SLASH = "/";
	
	public static List<Class<?>> scannerPack(String packName, ClassFilter filter) {
		List<Class<?>> classes = new ArrayList<>();
		for (String path : getClassPathArray()) {
			fillClasses(new File(path), packName, filter, classes);
		}
		return classes;
	}
	
	private static void fillClasses(File file, String packName, ClassFilter filter, List<Class<?>> classes) {
		if (isDirectory(file)) {
			processDirectory(file, packName, filter, classes);
		} else if (isClassFile(file.getName())) {
			processClassFile(file, packName, filter, classes);
		} else if (isJarFile(file.getName())) {
			processJarFile(file, packName, filter, classes);
		}
	}
	
	private static void processJarFile(File file, String packName, ClassFilter filter, List<Class<?>> classes) {
		try {
			for (ZipEntry entry : Collections.list(new ZipFile(file).entries())) {
				if (isClassFile(entry.getName())) {
					final String className = entry.getName().replace(ZIP_SLASH, DOT).replace(CLASS_TEXT, NONE);
					fillClass(className, packName, filter, classes);
				}
			}
		} catch (Exception e) {
		}
	}
	
	private static void processDirectory(File directory, String packName, ClassFilter filter, List<Class<?>> classes) {
		for (File file : directory.listFiles(fileFilter)) {
			fillClasses(file, packName, filter, classes);
		}
	}

	private static void processClassFile(File file, String packName, ClassFilter filter, List<Class<?>> classes) {
		final String filePathWithDot = file.getAbsolutePath().replace(File.separator, DOT);
		int subindex = -1;
		if ((subindex = filePathWithDot.indexOf(packName)) != -1) {
			final String className = filePathWithDot.substring(subindex).replace(CLASS_TEXT, NONE);
			fillClass(className, packName, filter, classes);
		}
	}

	private static void fillClass(String className, String packName, ClassFilter filter, List<Class<?>> classes) {
		if (checkClassName(className, packName)) {
			try {
				Class<?> clazz = Class.forName(className, Boolean.FALSE, ClassUtil.class.getClassLoader());
				if (checkClassFilter(clazz, filter)) {
					classes.add(clazz);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean checkClassFilter(Class<?> clazz, ClassFilter filter) {
		return filter == null || filter.filter(clazz);
	}

	private static boolean checkClassName(String className, String packName) {
		return className.indexOf(packName) == 0;
	}

	/** 获取所有classpath下的jar包 */
	private static String[] getClassPathArray() {
		// classPath路径
		String classpath = System.getProperty("java.class.path");
		// jre路径
		String jre = System.getProperty("java.home");
		// 分隔符
		String separaror = System.getProperty("path.separator");

		return classpath.concat(separaror).concat(jre).split(separaror);
	}

	/** 文件过滤器，过滤掉不需要的文件 */
	private static FileFilter fileFilter = new FileFilter() {
		@Override
		public boolean accept(File file) {
			return isClassFile(file.getName()) || isDirectory(file) || isJarFile(file.getName());
		}
	};

	private static boolean isClassFile(String fileName) {
		return fileName.endsWith(CLASS_TEXT);
	}

	private static boolean isJarFile(String fileName) {
		return fileName.endsWith(JAR_TEXT);
	}

	private static boolean isDirectory(File file) {
		return file.isDirectory();
	}
}
