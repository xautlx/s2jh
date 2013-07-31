package lab.s2jh.core.exception;

/**
 * 数据访问权限不足
 */
public class DataAccessDeniedException extends BaseRuntimeException{


    public DataAccessDeniedException() {
        super("无权数据访问");
    }
    
    public DataAccessDeniedException(String msg) {
        super(msg);
    }

    public DataAccessDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
