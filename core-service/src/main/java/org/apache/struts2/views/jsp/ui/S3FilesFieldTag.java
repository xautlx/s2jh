package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.components.Component;
import org.apache.struts2.components.Files;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 关联附件上传组件
 * 示例：<s3:files name="search['AD_createdDate']" format="date"/>
 */
public class S3FilesFieldTag extends AbstractClosingTag {

    /** 
     * 请求附件列表的URL除id以外的前面部分，标签自动在此前缀基础上添加id=listUrlId参数
     */
    protected String listUrlPrefix;

    /**
     * 请求附件列表的URL的id参数值，如果（新增界面）为空则不做ajax请求，否则ajax请求附件列表数据
     */
    protected String listUrlId;

    /**
     * 附件分类属性，绝大部分情况一般默认一个实体关联一系列附件默认即可;
     * 如果需要区分不同分类附件则定义不同分类属性值即可
     */
    protected String category = "default";

    /**
     * 只读设置只显示附件列表，不出现添加或删除附件按钮，一般用于查看界面列表显示
     */
    protected String readonly = "false";

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Files(stack, req, res);
    }

    protected void populateParams() {
        try {
            if (StringUtils.isNotBlank(listUrlId)) {
                this.setDynamicAttribute(null, "data-pk", listUrlId);
            }
            if (StringUtils.isNotBlank(listUrlPrefix)) {
                HttpServletRequest request = ServletActionContext.getRequest();
                this.setDynamicAttribute(null, "data-url", request.getContextPath() + listUrlPrefix);
            }
            if (StringUtils.isNotBlank(category)) {
                this.setDynamicAttribute(null, "data-category", category);
            }
            if (StringUtils.isNotBlank(readonly)) {
                this.setDynamicAttribute(null, "data-readonly", readonly);
            }
        } catch (JspException e) {
            e.printStackTrace();
        }

        super.populateParams();

        Files uiBean = ((Files) component);
        if (theme == null) {
            uiBean.setTheme("bootstrap3");
        }
        if (cssClass == null) {
            uiBean.setCssClass("btn green btn-fileinput-trigger");
        }
    }

    public void setListUrlPrefix(String listUrlPrefix) {
        this.listUrlPrefix = listUrlPrefix;
    }

    public void setListUrlId(String listUrlId) {
        this.listUrlId = listUrlId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

}
