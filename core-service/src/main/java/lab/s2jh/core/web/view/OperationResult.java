/**
 * Copyright (c) 2012
 */
package lab.s2jh.core.web.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 用于Object到JSON序列化的对象结构体定义
 */
@JsonInclude(Include.NON_NULL)
public class OperationResult {

    /** 标识操作成功与否 */
    public enum OPERATION_RESULT_TYPE {
        success, failure, error;
    }

    /** 返回success或failure操作标识 */
    private String type;
    /** 国际化处理的返回JSON消息正文 */
    private String message;
    /** 补充的数据 */
    private Object userdata;

    public static OperationResult buildSuccessResult(String message, Object entity) {
        return new OperationResult(OPERATION_RESULT_TYPE.success, message, entity);
    }

    public static OperationResult buildSuccessResult(String message) {
        return new OperationResult(OPERATION_RESULT_TYPE.success, message);
    }
    
    public static OperationResult buildFailureResult(String message) {
        return new OperationResult(OPERATION_RESULT_TYPE.failure, message);
    }

    public OperationResult(OPERATION_RESULT_TYPE type, String message) {
        this.type = type.name();
        this.message = message;
    }

    public OperationResult(OPERATION_RESULT_TYPE type, String message, Object userdata) {
        this.type = type.name();
        this.message = message;
        this.userdata = userdata;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getUserdata() {
        return userdata;
    }

    public void setUserdata(Object userdata) {
        this.userdata = userdata;
    }
}
