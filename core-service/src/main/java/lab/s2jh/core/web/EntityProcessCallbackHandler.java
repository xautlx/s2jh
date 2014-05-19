package lab.s2jh.core.web;

/**
 * 用于批量数据单一实体处理逻辑匿名回调接口
 * @param <T>
 */
public interface EntityProcessCallbackHandler<T> {
    void processEntity(T entity);
}
