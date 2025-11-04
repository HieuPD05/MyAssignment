package model;

public class AuditLog extends BaseModel {
    private String action;         // CREATE/UPDATE/DELETE/APPROVE/REJECT/ADMIN_DELETE...
    private Integer requestId;     // null nếu không gắn request cụ thể
    private Integer actorEmployeeId;
    private java.util.Date time;
    private String details;

    public String getAction(){ return action; }
    public void setAction(String action){ this.action = action; }

    public Integer getRequestId(){ return requestId; }
    public void setRequestId(Integer requestId){ this.requestId = requestId; }

    public Integer getActorEmployeeId(){ return actorEmployeeId; }
    public void setActorEmployeeId(Integer actorEmployeeId){ this.actorEmployeeId = actorEmployeeId; }

    public java.util.Date getTime(){ return time; }
    public void setTime(java.util.Date time){ this.time = time; }

    public String getDetails(){ return details; }
    public void setDetails(String details){ this.details = details; }
}
