package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.AccountRequest;

public class AccountRequestDBContext extends DBContext<AccountRequest> {

    @Override
    public ArrayList<AccountRequest> list() { return new ArrayList<>(); }

    @Override
    public AccountRequest get(int id) { return null; }

    @Override
    public void insert(AccountRequest model) { throw new UnsupportedOperationException("Not used here"); }

    @Override
    public void update(AccountRequest model) { throw new UnsupportedOperationException("Not used here"); }

    @Override
    public void delete(AccountRequest model) { throw new UnsupportedOperationException("Not used here"); }

    public int create(int requesterEid, String name, String div, String pos, String note) throws SQLException {
        String sql = "INSERT INTO AccountRequest(requester_eid,target_name,target_div,target_position,note) VALUES(?,?,?,?,?)";
        try (PreparedStatement s = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, requesterEid);
            s.setString(2, name);
            s.setString(3, div);
            s.setString(4, pos);
            s.setString(5, note);
            s.executeUpdate();
            try (ResultSet rs = s.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<AccountRequest> listOpen() throws SQLException {
        String sql = "SELECT arid, requester_eid, target_name, target_div, target_position, note, status, created_at " +
                     "FROM AccountRequest WHERE status=0 ORDER BY created_at DESC";
        List<AccountRequest> list = new ArrayList<>();
        try (PreparedStatement s = connection.prepareStatement(sql);
             ResultSet rs = s.executeQuery()) {
            while (rs.next()) {
                AccountRequest a = new AccountRequest();
                a.setArid(rs.getInt("arid"));
                a.setRequesterEid(rs.getInt("requester_eid"));
                a.setTargetName(rs.getString("target_name"));
                a.setTargetDiv(rs.getString("target_div"));
                a.setTargetPosition(rs.getString("target_position"));
                a.setNote(rs.getString("note"));
                a.setStatus(rs.getInt("status"));
                list.add(a);
            }
        }
        return list;
    }

    public void close(int arid, int adminUid, boolean approved) throws SQLException {
        String sql = "UPDATE AccountRequest SET status=?, processed_by_uid=?, processed_at=SYSUTCDATETIME() WHERE arid=?";
        try (PreparedStatement s = connection.prepareStatement(sql)) {
            s.setInt(1, approved ? 1 : 2);
            s.setInt(2, adminUid);
            s.setInt(3, arid);
            s.executeUpdate();
        }
    }
}
