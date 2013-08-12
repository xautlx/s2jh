package lab.s2jh.sys.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;

import org.apache.struts2.components.File.FileDef;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "T_SYS_ATTACHMENT_FILE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(title = "附件文件数据")
public class AttachmentFile extends BaseEntity<String> implements FileDef{

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

    /** 文件数据 */
    private byte[] fileContent;

    private String entityClassName;

    private String entityId;

    private Date lastTouchTime;

    private String lastTouchBy;

    private String id;

    /** 直接以文件byte数据计算的MD5码作为唯一标识 */
    @Id
    @Column(length = 40)
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Column(length = 500, nullable = false)
    public String getFileRealName() {
        return fileRealName;
    }

    public void setFileRealName(String fileRealName) {
        this.fileRealName = fileRealName;
    }

    @Column(nullable = false)
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

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
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
    public String getDisplayLabel() {
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
}
