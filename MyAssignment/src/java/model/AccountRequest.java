package model;

public class AccountRequest extends BaseModel {
    private int arid;
    private int requesterEid;
    private String targetName;
    private String targetDiv;
    private String targetPosition;
    private String note;
    private int status; // 0 Open, 1 Done, 2 Rejected
    private java.time.LocalDateTime createdAt;
    private Integer processedByUid;
    private java.time.LocalDateTime processedAt;

    public int getArid() { return arid; }
    public void setArid(int arid) { this.arid = arid; }

    public int getRequesterEid() { return requesterEid; }
    public void setRequesterEid(int requesterEid) { this.requesterEid = requesterEid; }

    public String getTargetName() { return targetName; }
    public void setTargetName(String targetName) { this.targetName = targetName; }

    public String getTargetDiv() { return targetDiv; }
    public void setTargetDiv(String targetDiv) { this.targetDiv = targetDiv; }

    public String getTargetPosition() { return targetPosition; }
    public void setTargetPosition(String targetPosition) { this.targetPosition = targetPosition; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getProcessedByUid() { return processedByUid; }
    public void setProcessedByUid(Integer processedByUid) { this.processedByUid = processedByUid; }

    public java.time.LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(java.time.LocalDateTime processedAt) { this.processedAt = processedAt; }
}
