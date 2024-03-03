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
        Class<?> aClass = bean.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        // 这里不允许方法重名
        Method method = Arrays.stream(declaredMethods).filter(e -> e.getName().equals(request.getMethodName())).findFirst().get();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object data = null;
        if (parameterTypes.length == 0) {
            data = method.invoke(bean);
        } else {
            Object o = null;
            try {
                o = JSONUtil.toBean((String)request.getData(), parameterTypes[0]);
            } catch (JSONException e) {
                String simpleName = parameterTypes[0].getSimpleName();
                o = switch (simpleName) {
                    case "String" -> String.valueOf(request.getData());
                    case "Integer" -> Integer.valueOf((String) request.getData());
                    case "Long" -> Long.valueOf((String) request.getData());
                    case "Double" -> Double.valueOf((String) request.getData());
                    case "Boolean" -> Boolean.valueOf((String) request.getData());
                    default -> o;
                };
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
                    String simpleName = parameterTypes[0].getSimpleName();
                    o = switch (simpleName) {
                        case "String" -> String.valueOf(request.getData());
                        case "Integer" -> Integer.valueOf(String.valueOf(request.getData()));
                        case "Long" -> Long.valueOf(String.valueOf(request.getData()));
                        case "Double" -> Double.valueOf(String.valueOf(request.getData()));
                        case "Boolean" -> Boolean.valueOf(String.valueOf(request.getData()));
                        default -> o;
                    };
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                return RpcResponse.builder().id(request.getId()).code(400).message("参数不合法").build();
            }
            try {
                data = method.invoke(bean, o);
            } catch (Throwable e) {
                throw new RuntimeException();
            }

        }
        return RpcResponse.builder().id(request.getId()).code(200).data(data).message("OK").build();
    }
}
