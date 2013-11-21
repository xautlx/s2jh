package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.Component;
import org.apache.struts2.components.TextArea;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 基于KindEditor封装的大文本录入组件标签
 * 用法示例：<s2:kindeditor name="description" label="描述" rows="3"/>
 */
public class S2KindEditorTag extends TextareaTag {

	/**
	 * 配置编辑器的工具栏，其中”/”表示换行，”|”表示分隔符。
	 * http://www.kindsoft.net/docs/option.html#items
	 * 未提供参数取组件默认配置项，内置“simple”表示简单配置项，其余可按照组件文档提供配置项定义
	 */
	protected String items;

	protected String height;

	protected String uploadJson;

	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		try {
			if (StringUtils.isNotBlank(height)) {
				this.setDynamicAttribute(null, "height", height);
			}
			if (StringUtils.isNotBlank(uploadJson)) {
				HttpSession session = req.getSession();
				dynamicAttributes.put("uploadJson", uploadJson + ";JSESSIONID=" + session.getId());
			}
			Long imageUploadMaxSize = (Long) findValue("%{getImageUploadMaxSize()}");
			if (imageUploadMaxSize == null) {
				//5M
				imageUploadMaxSize = 5242880L;
			}
			dynamicAttributes.put("imageSizeLimit", FileUtils.byteCountToDisplaySize(imageUploadMaxSize));
		} catch (JspException e) {
			e.printStackTrace();
		}

		return super.getBean(stack, req, res);
	}

	protected void populateParams() {

		super.populateParams();

		TextArea uiBean = ((TextArea) component);
		uiBean.setTemplate("kindeditor");
		if (id == null) {
			//设置ID随机
			uiBean.setId("kindeditor_" + RandomStringUtils.randomAlphabetic(10));
		}
		if (this.theme == null) {
			uiBean.setTheme("bootstrap");
		}

		if (StringUtils.isNotBlank(items)) {
			if ("simple".equals(items)) {
				items = "['fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',"
						+ "'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',"
						+ "'insertunorderedlist', '|', 'emoticons', 'image', 'link']";
			} else if ("view".equals(items)) {
				items = "['source', 'fullscreen']";
			}
			dynamicAttributes.put("items", items);
		}
	}

	public void setItems(String items) {
		this.items = items;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setUploadJson(String uploadJson) {
		this.uploadJson = uploadJson;
	}
}
