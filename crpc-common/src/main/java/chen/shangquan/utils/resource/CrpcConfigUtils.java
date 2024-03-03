package chen.shangquan.utils.resource;

import chen.shangquan.crpc.model.po.ServerInfo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author chenshangquan
 * @date 3/2/2024
 */
public class CrpcConfigUtils {
    public static Map<String, Object> getCrpcConfig() throws IOException {
        // 加载classpath下的YAML文件
        try (InputStream inputStream = new ClassPathResource("application.yml").getInputStream()) {
            // 读取文件内容
            String fileContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            // 解析YAML文件
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(fileContent);

            // 安全地获取并转换"CRPC"配置
            @SuppressWarnings("unchecked")
            Map<String, Object> crpc = (Map<String, Object>) data.get("crpc");
            if (crpc == null) {
                throw new IOException("在YML文件中找不到CRPC配置。");
            }

            return crpc;
        } catch (IOException e) {
            throw new IOException("加载或解析应用程序配置失败", e);
        }
    }

    public static ServerInfo getServerInfo() throws IOException {
        Map<String, Object> crpc = getCrpcConfig();
        ServerInfo server = new ServerInfo();
        server.setName((String) crpc.get("name"));
        server.setIp((String) crpc.get("ip"));
        Integer port = (Integer) crpc.get("port");
        server.setPort(port);
        server.setArea((String) crpc.get("area"));
        Integer weight = (Integer) crpc.get("weight");
        server.setWeight(weight);
        return server;
    }
}
