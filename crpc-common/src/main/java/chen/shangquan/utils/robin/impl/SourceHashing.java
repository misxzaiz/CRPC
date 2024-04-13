package chen.shangquan.utils.robin.impl;

import chen.shangquan.utils.robin.Robin;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.CRC32;

/**
 * 源地址哈希法
 */
public class SourceHashing implements Robin {
    private List<String> servers;
    private MessageDigest md5;
    private CRC32 crc32;

    public SourceHashing(List<String> servers) throws NoSuchAlgorithmException {
        this.servers = servers;
        this.md5 = MessageDigest.getInstance("MD5");
        this.crc32 = new CRC32();
    }

    @Override
    public String getServer() {
        return null;
    }

    @Override
    public String getServer(String sourceIP) {
        byte[] ipBytes = ByteBuffer.wrap(sourceIP.getBytes()).order(ByteOrder.BIG_ENDIAN).array();
        byte[] hashBytes = md5.digest(ipBytes);
        crc32.update(hashBytes);
        long hashValue = crc32.getValue();
        int index = (int) (hashValue % servers.size());
        return servers.get(index);
    }
}