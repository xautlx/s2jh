package lab.s2jh.core.exception;

/**
 * 业务逻辑校验异常，此类异常不会进行常规的logger.error记录，一般只在前端显示提示用户
 */
public class ValidationException extends BaseRuntimeException {

    public ValidationException(String msg) {
        super(msg);
    }
}
