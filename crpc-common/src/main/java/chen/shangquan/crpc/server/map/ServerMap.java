package chen.shangquan.crpc.server.map;

import java.util.HashMap;
import java.util.Map;

public class ServerMap {
    private static final Map<String, Object> SERVER_MAP = new HashMap<>();

    public static void put(String name, Object bean) {
        SERVER_MAP.put(name, bean);
    }

    public static Object get(String name, String version) {
        return SERVER_MAP.get(name + "-" + version);
    }
}
