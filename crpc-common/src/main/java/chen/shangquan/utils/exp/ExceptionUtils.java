package chen.shangquan.utils.exp;

import java.util.ArrayList;
import java.util.List;

public class ExceptionUtils {
    public static List<String> getExceptionMessage(Throwable t) {
        List<String> strings = new ArrayList<>();
        strings.add("Message: " + t.getMessage());
        strings.add("to: " + t.toString());
        strings.add("");
        strings.add("stackTrace:");
        for (StackTraceElement stackTraceElement : t.getStackTrace()) {
            strings.add(stackTraceElement.toString());
        }
        return strings;
    }
}
