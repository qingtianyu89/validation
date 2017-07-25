package com.bj58.daojia.dop2c.utils;

import com.bj58.daojia.dop2c.common.AbsValidationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pangming on 2017/4/21.
 */
public class FileUtil {

    private static Logger log = LoggerFactory.getLogger(FileUtil.class);

    public static void main(String[] args) {
        Class aClass = matchSon(AbsValidationConfig.class);
        System.out.println(aClass);
    }

    public static Class matchSon(Class<?> aClass) {
        if (aClass == null) {
            return null;
        }
        List<File> classFile = new ArrayList<File>();
        String rootPath = FileUtil.class.getResource("/").getPath();
        log.info("扫描目录，rootPath={}", rootPath);
        scan(new File(rootPath), classFile);
        for (File file : classFile) {
            Class clazz = getClassByFile(file);
            if (clazz == null) {
                continue;
            }
            if (aClass.isAssignableFrom(clazz) && !clazz.equals(aClass)) {
                return clazz;
            }
        }
        return null;
    }

    private static void scan(File file, List<File> classList) {
        if (file == null) {
            return;
        }
        if (file.isFile()) {
            classList.add(file);
            return;
        }
        File[] filesList = file.listFiles();
        if (filesList == null || filesList.length == 0) {
            return;
        }
        for (File file1 : filesList) {
            if (file1.isFile()) {
                classList.add(file1);
                continue;
            }
            scan(file1, classList);
        }
    }

    private static Class getClassByFile(File file) {
        Class<?> aClass = null;
        try {
            String absolutePath = file.getAbsolutePath();
            String tempPath = absolutePath.substring(absolutePath.indexOf("classes") + 8);
            if (!tempPath.endsWith(".class")) {
                return null;
            }
            tempPath = tempPath.replace(".class", "");
            String classPath = tempPath.replaceAll("\\\\", ".");
            classPath = classPath.replaceAll("/", ".");
            aClass = Class.forName(classPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aClass;
    }
}
