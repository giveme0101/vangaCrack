package com.crake.june.vangacrake.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassUtil {

    public static void logMethod(Class<?> clazz){
        try {
            MLogger.log("\t" + clazz.getName());

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                StringBuilder sb = new StringBuilder();
                Class<?>[] getTypeParameters = method.getParameterTypes();
                for (Class<?> paraType : getTypeParameters) {
                    sb.append(paraType.getName()).append(", ");
                }
                if (0 != sb.length()){
                    sb.setLength(sb.length() - 2);
                    MLogger.log(String.format("\t\t%s %s (%s)", method.getReturnType().getName(), method.getName(), sb.toString()));
                } else {
                    MLogger.log(String.format("\t\t%s %s ()", method.getReturnType(), method.getName()));
                }
            }
        } catch (Exception ex){
            MLogger.log(ex.toString());
        }
    }

    public static void printAllFileds(Class clazz) {
        MLogger.log("\t" + clazz.getName());
        Field[] field = clazz.getDeclaredFields();
        MLogger.log("getFields():获取所有权限修饰符修饰的字段");
        for (Field f : field) {
            MLogger.log("Field K: " + f.getName()+"\tV:"+ f.toGenericString());
        }
    }
}
