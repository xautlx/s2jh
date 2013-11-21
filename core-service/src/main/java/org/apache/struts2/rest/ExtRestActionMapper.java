package org.apache.struts2.rest;

import org.apache.struts2.dispatcher.mapper.ActionMapping;

import com.opensymphony.xwork2.config.ConfigurationManager;

/**
 * 扩展标准的REST解析处理逻辑：添加对;jsessionid=的清洗处理
 */
public class ExtRestActionMapper extends RestActionMapper{
	protected void parseNameAndNamespace(String uri, ActionMapping mapping,
            ConfigurationManager configManager) {
		super.parseNameAndNamespace(uri, mapping, configManager);
		String name=mapping.getName();
		// cut off any ;jsessionid= type appendix but allow the rails-like ;edit
        int scPos = name.indexOf(';');
        if (scPos > -1 && !"edit".equals(name.substring(scPos + 1))) {
        	name = name.substring(0, scPos);
        	mapping.setName(name);
        }
	}
}
