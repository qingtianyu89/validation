package com.bj58.daojia.dop2c.utils;

import com.bj58.daojia.dop2c.common.AbsValidationConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pangming on 2017/4/21.
 */
public class FileUtil {

    public static void main(String[] args) {
        Class aClass = matchSon(AbsValidationConfig.class);
        System.out.println(aClass);
    }

    public static Class matchSon(Class<?> aClass) {
        if(aClass == null){
            return null;
        }
        List<File> classFile = new ArrayList<File>();
        System.out.println(FileUtil.class.getResource("/").getPath());
        scan(new File(FileUtil.class.getResource("/").getPath()), classFile);
        for (File file : classFile) {
            Class clazz = getClassByFile(file);
            if(clazz == null){
                continue;
            }
            if(aClass.isAssignableFrom(clazz) && !clazz.equals(aClass)){
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
        String absolutePath = file.getAbsolutePath();
        String classPath = absolutePath.substring(absolutePath.indexOf("classes") + 8);
        classPath = classPath.replace(".class", "");
        Class<?> aClass = null;
        try {
            aClass = Class.forName(classPath.replace("\\", "."));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return aClass;
    }


}
