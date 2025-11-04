package dal;

import model.AuditLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Ghi và xem audit log (Admin xem lịch sử) */
public class AuditDBContext extends DBContext<AuditLog> {

    public void log(String action, Integer requestId, Integer actorEmployeeId, String details){
        try {
            String sql = "INSERT INTO AuditLog(action, request_id, actor_eid, time, details) VALUES (?,?,?,?,?)";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, action);
            if (requestId == null) stm.setNull(2, Types.INTEGER); else stm.setInt(2, requestId);
            if (actorEmployeeId == null) stm.setNull(3, Types.INTEGER); else stm.setInt(3, actorEmployeeId);
            stm.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            stm.setNString(5, details);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException ex){
            Logger.getLogger(AuditDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    public ArrayList<AuditLog> list(){
        ArrayList<AuditLog> arr = new ArrayList<>();
        try {
            String sql = "SELECT id, action, request_id, actor_eid, time, details FROM AuditLog ORDER BY time DESC";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()){
                AuditLog a = new AuditLog();
                a.setId(rs.getInt("id"));
                a.setAction(rs.getString("action"));
                a.setRequestId((Integer)rs.getObject("request_id"));
                a.setActorEmployeeId((Integer)rs.getObject("actor_eid"));
                a.setTime(rs.getTimestamp("time"));
                a.setDetails(rs.getString("details"));
                arr.add(a);
            }
            rs.close(); stm.close();
        } catch (SQLException ex){
            Logger.getLogger(AuditDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return arr;
    }

    @Override public AuditLog get(int id){ throw new UnsupportedOperationException("Not supported."); }
    @Override public void insert(AuditLog m){ throw new UnsupportedOperationException("Not supported."); }
    @Override public void update(AuditLog m){ throw new UnsupportedOperationException("Not supported."); }
    @Override public void delete(AuditLog m){ throw new UnsupportedOperationException("Not supported."); }
}
