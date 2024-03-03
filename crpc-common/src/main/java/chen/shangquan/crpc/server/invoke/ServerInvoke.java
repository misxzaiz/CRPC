package chen.shangquan.crpc.server.invoke;

import chen.shangquan.crpc.network.data.RpcRequest;
import chen.shangquan.crpc.network.data.RpcResponse;
import chen.shangquan.crpc.server.map.ServerMap;
import chen.shangquan.utils.type.ClassTypeUtils;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ServerInvoke {
    public static RpcResponse invoke(RpcRequest request) throws InvocationTargetException, IllegalAccessException {
        Object bean = ServerMap.get(request.getClassName(), request.getVersion());
        Method[] declaredMethods = bean.getClass().getDeclaredMethods();
        Method method = Arrays.stream(declaredMethods).filter(e -> e.getName().equals(request.getMethodName())).findFirst().get();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object data = null;
        // 没有参数
        if (parameterTypes.length == 0) {
            data = method.invoke(bean);
        } else {
            Object o = null;
            try {
                // 参数为 object
                o = JSONUtil.toBean((String)request.getData(), parameterTypes[0]);
            } catch (JSONException e) {
                // 参数为基本数据类型
                o = getObject(request.getData(), o, parameterTypes[0].getSimpleName());
            } catch (ClassCastException e) {
                try {
                    JSONObject jsonObject = (JSONObject) request.getData();
                    String serverName = (String)jsonObject.get("serverName");
                    if (serverName != null) {
                        o = JSONUtil.toBean(jsonObject.toString(), RpcRequest.class);
                    } else {
                        if (ClassTypeUtils.isBaseType(parameterTypes[0])) {
                            o = jsonObject.toString();
                        } else {
                            o = JSONUtil.toBean(jsonObject.toString(), parameterTypes[0]);
                        }
                    }
                } catch (ClassCastException e1) {
                    o = getObject(request.getData(), o, parameterTypes[0].getSimpleName());
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                return RpcResponse.builder().id(request.getId()).code(400).message("参数不合法").build();
            }
            try {
                data = method.invoke(bean, o);
            } catch (Throwable e) {
                return RpcResponse.builder().id(request.getId()).code(400).message("服务执行异常").build();
            }
        }
        return RpcResponse.builder().id(request.getId()).code(200).data(data).message("OK").build();
    }

    private static Object getObject(Object data, Object o, String simpleName) {
        o = switch (simpleName) {
            case "String" -> String.valueOf(data);
            case "Integer" -> Integer.valueOf((String) data);
            case "Long" -> Long.valueOf((String) data);
            case "Double" -> Double.valueOf((String) data);
            case "Boolean" -> Boolean.valueOf((String) data);
            default -> o;
        };
        return o;
    }
}
