package lab.s2jh.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lab.s2jh.auth.entity.User;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "T_SYS_PUB_POST_READ", uniqueConstraints = @UniqueConstraint(columnNames = { "pub_post_id",
        "read_user_id" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@MetaData(title = "公告阅读记录")
public class PubPostRead extends BaseEntity<String> {

    @MetaData(title = "公告")
    @EntityAutoCode(order = 10)
    private PubPost pubPost;

    @MetaData(title = "阅读用户")
    @EntityAutoCode(order = 40)
    private User readUser;

    @MetaData(title = "首次阅读时间")
    @EntityAutoCode(order = 30)
    private Date firstReadTime;

    @MetaData(title = "最后阅读时间")
    @EntityAutoCode(order = 35)
    private Date lastReadTime;

    @MetaData(title = "总计阅读次数")
    @EntityAutoCode(order = 40)
    private Integer readTotalCount = 1;

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
        return null;
    }

    @ManyToOne
    @JoinColumn(name = "pub_post_id", nullable = false)
    @JsonIgnore
    public PubPost getPubPost() {
        return pubPost;
    }

    public void setPubPost(PubPost pubPost) {
        this.pubPost = pubPost;
    }

    @ManyToOne
    @JoinColumn(name = "read_user_id", nullable = false)
    @JsonIgnore
    public User getReadUser() {
        return readUser;
    }

    public void setReadUser(User readUser) {
        this.readUser = readUser;
    }

    @Transient
    public String getReadUserLabel() {
        return readUser.getDisplayLabel();
    }

    @Column(nullable = false)
    public Date getFirstReadTime() {
        return firstReadTime;
    }

    public void setFirstReadTime(Date firstReadTime) {
        this.firstReadTime = firstReadTime;
    }

    public Date getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(Date lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    @Column(nullable = false)
    public Integer getReadTotalCount() {
        return readTotalCount;
    }

    public void setReadTotalCount(Integer readTotalCount) {
        this.readTotalCount = readTotalCount;
    }
}
