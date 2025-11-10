package dal;

import java.sql.*;
import java.util.ArrayList;
import model.BaseModel;
import model.Notification;

public class NotificationDBContext extends DBContext {

    public void push(int toUid, String title, String content, String linkUrl) throws SQLException {
        String sql = "INSERT INTO Notification(to_uid, title, content, link_url, is_read, created_at) " +
                     "VALUES(?,?,?,?,0, SYSUTCDATETIME())";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, toUid);
            st.setString(2, title);
            st.setString(3, content);
            st.setString(4, linkUrl);
            st.executeUpdate();
        }
    }

    public ArrayList<Notification> latestOfUser(int uid, int topN) throws SQLException {
        String sql = "SELECT TOP(?) nid, to_uid, title, content, link_url, is_read, created_at " +
                     "FROM Notification WHERE to_uid=? ORDER BY created_at DESC";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, topN);
            st.setInt(2, uid);
            ArrayList<Notification> list = new ArrayList<>();
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Notification n = new Notification();
                    // các setter tên phổ biến
                    try { n.getClass().getMethod("setNid", int.class).invoke(n, rs.getInt("nid")); } catch (Exception ignored) {}
                    try { n.getClass().getMethod("setToUid", int.class).invoke(n, rs.getInt("to_uid")); } catch (Exception ignored) {}
                    try { n.getClass().getMethod("setTitle", String.class).invoke(n, rs.getString("title")); } catch (Exception ignored) {}
                    try { n.getClass().getMethod("setContent", String.class).invoke(n, rs.getString("content")); } catch (Exception ignored) {}
                    try { n.getClass().getMethod("setLinkUrl", String.class).invoke(n, rs.getString("link_url")); } catch (Exception ignored) {}
                    try { n.getClass().getMethod("setIsRead", boolean.class).invoke(n, rs.getBoolean("is_read")); } catch (Exception ignored) {}
                    try { n.getClass().getMethod("setCreatedAt", Timestamp.class).invoke(n, rs.getTimestamp("created_at")); } catch (Exception ignored) {}
                    list.add(n);
                }
            }
            return list;
        }
    }

    public void markRead(int nid) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement(
                "UPDATE Notification SET is_read=1 WHERE nid=?")) {
            st.setInt(1, nid);
            st.executeUpdate();
        }
    }

    public int countUnread(int uid) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Notification WHERE to_uid=? AND is_read=0";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, uid);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    @Override
    public ArrayList list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BaseModel get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void insert(BaseModel model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(BaseModel model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(BaseModel model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object latest(int uid, int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
