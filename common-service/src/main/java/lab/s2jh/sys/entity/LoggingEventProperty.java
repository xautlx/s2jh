package lab.s2jh.sys.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/** 
 * @see http://logback.qos.ch/manual/configuration.html #DBAppender
 */
@Entity
@Table(name = "logging_event_property")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class LoggingEventProperty implements java.io.Serializable {

    private LoggingEventPropertyId id;
    private LoggingEvent loggingEvent;
    private String mappedValue;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "eventId", column = @Column(name = "event_id", nullable = false)),
            @AttributeOverride(name = "mappedKey", column = @Column(name = "mapped_key", nullable = false, length = 254)) })
    public LoggingEventPropertyId getId() {
        return this.id;
    }

    public void setId(LoggingEventPropertyId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false, insertable = false, updatable = false)
    public LoggingEvent getLoggingEvent() {
        return this.loggingEvent;
    }

    public void setLoggingEvent(LoggingEvent loggingEvent) {
        this.loggingEvent = loggingEvent;
    }

    @Lob
    @Column(name = "mapped_value")
    public String getMappedValue() {
        return this.mappedValue;
    }

    public void setMappedValue(String mappedValue) {
        this.mappedValue = mappedValue;
    }

}
