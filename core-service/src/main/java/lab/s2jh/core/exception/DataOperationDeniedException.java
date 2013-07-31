package lab.s2jh.core.exception;

/**
 * 数据操作无权
 */
public class DataOperationDeniedException extends BaseRuntimeException{

    public DataOperationDeniedException() {
        super("无效数据操作");
    }
    
    public DataOperationDeniedException(String msg) {
        super(msg);
    }

    public DataOperationDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
