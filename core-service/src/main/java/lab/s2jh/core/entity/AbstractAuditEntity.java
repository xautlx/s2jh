package lab.s2jh.core.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@MappedSuperclass
@JsonInclude(Include.NON_EMPTY)
public abstract class AbstractAuditEntity<ID extends Serializable> extends BaseEntity<ID> {

    /**
     * 转换操作状态数据值为字面显示字符串
     * @return
     */
    public abstract String convertStateToDisplay(String rawState);
    /**
     * 转换操作事件数据值为字面显示字符串
     * @return
     */
    public abstract String convertEventToDisplay(String rawEvent);
}
