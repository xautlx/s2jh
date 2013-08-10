package lab.s2jh.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "T_SYS_PUB_POST")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(title = "公告", description = "用于向应用所有用户显示的公告消息，不做用户或权限区分控制")
public class PubPost extends BaseEntity<String> {

    @MetaData(title = "标题")
    @EntityAutoCode(order = 20)
    private String htmlTitle;

    @MetaData(title = "发布时间")
    @EntityAutoCode(order = 30)
    private Date publishTime;

    @MetaData(title = "到期时间")
    @EntityAutoCode(order = 40)
    private Date expireTime;

    @MetaData(title = "公告内容")
    @EntityAutoCode(order = 40)
    private String htmlContent;

    @MetaData(title = "总计查看用户数")
    @EntityAutoCode(order = 50)
    private Integer readUserCount;
    
    @MetaData(title = "关联附件")
    @EntityAutoCode(order = 100, search = false)
    private AttachmentFile r2File;

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

    @Override
    @Transient
    public String getDisplayLabel() {
        return htmlTitle;
    }

    @Column(nullable = false)
    public String getHtmlTitle() {
        return htmlTitle;
    }

    public void setHtmlTitle(String htmlTitle) {
        this.htmlTitle = htmlTitle;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @JsonIgnore
    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

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

    @OneToOne
    @JoinColumn(name = "FILE_ID")
    public AttachmentFile getR2File() {
        return r2File;
    }

    public void setR2File(AttachmentFile r2File) {
        this.r2File = r2File;
    }
}
