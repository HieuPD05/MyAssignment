package model;

public class User extends BaseModel {
    private String username;
    private String password;
    private String displayname;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {   // ← PHẢI có dòng này
        this.password = password;
    }

    public String getDisplayname() {
        return displayname;
    }
    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
}
