package chen.shangquan.crpc.network.thread;

import chen.shangquan.crpc.network.data.RpcRequest;

public class RpcRequestLocalThread {
    private static final ThreadLocal<RpcRequest> tl = new ThreadLocal<>();

    public static void saveRpcRequest(RpcRequest request){
        tl.set(request);
    }

    public static RpcRequest getRpcRequest(){
        return tl.get();
    }

    public static void removeRpcRequest(){
        tl.remove();
    }
}
