package org.apache.struts2.rest;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.audit.envers.ExtRevisionListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import com.opensymphony.xwork2.config.ConfigurationManager;

/**
 * 扩展标准的REST解析处理逻辑
 * 1. 添加对;jsessionid=的清洗处理
 * 2. 将Action方法名作为Hibernate Envers操作审计事件名写入线程对象数据对象
 */
public class ExtRestActionMapper extends RestActionMapper {

    @Override
    protected void parseNameAndNamespace(String uri, ActionMapping mapping, ConfigurationManager configManager) {
        super.parseNameAndNamespace(uri, mapping, configManager);
        String name = mapping.getName();
        // cut off any ;jsessionid= type appendix but allow the rails-like ;edit
        int scPos = name.indexOf(';');
        if (scPos > -1 && !"edit".equals(name.substring(scPos + 1))) {
            name = name.substring(0, scPos);
            mapping.setName(name);
        }

    }

    @Override
    public ActionMapping getMapping(HttpServletRequest request, ConfigurationManager configManager) {
        ActionMapping actionMapping = super.getMapping(request, configManager);
        //将Action方法名作为Hibernate Envers操作审计事件名写入线程对象数据对象
        //TODO: 可以考虑进一步优化取方法对应的metadata中文信息可以使审计信息显示更友好
        if (actionMapping != null) {
            String path = actionMapping.getNamespace() + "/" + actionMapping.getName();
            if (StringUtils.isNotBlank(actionMapping.getMethod())) {
                path += ("!" + actionMapping.getMethod());
            }
            ExtRevisionListener.setOperationEvent(path);
        }
        return actionMapping;
    }
}
