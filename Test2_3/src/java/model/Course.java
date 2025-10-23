package model;

public class Course extends BaseModel {
    private String name;
    private String from;
    private String to;
    private boolean online;
    private String subject;
    private User createdBy;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
}
