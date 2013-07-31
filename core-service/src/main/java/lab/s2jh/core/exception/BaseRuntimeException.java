package lab.s2jh.core.exception;

import org.springframework.core.NestedRuntimeException;

public abstract class BaseRuntimeException extends NestedRuntimeException{

    public BaseRuntimeException(String msg) {
        super(msg);
    }
    
    public BaseRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
