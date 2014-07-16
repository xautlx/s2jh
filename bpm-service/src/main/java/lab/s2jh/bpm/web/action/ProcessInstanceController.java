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
import org.activiti.engine.repository.ProcessDefinitionQuery;
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

    public Map<String, String> getProcessDefinitions() {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.list();
        Map<String, String> datas = Maps.newHashMap();
        for (ProcessDefinition processDefinition : processDefinitions) {
            datas.put(processDefinition.getKey(), processDefinition.getName());
        }
        return datas;
    }

    public HttpHeaders findByPageRunning() {
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(getRequest());
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        String searchBusinessKey = getParameter("businessKey");
        if (StringUtils.isNotBlank(searchBusinessKey)) {
            processInstanceQuery.processInstanceBusinessKey(searchBusinessKey);
        }
        String processDefinitionKey = getParameter("processDefinitionKey");
        if (StringUtils.isNotBlank(processDefinitionKey)) {
            processInstanceQuery.processDefinitionKey(processDefinitionKey);
        }
        List<ProcessInstance> processInstances = processInstanceQuery.orderByProcessInstanceId().asc()
                .listPage(pageable.getOffset(), pageable.getPageSize());
        List<Map<String, Object>> datas = Lists.newArrayList();
        for (ProcessInstance processInstance : processInstances) {
            ExecutionEntity executionEntity = (ExecutionEntity) processInstance;
            Map<String, Object> data = Maps.newHashMap();
            String businessKey = executionEntity.getBusinessKey();
            ProcessDefinition pd = repositoryService.getProcessDefinition(executionEntity.getProcessDefinitionId());
            data.put("id", executionEntity.getId());
            data.put("executionEntityId", executionEntity.getId());
            data.put("businessKey", businessKey);
            data.put("processDefinitionName", pd.getName());
            data.put("activityNames", activitiService.findActiveTaskNames(businessKey));
            datas.add(data);
        }
        setModel(new PageImpl(datas, pageable, processInstanceQuery.count()));
        return buildDefaultHttpHeaders();
    }

    public HttpHeaders forceTerminal() {
        //删除失败的id和对应消息以Map结构返回，可用于前端批量显示错误提示和计算表格组件更新删除行项
        Map<String, String> errorMessageMap = Maps.newLinkedHashMap();

        String[] ids = getParameterIds();
        for (String id : ids) {
            String msg = "Terminal processInstance[" + id + "]  by user " + AuthContextHolder.getAuthUserPin();
            logger.debug(msg);
            activitiService.deleteProcessInstanceByProcessInstanceId(id, msg);
        }
        int rejectSize = errorMessageMap.size();
        if (rejectSize == 0) {
            setModel(OperationResult.buildSuccessResult("强制结束流程实例选取记录:" + ids.length + "条"));
        } else {
            if (rejectSize == ids.length) {
                setModel(OperationResult.buildFailureResult("强制结束流程实例操作失败", errorMessageMap));
            } else {
                setModel(OperationResult.buildWarningResult("强制结束流程实例操作已处理. 成功:" + (ids.length - rejectSize) + "条"
                        + ",失败:" + rejectSize + "条", errorMessageMap));
            }
        }
        return buildDefaultHttpHeaders();
    }
}
