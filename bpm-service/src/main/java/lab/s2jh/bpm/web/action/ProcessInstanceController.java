package lab.s2jh.bpm.web.action;

import java.util.List;
import java.util.Map;

import lab.s2jh.bpm.service.ActivitiService;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.web.SimpleController;
import lab.s2jh.core.web.view.OperationResult;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ProcessInstanceController extends SimpleController {

    protected static Logger logger = LoggerFactory.getLogger(ProcessInstanceController.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FormService formService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    protected ActivitiService activitiService;

    public HttpHeaders findByPageRunning() {
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(getRequest());
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        String searchBusinessKey = getParameter("businessKey");
        if (StringUtils.isNotBlank(searchBusinessKey)) {
            processInstanceQuery.processInstanceBusinessKey(searchBusinessKey);
        }
        List<ProcessInstance> processInstances = processInstanceQuery.listPage(pageable.getOffset(),
                pageable.getPageSize());
        List<Map<String, Object>> datas = Lists.newArrayList();
        for (ProcessInstance processInstance : processInstances) {
            ExecutionEntity executionEntity = (ExecutionEntity) processInstance;
            Map<String, Object> data = Maps.newHashMap();
            String businessKey = executionEntity.getBusinessKey();
            ProcessDefinition pd = repositoryService.getProcessDefinition(executionEntity.getProcessDefinitionId());
            data.put("id", executionEntity.getId());
            data.put("businessKey", businessKey);
            data.put("processDefinitionName", pd.getName());
            data.put("activityNames", activitiService.findActiveTaskNames(businessKey));
            datas.add(data);
        }
        setModel(new PageImpl(datas, pageable, processInstanceQuery.count()));
        return buildDefaultHttpHeaders();
    }

    public HttpHeaders forceTerminal() {
        String[] ids = getParameterIds();
        for (String id : ids) {
            String msg = "Terminal processInstance[" + id + "]  by user " + AuthContextHolder.getAuthUserPin();
            logger.debug(msg);
            runtimeService.deleteProcessInstance(id, msg);
        }
        setModel(OperationResult.buildSuccessResult("强制结束流程实例操作完成"));
        return buildDefaultHttpHeaders();
    }
}
