package chen.shangquan.utils.object;

import chen.shangquan.utils.type.ClassTypeUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectResolution {
    private static final String INDENTATION = "\t";
    private static String PREFIX = "";
    private static final String RIGHT_BRACKET = "】";
    private static final String LEFT_BRACKET = "【";
    private static final String MARKER = "\"";
    private static final String COLON = ":";
    private static final String OPEN_BRACE = "{";
    private static final String CLOSE_BRACE = "}";
    private static final char NEW_LINE = '\n';
    private static final String COMMA = ",";
    private static final String OPEN_ANGLE_BRACKET = "<";
    private static final String CLOSE_ANGLE_BRACKET = ">";
    private static final String OPEN_SQUARE_BRACKET = "[";
    private static final String CLOSE_SQUARE_BRACKET = "]";

    private static final LinkedList<String> BRACKETS_STACK = new LinkedList<>();

    private static boolean lastIsNewline(StringBuilder sb) {
        return sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n';
    }

    private static StringBuilder analysisClassDetail(Class<?> tClass, StringBuilder cson) throws ClassNotFoundException {
        // 基本类型 & 引用类型
        if (ClassTypeUtils.isBaseType(tClass) && !lastIsNewline(cson)) {
            cson.append(LEFT_BRACKET).append(tClass.getSimpleName()).append(RIGHT_BRACKET).append(COLON).append(MARKER).append("0").append(MARKER).append(COMMA).append(NEW_LINE);
        } else {
            // 如果没有换行，就补充类型
            if (!lastIsNewline(cson)) {
                cson.append(LEFT_BRACKET).append(tClass.getSimpleName()).append(RIGHT_BRACKET).append(COLON).append(OPEN_BRACE).append(NEW_LINE);
                BRACKETS_STACK.push(CLOSE_BRACE);
                PREFIX += INDENTATION;
            }
            // 解析引用类型的属性
            Field[] fields = tClass.getDeclaredFields();
            for (Field field : fields) {
                Class<?> fieldClass = field.getType();
                cson.append(PREFIX).append(MARKER).append(field.getName()).append(MARKER).append(COLON);
                // 列表
                if (ClassTypeUtils.isCollectionClass(fieldClass)) {
                    String typeClassName = field.getGenericType().getTypeName().split("<")[1].split(">")[0];
                    try {
                        Class<?> typeClass = Class.forName(typeClassName);
                        cson.append(LEFT_BRACKET).append(fieldClass.getSimpleName()).append(OPEN_ANGLE_BRACKET).append(typeClass.getSimpleName()).append(CLOSE_ANGLE_BRACKET).append(RIGHT_BRACKET).append(COLON).append(OPEN_SQUARE_BRACKET).append(NEW_LINE);
                        BRACKETS_STACK.push(CLOSE_SQUARE_BRACKET);
                        if (!ClassTypeUtils.isBaseType(typeClass)) {
                            PREFIX += INDENTATION;
                            cson.append(PREFIX).append(OPEN_BRACE).append(NEW_LINE);
                            BRACKETS_STACK.push(CLOSE_BRACE);
                        }
                        PREFIX += INDENTATION;
                        if (ClassTypeUtils.isBaseType(typeClass)) {
                            cson = new StringBuilder(cson.substring(0, cson.length() - 1));
                            cson
                                    .append(MARKER).append(MARKER) // TODO 根据类型赋默认值
                                    .append(BRACKETS_STACK.pop()).append(COMMA).append(NEW_LINE);
                            PREFIX = PREFIX.substring(0, PREFIX.length() - 1);
                            continue;
                        }
                        if (ClassTypeUtils.isObjectType(typeClass)) {
                            cson = new StringBuilder(cson.substring(0, cson.length() - 1));
                            cson.append(BRACKETS_STACK.pop()).append(COMMA).append(NEW_LINE);
                            PREFIX = PREFIX.substring(0, PREFIX.length() - 1);
                            continue;
                        }
                        if (tClass.getSimpleName().equals(typeClass.getSimpleName())) {
                            cson = new StringBuilder(cson.substring(0, cson.length() - 1));
                            cson.append("递归").append(BRACKETS_STACK.pop()).append(COMMA).append(NEW_LINE);
                            if (PREFIX.length() >= 1) {
                                PREFIX = PREFIX.substring(0, PREFIX.length() - 1);
                            }

                            if (BRACKETS_STACK.size() > 0) {
                                String pop = BRACKETS_STACK.pop();
                                selfUserNewLine(cson,pop);
                            }
                            continue;
                        }
                        cson = analysisClassDetail(typeClass, cson);
                        continue;
                    } catch (ClassNotFoundException e) {
                        // TODO
                        continue;
                    }
                }
                // 自引用
                if (tClass.getSimpleName().equals(fieldClass.getSimpleName())) {
                    cson.append(LEFT_BRACKET).append(fieldClass.getSimpleName()).append(RIGHT_BRACKET).append(OPEN_BRACE).append("递归").append(CLOSE_BRACE).append(COMMA).append(NEW_LINE);
                    if (BRACKETS_STACK.size() > 0) {
                        String pop = BRACKETS_STACK.pop();
                        selfUserNewLine(cson,pop);
                    }
                    continue;
                }
                // Object
                if (ClassTypeUtils.isObjectType(fieldClass)) {
                    cson = new StringBuilder(cson.substring(0, cson.length() - 1));
                    cson.append(COLON).append(LEFT_BRACKET).append(fieldClass.getSimpleName()).append(RIGHT_BRACKET).append(COLON).append(OPEN_BRACE).append(CLOSE_BRACE).append(COMMA).append(NEW_LINE);
                    continue;
                }
                // 其他类型
                cson = analysisClassDetail(fieldClass, cson);
            }
            if (PREFIX.length() >= 1) {
                PREFIX = PREFIX.substring(0, PREFIX.length() - 1);
            }
            try {
                cson.append(PREFIX).append(BRACKETS_STACK.pop()).append(COMMA).append(NEW_LINE);
            } catch (NoSuchElementException e) {
                cson.append(PREFIX).append("【pop is null】").append(COMMA).append(NEW_LINE);
            }
        }
        return cson;
    }

    private static void selfUserNewLine(StringBuilder cson,String pop) {
        if ("]".equals(pop)) {
            PREFIX = PREFIX.substring(0, PREFIX.length() - 1);
            cson.append(PREFIX).append(pop).append(COMMA).append(NEW_LINE);
            pop = BRACKETS_STACK.pop();
            selfUserNewLine(cson,pop);
        } else {
            if (BRACKETS_STACK.size() > 0) {
                String pop1 = BRACKETS_STACK.pop();
                if ("]".equals(pop1)) {
                    PREFIX = PREFIX.substring(0, PREFIX.length() - 1);
                    cson.append(PREFIX).append(pop).append(COMMA).append(NEW_LINE);
                    BRACKETS_STACK.push(pop1);
                } else {
                    BRACKETS_STACK.push(pop1);
                    BRACKETS_STACK.push(pop);
                }
            } else {
                BRACKETS_STACK.push(pop);
            }

        }
    }

    public static String analysisClassDetail(Class<?> tClass) throws ClassNotFoundException {
        StringBuilder cson = new StringBuilder();
        cson = new StringBuilder(analysisClassDetail(tClass, cson));
        if (BRACKETS_STACK.size() > 0) {
            int size = BRACKETS_STACK.size();
            for (int i = 0; i < size; i++) {
                PREFIX = PREFIX.length() > 0 ? PREFIX.substring(0, PREFIX.length() - 1) : "";
                cson.append(PREFIX).append(BRACKETS_STACK.pop()).append(NEW_LINE);
            }
        }

        return removeLastComma(cson.toString());
    }

    private static String removeLastComma(String str) {
        if (str != null && str.length() > 0) {
            int lastIndex = str.lastIndexOf(",");
            if (lastIndex >= 0) {
                return str.substring(0, lastIndex) + str.substring(lastIndex + 1);
            }
        }
        return str;
    }

    /**
     * 转为JSON
     */
    public static String removeBrackets(String cson) {
        String regex = "【[^】]+】:";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cson);
        String result = matcher.replaceAll("");

        String regex1 = "【[^】]+】";
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher1 = pattern1.matcher(result);
        String result1 = matcher1.replaceAll("");
        String replace = result1.replace("递归", "");
        // 删除最后一个换行符
        int lastIndex = replace.lastIndexOf("\n");
        if (lastIndex != -1) {
            replace = replace.substring(0, lastIndex) + replace.substring(lastIndex + 1);
        }

        if (!(replace.contains("{") || replace.contains("["))) {
            return "0";
        }
        return replace;
    }

    public static String getObjectResolution(Class<?> tClass) {
        try {
            String cson = analysisClassDetail(tClass);
            return removeBrackets(cson);
        } catch (ClassNotFoundException e) {
            return "";
        }
    }
}
