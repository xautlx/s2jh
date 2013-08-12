package lab.s2jh.sys.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MenuVO.
 */
public class NavMenuVO {

    private String id;

    /** 菜单名称，菜单显示的字面值. */
    private String name;

    /**
     * 菜单URL.
     */
    private String url = "";

    /** 父节点. */
    private NavMenuVO parent;

    /** 孩子节点. */
    private List<NavMenuVO> children = new ArrayList<NavMenuVO>();

    /** 是否默认展开菜单组 */
    private Boolean open = Boolean.FALSE;

    /** 显示标识 */
    private Boolean show = Boolean.FALSE;

    public void setShow(Boolean show) {
        this.show = show;
        if (show == true & this.getParent() != null) {
            this.getParent().setShow(true);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NavMenuVO getParent() {
        return parent;
    }

    public void setParent(NavMenuVO parent) {
        this.parent = parent;
    }

    public List<NavMenuVO> getChildren() {
        return children;
    }

    public void setChildren(List<NavMenuVO> children) {
        this.children = children;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getShow() {
        return show;
    }
}