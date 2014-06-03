package lab.s2jh.sys.entity;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.File.FileDef;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "TBL_SYS_ATTACHMENT_FILE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "附件文件数据")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE)
public class AttachmentFile extends BaseEntity<String> implements FileDef {

    /** 附件上传文件名称 */
    private String fileRealName;

    /** 文件描述 */
    private String fileDescription;

    /** 附件扩展名 */
    private String fileExtension;

    /** 附件大小 */
    private int fileLength;

    /** 附件MIME类型 */
    private String fileType;

    private String fileRelativePath;

    private String entityClassName;

    @MetaData(value="关联实体主键")
    private String entityId;
    
    @MetaData(value="分类",comments="如果一个对象有多个类型关联附件，可通过此属性进行分类")
    private String entityFileCategory;

    private Date lastTouchTime;

    private String lastTouchBy;

    private String id;

    /** 直接以文件byte数据计算的MD5码或UUID作为唯一标识 */
    @Id
    @Column(length = 80)
    @JsonProperty
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Column(length = 500, nullable = false)
    @JsonProperty
    public String getFileRealName() {
        return fileRealName;
    }

    public void setFileRealName(String fileRealName) {
        this.fileRealName = fileRealName;
    }

    @Column(nullable = false)
    @JsonProperty
    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    @Column(length = 32, nullable = false)
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Column(length = 8)
    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Column(length = 200, nullable = true)
    @JsonIgnore
    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    @Override
    @Transient
    public String getDisplay() {
        return fileRealName;
    }

    @Column(length = 512, nullable = true)
    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    @Column(length = 200, nullable = true)
    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Date getLastTouchTime() {
        return lastTouchTime;
    }

    public void setLastTouchTime(Date lastTouchTime) {
        this.lastTouchTime = lastTouchTime;
    }

    public String getLastTouchBy() {
        return lastTouchBy;
    }

    public void setLastTouchBy(String lastTouchBy) {
        this.lastTouchBy = lastTouchBy;
    }

    @Transient
    @JsonIgnore
    public static AttachmentFile buildInstance(File file) {
        //简便的做法用UUID作为主键，每次上传都会创建文件对象和数据记录，便于管理，但是存在相同文件重复保存情况
        String id = UUID.randomUUID().toString();

        DateTime now = new DateTime();
        StringBuilder sb = new StringBuilder();
        int year = now.getYear();
        sb.append("/" + year);
        String month = "";
        int monthOfYear = now.getMonthOfYear();
        if (monthOfYear < 10) {
            month = "0" + monthOfYear;
        } else {
            month = "" + monthOfYear;
        }
        String day = "";
        int dayOfMonth = now.getDayOfMonth();
        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
        } else {
            day = "" + dayOfMonth;
        }
        sb.append("/" + month);
        sb.append("/" + day);
        Assert.notNull(id, "id is required to buildInstance");
        sb.append("/" + StringUtils.substring(id, 0, 2));
        String path = sb.toString();

        AttachmentFile af = new AttachmentFile();
        af.setId(path.replaceAll("/", "") + id);
        af.setFileRelativePath(path);
        af.setFileLength((int) file.length());
        return af;
    }

    public String getFileRelativePath() {
        return fileRelativePath;
    }

    public void setFileRelativePath(String fileRelativePath) {
        this.fileRelativePath = fileRelativePath;
    }

    @Transient
    @JsonIgnore
    public String getDiskFileName() {
        return getId() + "." + getFileExtension();
    }

    public String getEntityFileCategory() {
        return entityFileCategory;
    }

    public void setEntityFileCategory(String entityFileCategory) {
        this.entityFileCategory = entityFileCategory;
    }
}
