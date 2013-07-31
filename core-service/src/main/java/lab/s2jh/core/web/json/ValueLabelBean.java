package lab.s2jh.core.web.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ValueLabelBean implements Serializable {

    private String label;
    private String value;

    private String extData;
    private List<ValueLabelBean> children;

    public ValueLabelBean(String value, String label) {
        super();
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<ValueLabelBean> getChildren() {
        return children;
    }

    public void setChildren(List<ValueLabelBean> children) {
        this.children = children;
    }

    public void addChild(ValueLabelBean vlb) {
        if (children == null) {
            children = new ArrayList<ValueLabelBean>();
        }
        children.add(vlb);
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }
}
