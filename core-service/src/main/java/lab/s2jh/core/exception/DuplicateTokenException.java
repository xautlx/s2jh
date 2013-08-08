package lab.s2jh.core.exception;

public class DuplicateTokenException extends BaseRuntimeException{

    public DuplicateTokenException(String msg) {
        super(msg);
    }

    public DuplicateTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
