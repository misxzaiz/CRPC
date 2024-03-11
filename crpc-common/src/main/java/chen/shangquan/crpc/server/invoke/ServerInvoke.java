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
    /**
     * 失败重试次数
     */
    private static final int MAX_RETRIES = 3;
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
                if (request.getData() instanceof  JSONObject) {
                    o = JSONUtil.toBean(((JSONObject)request.getData()).toString(), parameterTypes[0]);
                } else {
                    o = JSONUtil.toBean((String)request.getData(), parameterTypes[0]);
                }
            } catch (JSONException e) {
                // 参数为基本数据类型
                o = getObject(request.getData(), o, parameterTypes[0].getSimpleName());
            } catch (ClassCastException e) {
                e.printStackTrace();
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
            // 失败重试机制
            return invokeWithRetry(method, bean, o, request.getId());
        }
        return RpcResponse.builder().id(request.getId()).code(200).data(data).message("OK").build();
    }


    /**
     * 使用策略模式实现失败重试
     */
    public static RpcResponse invokeWithRetry(Method method, Object bean, Object args, String requestId) {
        for (int retry = 0; retry < MAX_RETRIES; retry++) {
            try {
                Object data = method.invoke(bean, args);
                // 如果执行成功，返回结果
                return RpcResponse.builder().id(requestId).code(200).data(data).build();
            } catch (Throwable e) {
                e.printStackTrace();
                // 如果是最后一次重试，返回失败响应
                if (retry == MAX_RETRIES - 1) {
                    throw new RuntimeException(e.getCause().toString());
//                    return RpcResponse.builder().id(requestId).code(400).message(e.getMessage()).build();
                }
            }
        }
        // 此处理论上不会执行到
        return null;
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
