package lab.s2jh.sys.web.action;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.PersistableController;
import lab.s2jh.sys.entity.LoggingEvent;
import lab.s2jh.sys.service.LoggingEventService;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData(title = "日志处理")
public class LoggingEventController extends PersistableController<LoggingEvent,Long> {

    @Autowired
    private LoggingEventService loggingEventService;

    @Override
    protected BaseService<LoggingEvent, Long> getEntityService() {
        return loggingEventService;
    }
    
    @Override
    protected void checkEntityAclPermission(LoggingEvent entity) {
        // TODO Add acl check code logic
    }

    @Override
    @MetaData(title = "更新")
    public HttpHeaders doUpdate() {
        return super.doUpdate();
    }

    @Override
    @MetaData(title = "删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData(title = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }
}