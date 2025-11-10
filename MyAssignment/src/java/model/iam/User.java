package model.iam;

import model.BaseModel;

public class User extends BaseModel {
    private int uid;
    private String username;
    private String password;     // lưu hash
    private String displayname;

    public int getUid() { return uid; }
    public void setUid(int uid) { this.uid = uid; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDisplayname() { return displayname; }
    public void setDisplayname(String displayname) { this.displayname = displayname; }
}
