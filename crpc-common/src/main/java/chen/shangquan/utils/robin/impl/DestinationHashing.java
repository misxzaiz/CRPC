package chen.shangquan.utils.robin.impl;

import chen.shangquan.utils.robin.Robin;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.CRC32;

/**
 * 目标地址哈希法
 */
public class DestinationHashing implements Robin {
    private List<String> servers;
    private MessageDigest md5;
    private CRC32 crc32;

    public DestinationHashing(List<String> servers) throws NoSuchAlgorithmException {
        this.servers = servers;
        this.md5 = MessageDigest.getInstance("MD5");
        this.crc32 = new CRC32();
    }

    @Override
    public String getServer() {
        return null;
    }

    @Override
    public String getServer(String destinationURL) {
        byte[] urlBytes = ByteBuffer.wrap(destinationURL.getBytes()).order(ByteOrder.BIG_ENDIAN).array();
        byte[] hashBytes = md5.digest(urlBytes);
        crc32.update(hashBytes);
        long hashValue = crc32.getValue();
        int index = (int) (hashValue % servers.size());
        return servers.get(index);
    }
}