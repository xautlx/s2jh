package lab.s2jh.sys.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/** 
 * @see http://logback.qos.ch/manual/configuration.html #DBAppender
 */
@Embeddable
public class LoggingEventExceptionId implements java.io.Serializable {

    private Long eventId;
    private short i;

    public LoggingEventExceptionId() {
    }

    public LoggingEventExceptionId(Long eventId, short i) {
        this.eventId = eventId;
        this.i = i;
    }

    @Column(name = "event_id", nullable = false)
    public Long getEventId() {
        return this.eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Column(name = "i", nullable = false)
    public short getI() {
        return this.i;
    }

    public void setI(short i) {
        this.i = i;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof LoggingEventExceptionId))
            return false;
        LoggingEventExceptionId castOther = (LoggingEventExceptionId) other;

        return ((this.getEventId() == castOther.getEventId()) || (this.getEventId() != null
                && castOther.getEventId() != null && this.getEventId().equals(castOther.getEventId())))
                && (this.getI() == castOther.getI());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (getEventId() == null ? 0 : this.getEventId().hashCode());
        result = 37 * result + this.getI();
        return result;
    }

}
