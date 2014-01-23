package lab.s2jh.bpm.web.action;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import lab.s2jh.bpm.service.ActivitiService;
import lab.s2jh.core.exception.WebException;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.rest.RestActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.opensymphony.xwork2.ModelDriven;

public class ActivitiController extends RestActionSupport implements ModelDriven<Object> {

    protected static Logger logger = LoggerFactory.getLogger(ActivitiController.class);

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
    protected ActivitiService activitiService;

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
    public void processInstanceImage() {
        HttpServletRequest request = ServletActionContext.getRequest();
        InputStream imageStream = null;
        String bizKey = request.getParameter("bizKey");
        if (StringUtils.isNotBlank(bizKey)) {
            imageStream = activitiService.buildProcessImageByBizKey(bizKey);
        } else {
            String processInstanceId = request.getParameter("processInstanceId");
            imageStream = activitiService.buildProcessImageByProcessInstanceId(processInstanceId);
        }
        Assert.notNull(imageStream);

        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len = -1;
        ServletOutputStream out;
        try {
            out = ServletActionContext.getResponse().getOutputStream();
            while ((len = imageStream.read(b, 0, 1024)) != -1) {
                out.write(b, 0, len);
            }
            imageStream.close();
            out.close();
        } catch (IOException e) {
            logger.error("Output process image error", e);
            throw new WebException("流程运行图处理异常", e);
        }

    }
}
