package lab.s2jh.sys.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "T_SYS_MENU", uniqueConstraints = @UniqueConstraint(columnNames = { "title", "PARENT_ID" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(title = "菜单")
public class Menu extends BaseEntity<String> implements Comparable<Menu> {

    /** 用于报表菜单项计算的固定菜单代码 */
    public static final String MENU_CODE_RPT = "MFIXRPT";

    @MetaData(title = "代码")
    @EntityAutoCode(order = 10, search = true)
    private String code;

    @MetaData(title = "名称")
    @EntityAutoCode(order = 20, search = true)
    private String title;

    @MetaData(title = "描述")
    @EntityAutoCode(listShow = false)
    private String description;

    @MetaData(title = "禁用标识", description = "禁用菜单全局不显示")
    @EntityAutoCode(order = 40, search = true)
    private Boolean disabled = Boolean.FALSE;

    /**
     * @see #type
     */
    @MetaData(title = "菜单URL")
    @EntityAutoCode(order = 30, search = true)
    private String url;

    @MetaData(title = "图标样式")
    @EntityAutoCode(listHidden = true, order = 40)
    private String style;

    @MetaData(title = "类型")
    @EntityAutoCode(order = 30, search = true)
    private MenuTypeEnum type = MenuTypeEnum.RELC;

    @MetaData(title = "排序号", description = "相对排序号，数字越大越靠上显示")
    @EntityAutoCode(order = 1000)
    private Integer orderRank = 100;

    @MetaData(title = "展开标识", description = "是否默认展开菜单组")
    @EntityAutoCode(order = 30, search = true)
    private Boolean initOpen = Boolean.FALSE;

    @MetaData(title = "父节点")
    private Menu parent;

    @MetaData(title = "子节点集合")
    private List<Menu> children;

    @MetaData(title = "子节点数", description = "冗余字段：当前节点下属子节点数目，可以用于快速确定当前是否叶子节点")
    private Integer childrenSize;

    @MetaData(title = "所在层级", description = " 冗余字段：当前节点所在层级，方便高效的树形层级显示")
    private Integer inheritLevel;

    private String id;

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public static enum MenuTypeEnum {

        @MetaData(title = "相对上下文")
        RELC,

        @MetaData(title = "相对域名")
        RELD,

        @MetaData(title = "绝对路径")
        ABS;

    }

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    @JsonIgnore
    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent")
    @OrderBy("orderRank desc")
    @JsonIgnore
    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    @Column(length = 1000)
    @JsonIgnore
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(length = 256)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    public MenuTypeEnum getType() {
        return type;
    }

    public void setType(MenuTypeEnum type) {
        this.type = type;
    }

    public Integer getOrderRank() {
        return orderRank;
    }

    public void setOrderRank(Integer orderRank) {
        this.orderRank = orderRank;
    }

    public Boolean getInitOpen() {
        return initOpen;
    }

    public void setInitOpen(Boolean initOpen) {
        this.initOpen = initOpen;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getChildrenSize() {
        return childrenSize;
    }

    public void setChildrenSize(Integer childrenSize) {
        this.childrenSize = childrenSize;
    }

    public Integer getInheritLevel() {
        return inheritLevel;
    }

    public void setInheritLevel(Integer inheritLevel) {
        this.inheritLevel = inheritLevel;
    }

    @Column(length = 128)
    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Column(length = 128, nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    @Transient
    public String getDisplayLabel() {
        return title;
    }

    @Override
    public int compareTo(Menu o) {
        return CompareToBuilder.reflectionCompare(o.getOrderRank(), this.getOrderRank());
    }

    @Column(nullable = false, unique = true, length = 64)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 计算节点所在层级，根节点以0开始
     * @return
     */
    @Transient
    @JsonIgnore
    public int getLevel() {
        int level = 0;
        return loopLevel(level, this);
    }

    private int loopLevel(int level, Menu menu) {
        Menu parent = menu.getParent();
        if (parent != null && parent.getId() != null) {
            return loopLevel(level + 1, menu.getParent());
        }
        return level;
    }
}
