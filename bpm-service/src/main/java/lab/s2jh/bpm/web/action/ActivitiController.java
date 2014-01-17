package lab.s2jh.bpm.web.action;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.bpm.service.WorkflowTraceService;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.rest.RestActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

public class ActivitiController extends RestActionSupport implements ModelDriven<Object> {

    private Object model;

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
    protected WorkflowTraceService traceService;

    @Autowired
    ProcessEngineFactoryBean processEngine;

    @Override
    public Object getModel() {
        return model;
    }

    public HttpHeaders showProcessImage() {
        return new DefaultHttpHeaders("process-image").disableCaching();
    }

    /**
     * 流程运行图
     * @return
     * @throws Exception 
     */
    public void processInstanceImage() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        String processInstanceId = request.getParameter("processInstanceId");
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
        // 使用spring注入引擎请使用下面的这行代码
        Context.setProcessEngineConfiguration(processEngine.getProcessEngineConfiguration());

        InputStream imageStream = ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);

        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            ServletActionContext.getResponse().getOutputStream().write(b, 0, len);
        }
    }
}
