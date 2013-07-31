package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.Component;
import org.apache.struts2.components.DynamicTable;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 针对类似批量订单行项编辑业务场景,提供一个动态表格组件,实现基础的表格行项元素动态增减处理
 */
public class S2DynamicTableTag extends AbstractClosingTag {

    /** 可选参数，默认值为1：控制删除剩余最少行项数 */
    protected String minRows;
    /** 可选参数：控制最大添加行项数 */
    protected String maxRows;
    /** 可选参数：点击添加行项回调函数,一般用于为行项元素做额外初始化处理，row参数为当前添加行项的JQuery TR对象实例 */
    protected String afterAdd;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new DynamicTable(stack, req, res);
    }

    protected void populateParams() {
        
        try {
            if (StringUtils.isNotBlank(minRows)) {
                this.setDynamicAttribute(null, "minRows", minRows);
            }
            if (StringUtils.isNotBlank(maxRows)) {
                this.setDynamicAttribute(null, "maxRows", maxRows);
            }
            if (StringUtils.isNotBlank(afterAdd)) {
                this.setDynamicAttribute(null, "afterAdd", afterAdd);
            }
        } catch (JspException e) {
            e.printStackTrace();
        }
        
        super.populateParams();

        DynamicTable uiBean = ((DynamicTable) component);
        if (id == null) {
            //设置ID随机
            uiBean.setId("dynamictable_" + RandomStringUtils.randomAlphabetic(10));
        }
        if (cssClass == null) {
            uiBean.setCssClass("table table-striped table-condensed");
        }
    }

    public void setMinRows(String minRows) {
        this.minRows = minRows;
    }

    public void setMaxRows(String maxRows) {
        this.maxRows = maxRows;
    }

    public void setAfterAdd(String afterAdd) {
        this.afterAdd = afterAdd;
    }
}
