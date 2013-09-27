package lab.s2jh.schedule.web.action;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.schedule.entity.JobRunHist;
import lab.s2jh.schedule.service.JobRunHistService;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData(title = "定时任务运行记录")
public class JobRunHistController extends BaseController<JobRunHist,String> {

    @Autowired
    private JobRunHistService jobRunHistService;

    @Override
    protected BaseService<JobRunHist, String> getEntityService() {
        return jobRunHistService;
    }
    
    @Override
    protected void checkEntityAclPermission(JobRunHist entity) {
        // Nothing to do
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