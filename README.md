## 基本架构

1. crpc-client: 客户端
2. crpc-server: 服务端
3. crpc-common: 公共模块

## crpc-common：公共模块

### 一、ObjectResolution 对象解析器

> 主要时通过反射将对象解析为 json 格式，在 CRPC 客户端服务测试时使用

方法：
```java
String json = ObjectResolution.getObjectResolution(Class<?> tClass);
```
实例
```java
@Data
public class P {
    private String name;
    private int age;
    private P father;
    private List<P> friend;
    private F f;
    private List<F> flist;
}

@Data
public class F {
    private String name;
    private int age;
}
```
解析结果
```json
{
  "name":"0",
  "age":"0",
  "father":{},
  "friend":[
    {},
  ],
  "f":{
    "name":"0",
    "age":"0",
  },
  "flist":[
    {
      "name":"0",
      "age":"0",
    },
  ]
}

```

问题

1. 两个对象相互调用时，会导致无限递归，需要处理  
> 思路1：使用全路径记录对象是否已经使用过，但是这样会导致解析结果变少

### 二、CrpcConfigUtils Application.yml 解析

> 解析 application.yml 获取 CRPC 配置信息

### 三、UniqueIdGenerator 唯一 ID 生成器

> 最多支持 32 个节点 （0 - 31）
> 每个节点每毫秒最多生成 4096 个 ID

方法  

|               方法                |          说明           |  
|:-------------------------------:|:---------------------:|  
| void setWorkerId(long workerId) |      设置机器码（0-31）      |
|  String generateUniqueIdLong()  |  生成 Long 类型 10 进制 ID  |
|  String generateUniqueIdHex()   | 生成 String 类型 16 进制 ID |
| String generateUniqueIdBinary() | 生成 String 类型 2 进制 ID  |
|    String generateUniqueId()    | 生成 String 类型 10 进制 ID |