package chen.shangquan.crpc.server;

import chen.shangquan.crpc.center.CrpcRegisterCenter;
import chen.shangquan.crpc.center.zookeeper.CuratorClient;
import chen.shangquan.crpc.constant.CrpcConstant;
import chen.shangquan.crpc.model.po.ClassInfo;
import chen.shangquan.crpc.model.po.MethodInfo;
import chen.shangquan.crpc.model.po.ParameterInfo;
import chen.shangquan.crpc.model.po.ServerInfo;
import chen.shangquan.crpc.server.map.ServerMap;
import chen.shangquan.utils.object.ObjectResolution;
import chen.shangquan.utils.resource.CrpcConfigUtils;
import chen.shangquan.utils.type.ClassTypeUtils;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static chen.shangquan.crpc.constant.CrpcConstant.DEFAULT_VERSION;
import static org.apache.zookeeper.CreateMode.EPHEMERAL_SEQUENTIAL;

public class ServerRegister {

    public static void registerServerClass(ServerInfo server, List<Class<?>> serverRegisterClasses) throws Exception {
        String path = CrpcConstant.TOP_PATH_SEPARATOR + server.getName() + CrpcConstant.SEPARATOR + server.getName();
        String pathInterface = CrpcConstant.SEPARATOR + CrpcConstant.INTERFACE + CrpcConstant.SEPARATOR + server.getName() + CrpcConstant.SEPARATOR + server.getName();
        List<Class<?>> classList = serverRegisterClasses.stream().filter(e -> !e.isInterface()).collect(Collectors.toList());
        List<Class<?>> interfaceList = serverRegisterClasses.stream().filter(Class::isInterface).collect(Collectors.toList());
        String jsonStr = dealServerListClass(server, classList);
        String jsonInterfaceStr = dealServerListInterface(server, interfaceList);
        boolean exist = CrpcRegisterCenter.checkExists(path);
        if (exist) {
            CrpcRegisterCenter.delete(path);
        }
        CrpcRegisterCenter.createTemporaryOrder(path, jsonStr.getBytes(StandardCharsets.UTF_8));
        boolean statInterface = CrpcRegisterCenter.checkExists(pathInterface);
        if (statInterface) {
            CrpcRegisterCenter.delete(pathInterface);
        }
        if (jsonInterfaceStr != null) {
            CrpcRegisterCenter.createTemporaryOrder(pathInterface, jsonInterfaceStr.getBytes(StandardCharsets.UTF_8));
        }
    }

    private static String dealServerListInterface(ServerInfo server, List<Class<?>> serverRegisterClasses) {
        List<ClassInfo> classItemList = new ArrayList<>();
        for (Class<?> clazz : serverRegisterClasses) {
            // bean 为空的是接口
            classItemList.add(dealServiceInterface(clazz));
        }
        server.setClassList(classItemList);
        return JSONUtil.toJsonStr(server);
    }

    public static String dealServerListClass(ServerInfo server, List<Class<?>> serverRegisterClasses) {
        List<ClassInfo> classItemList = new ArrayList<>();
        for (Class<?> clazz : serverRegisterClasses) {
            // bean 为空的是接口
            classItemList.add(dealServiceClass(clazz));
        }
        server.setClassList(classItemList);
        return JSONUtil.toJsonStr(server);
    }

    private static <T> ClassInfo dealServiceInterface(Class<?> aClass) {
        chen.shangquan.crpc.server.annotation.ServerRegister annotation = aClass.getAnnotation(chen.shangquan.crpc.server.annotation.ServerRegister.class);
        String serverName = null;
        String className = null;
        String version = null;
        if (annotation != null) {
            serverName = annotation.serverName();
            className = annotation.className();
            version = annotation.version();
        } else {
            serverName = "";
            className = aClass.getSimpleName();
            version = DEFAULT_VERSION;
        }
        if (!aClass.isInterface()) {
            Object bean = null;
            try {
                bean = SpringUtil.getBean(aClass.getSimpleName());
            } catch (NoSuchBeanDefinitionException e) {
                bean = SpringUtil.getBean(ClassTypeUtils.setFirstToLowerCase(aClass.getSimpleName()));
            }
            ServerMap.put(className + "-" + version, bean);
        }
        Method[] methods = aClass.getMethods();
        List<MethodInfo> methodDetails = new ArrayList<>();
        for (Method method : methods) {
            if (ClassTypeUtils.isDefaultMethod(method.getName())) {
                continue;
            }
            String name = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            MethodInfo methodDetail = new MethodInfo();
            methodDetail.setName(name);
            List<ParameterInfo> args = new ArrayList<>();
            for (Class<?> parameterType : parameterTypes) {
                String simpleName = parameterType.getSimpleName();
                ParameterInfo methodArgs = new ParameterInfo();
                methodArgs.setName(simpleName);
                if (!ClassTypeUtils.isBaseType(parameterType)) {
                    Field[] fields = parameterType.getDeclaredFields();
                    List<String> fieldNames = new ArrayList<>();
                    for (Field field : fields) {
                        fieldNames.add(field.getName());
                    }
                    methodArgs.setParameterList(fieldNames);
                }
                try {
                    String cson = ObjectResolution.analysisClassDetail(parameterType);
                    String json = ObjectResolution.removeBrackets(cson);
                    methodArgs.setParameterJson(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                args.add(methodArgs);
            }
            methodDetail.setParameterList(args);
            methodDetails.add(methodDetail);
        }
        return new ClassInfo(serverName, className, aClass.getName().replace("." + aClass.getSimpleName(), ""), version, methodDetails);
    }

    private static <T> ClassInfo dealServiceClass(Class<?> aClass) {
        chen.shangquan.crpc.server.annotation.ServerRegister annotation = aClass.getAnnotation(chen.shangquan.crpc.server.annotation.ServerRegister.class);
        String serverName = null;
        String className = null;
        String version = null;
        if (annotation != null) {
            serverName = annotation.serverName();
            className = annotation.className();
            version = annotation.version();
        } else {
            serverName = "";
            className = aClass.getSimpleName();
            version = DEFAULT_VERSION;
        }
        if (!aClass.isInterface()) {
            Object bean = null;
            String simpleName = aClass.getSimpleName();
            try {
                bean = SpringUtil.getBean(simpleName);
            } catch (NoSuchBeanDefinitionException e) {
                simpleName = ClassTypeUtils.setFirstToLowerCase(simpleName);
                bean = SpringUtil.getBean(simpleName);
            }
            ServerMap.put(className + "-" + version, bean);
        }
        Method[] methods = aClass.getMethods();
        List<MethodInfo> methodDetails = new ArrayList<>();
        for (Method method : methods) {
            if (ClassTypeUtils.isDefaultMethod(method.getName())) {
                continue;
            }
            String name = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            MethodInfo methodDetail = new MethodInfo();
            methodDetail.setName(name);
            List<ParameterInfo> args = new ArrayList<>();
            for (Class<?> parameterType : parameterTypes) {
                String simpleName = parameterType.getSimpleName();
                ParameterInfo methodArgs = new ParameterInfo();
                methodArgs.setName(simpleName);
                if (!ClassTypeUtils.isBaseType(parameterType)) {
                    Field[] fields = parameterType.getDeclaredFields();
                    List<String> fieldNames = new ArrayList<>();
                    for (Field field : fields) {
                        fieldNames.add(field.getName());
                    }
                    methodArgs.setParameterList(fieldNames);
                }
                try {
                    String cson = ObjectResolution.analysisClassDetail(parameterType);
                    String json = ObjectResolution.removeBrackets(cson);
                    methodArgs.setParameterJson(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                args.add(methodArgs);
            }
            methodDetail.setParameterList(args);
            methodDetails.add(methodDetail);
        }
        return new ClassInfo(serverName, className, aClass.getName().replace("." + aClass.getSimpleName(), ""), version, methodDetails);
    }
}
