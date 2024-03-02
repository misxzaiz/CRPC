package chen.shangquan.utils.type;

import java.util.*;

public class ClassTypeUtils {
    /**
     * 检查是不是基本类型
     */
    public static boolean isBaseType(Class<?> clazz) {
        return clazz.isPrimitive()
                || clazz == String.class
                || clazz == Character.class
                || clazz == Byte.class
                || clazz == Short.class
                || clazz == Integer.class
                || clazz == Long.class
                || clazz == Float.class
                || clazz == Double.class
                || clazz == Boolean.class
                || clazz == Void.class;
    }

    public static boolean isObjectType(Class<?> clazz) {
        return "Object".equals(clazz.getSimpleName());
    }

    public static boolean isVoidType(Class<?> clazz) {
        return clazz.isPrimitive() && clazz == Void.class;
    }

    public static boolean isCollectionClass(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isMapClass(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    public static final Set<String> IGNORED_METHODS = new HashSet<>(Arrays.asList("wait", "toString", "hashCode", "equals", "getClass", "notify", "notifyAll"));

    public static boolean isDefaultMethod(String methodName) {
        return IGNORED_METHODS.contains(methodName);
    }

    public static String setFirstToUpperCase(String tmp) {
        return String.valueOf(tmp.charAt(0)).toUpperCase() + tmp.substring(1);
    }

    public static String setFirstToLowerCase(String tmp) {
        return String.valueOf(tmp.charAt(0)).toLowerCase() + tmp.substring(1);
    }
}
