package chen.shangquan.crpc.exp;

import java.util.List;

public class ServerInvokeException extends RuntimeException {
    private List<String> errorMessages;

    // 构造方法，接受一个错误信息的列表
    public ServerInvokeException(List<String> errorMessages) {
        super("Server invocation failed with the following errors:");
        this.errorMessages = errorMessages;
    }

    // 构造方法，接受一个错误信息的列表和详细的错误信息
    public ServerInvokeException(List<String> errorMessages, String message) {
        super(message);
        this.errorMessages = errorMessages;
    }

    // 构造方法，接受一个错误信息的列表、详细的错误信息和原因
    public ServerInvokeException(List<String> errorMessages, String message, Throwable cause) {
        super(message, cause);
        this.errorMessages = errorMessages;
    }

    // 获取错误信息的列表
    public List<String> getErrorMessages() {
        return errorMessages;
    }

    // 覆盖toString方法，以便在打印异常时输出更详细的错误信息
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("\nError messages: ");
        for (String message : errorMessages) {
            sb.append(message).append("; ");
        }
        return sb.toString();
    }
}
