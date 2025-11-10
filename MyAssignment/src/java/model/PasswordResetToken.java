package model;

import java.time.LocalDateTime;

public class PasswordResetToken {
    private String tokenId;    // UUID (string)
    private int uid;
    private String token;
    private LocalDateTime expireAt;
    private boolean used;
    private LocalDateTime createdAt;

    public String getTokenId() { return tokenId; }
    public void setTokenId(String tokenId) { this.tokenId = tokenId; }

    public int getUid() { return uid; }
    public void setUid(int uid) { this.uid = uid; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getExpireAt() { return expireAt; }
    public void setExpireAt(LocalDateTime expireAt) { this.expireAt = expireAt; }

    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
