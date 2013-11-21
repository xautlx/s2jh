package org.apache.struts2.views.jsp.ui;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.UIBean;

/**
 * 扩展标准DIV，用法示例：<s2:div stretch="true">
 */
public class S2DivTag extends DivTag {

	/**
	 * DIV高度设定
	 */
	protected String height;

	/**
	 * stretch=true, 自动延伸占满当前窗口固定区域剩余高度
	 */
	protected Boolean stretch = Boolean.FALSE;

	protected void populateParams() {
		try {
			this.setDynamicAttribute(null, "stretch", stretch);
			if (StringUtils.isNotBlank(height)) {
				this.setDynamicAttribute(null, "height", height);
			}
		} catch (JspException e) {
			e.printStackTrace();
		}

		super.populateParams();
		UIBean uiBean = ((UIBean) component);

		if (id == null) {
			uiBean.setId("div_" + RandomStringUtils.randomAlphabetic(10));
		}
		if (this.theme == null) {
			uiBean.setTheme("bootstrap");
		}
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setStretch(Boolean stretch) {
		this.stretch = stretch;
	}
}
