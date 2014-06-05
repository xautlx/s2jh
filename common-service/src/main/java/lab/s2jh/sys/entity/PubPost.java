package lab.s2jh.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.AttachmentableEntity;
import lab.s2jh.core.entity.BaseUuidEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "tbl_SYS_PUB_POST")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "公告", comments = "用于向应用所有用户显示的公告消息，不做用户或权限区分控制")
public class PubPost extends BaseUuidEntity implements AttachmentableEntity {

    private static final long serialVersionUID = 2544390748513253055L;

    @MetaData(value = "标题")
    private String htmlTitle;

    @MetaData(value = "发布时间")
    private Date publishTime;

    @MetaData(value = "到期时间")
    private Date expireTime;

    @MetaData(value = "前端显示")
    private Boolean frontendShow = Boolean.FALSE;

    @MetaData(value = "后台显示")
    private Boolean backendShow = Boolean.TRUE;

    @MetaData(value = "外部链接")
    @EntityAutoCode(order = 40)
    private String externalLink;

    @MetaData(value = "公告内容")
    @EntityAutoCode(order = 45)
    private String htmlContent;

    @MetaData(value = "总计查看用户数")
    @EntityAutoCode(order = 50)
    private Integer readUserCount;

    @MetaData(value = "排序号", tooltips = "数字越大显示越靠上")
    @EntityAutoCode(order = 50)
    private Integer orderRank = 100;

    @Override
    @Transient
    public String getDisplay() {
        return htmlTitle;
    }

    @Column(nullable = false)
    public String getHtmlTitle() {
        return htmlTitle;
    }

    public void setHtmlTitle(String htmlTitle) {
        this.htmlTitle = htmlTitle;
    }

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @Lob
    @JsonIgnore
    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getReadUserCount() {
        return readUserCount;
    }

    public void setReadUserCount(Integer readUserCount) {
        this.readUserCount = readUserCount;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public Boolean getFrontendShow() {
        return frontendShow;
    }

    public void setFrontendShow(Boolean frontendShow) {
        this.frontendShow = frontendShow;
    }

    public Boolean getBackendShow() {
        return backendShow;
    }

    public void setBackendShow(Boolean backendShow) {
        this.backendShow = backendShow;
    }

    public Integer getOrderRank() {
        return orderRank;
    }

    public void setOrderRank(Integer orderRank) {
        this.orderRank = orderRank;
    }

    @Transient
    public boolean isInternal() {
        return StringUtils.isBlank(externalLink);
    }
    
    @MetaData(value="关联附件个数",comments="用于列表显示和关联处理附件清理判断")
    private Integer attachmentSize;

    @Override
    public Integer getAttachmentSize() {
        return attachmentSize;
    }

    public void setAttachmentSize(Integer attachmentSize) {
        this.attachmentSize = attachmentSize;
    }
}
