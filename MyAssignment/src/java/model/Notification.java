package model;

public class Notification extends BaseModel {
    private int nid;
    private int toUid;
    private String title;
    private String content;
    private String linkUrl;
    private boolean read;
    private java.time.LocalDateTime createdAt;

    public int getNid() { return nid; }
    public void setNid(int nid) { this.nid = nid; }

    public int getToUid() { return toUid; }
    public void setToUid(int toUid) { this.toUid = toUid; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
}
