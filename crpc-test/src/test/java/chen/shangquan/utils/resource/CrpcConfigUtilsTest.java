package chen.shangquan.utils.resource;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CrpcConfigUtilsTest {

    @Test
    public void testGetConfig() throws IOException {
        System.out.println(CrpcConfigUtils.getCrpcConfig());
    }
}
