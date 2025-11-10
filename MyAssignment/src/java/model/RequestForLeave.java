package model;

import java.sql.Date;
import java.sql.Timestamp;

public class RequestForLeave extends BaseModel {
    private int rid;
    private int createdBy;
    private Date from;
    private Date to;
    private int ltid;
    private String reason;
    private int status;            // 0 Inprogress, 1 Approved, 2 Rejected, 3 Canceled
    private Integer processedBy;   // nullable
    private Timestamp processedTime;

    public int getRid() { return rid; }
    public void setRid(int rid) { this.rid = rid; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public Date getFrom() { return from; }
    public void setFrom(Date from) { this.from = from; }

    public Date getTo() { return to; }
    public void setTo(Date to) { this.to = to; }

    public int getLtid() { return ltid; }
    public void setLtid(int ltid) { this.ltid = ltid; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public Integer getProcessedBy() { return processedBy; }
    public void setProcessedBy(Integer processedBy) { this.processedBy = processedBy; }

    public Timestamp getProcessedTime() { return processedTime; }
    public void setProcessedTime(Timestamp processedTime) { this.processedTime = processedTime; }
}
