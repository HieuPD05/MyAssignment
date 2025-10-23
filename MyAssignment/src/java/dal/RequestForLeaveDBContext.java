/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Employee;
import model.RequestForLeave;

/**
 * Quản lý dữ liệu bảng RequestForLeave
 * Hỗ trợ lấy danh sách, thêm mới, duyệt (update status)
 * Tự động đóng connection sau mỗi thao tác.
 * 
 * @author ChatGPT
 */
public class RequestForLeaveDBContext extends DBContext<RequestForLeave> {

    /**
     * Lấy tất cả đơn nghỉ phép của nhân viên và cấp dưới của họ
     */
    public ArrayList<RequestForLeave> getByEmployeeAndSubodiaries(int eid) {
        ArrayList<RequestForLeave> rfls = new ArrayList<>();
        try {
            String sql = """
                         WITH Org AS (
                             SELECT *, 0 as lvl FROM Employee e WHERE e.eid = ?
                             UNION ALL
                             SELECT c.*, o.lvl + 1 as lvl FROM Employee c 
                             JOIN Org o ON c.supervisorid = o.eid
                         )
                         SELECT
                             r.rid,
                             r.created_by,
                             e.ename as created_name,
                             r.created_time,
                             r.[from],
                             r.[to],
                             r.reason,
                             r.status,
                             r.processed_by,
                             p.ename as processed_name
                         FROM Org e 
                         INNER JOIN RequestForLeave r ON e.eid = r.created_by
                         LEFT JOIN Employee p ON p.eid = r.processed_by
                         """;

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, eid);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                RequestForLeave rfl = new RequestForLeave();

                // ⚠️ Bổ sung dòng này để rid không còn = 0
                rfl.setId(rs.getInt("rid"));

                rfl.setCreated_time(rs.getTimestamp("created_time"));
                rfl.setFrom(rs.getDate("from"));
                rfl.setTo(rs.getDate("to"));
                rfl.setReason(rs.getString("reason"));
                rfl.setStatus(rs.getInt("status"));

                Employee created_by = new Employee();
                created_by.setId(rs.getInt("created_by"));
                created_by.setName(rs.getString("created_name"));
                rfl.setCreated_by(created_by);

                int processed_by_id = rs.getInt("processed_by");
                if (processed_by_id != 0) {
                    Employee processed_by = new Employee();
                    processed_by.setId(processed_by_id);
                    processed_by.setName(rs.getString("processed_name"));
                    rfl.setProcessed_by(processed_by);
                }

                rfls.add(rfl);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return rfls;
    }

    /**
     * Thêm đơn nghỉ phép mới
     */
    @Override
    public void insert(RequestForLeave model) {
        try {
            String sql = """
                INSERT INTO [RequestForLeave]
                ([created_by], [created_time], [from], [to], [reason], [status])
                VALUES (?, GETDATE(), ?, ?, ?, 0)
            """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, model.getCreated_by().getId());
            stm.setDate(2, model.getFrom());
            stm.setDate(3, model.getTo());
            stm.setNString(4, model.getReason()); // Hỗ trợ Unicode
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    /**
     * Lấy thông tin chi tiết một đơn nghỉ phép
     */
    @Override
    public RequestForLeave get(int rid) {
        RequestForLeave r = null;
        try {
            String sql = """
                SELECT r.rid, r.[from], r.[to], r.[reason], r.[status],
                       e1.eid as created_id, e1.ename as created_name,
                       e2.eid as processed_id, e2.ename as processed_name
                FROM RequestForLeave r
                JOIN Employee e1 ON e1.eid = r.created_by
                LEFT JOIN Employee e2 ON e2.eid = r.processed_by
                WHERE r.rid = ?
            """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, rid);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                r = new RequestForLeave();
                r.setId(rs.getInt("rid"));
                r.setFrom(rs.getDate("from"));
                r.setTo(rs.getDate("to"));
                r.setReason(rs.getString("reason"));
                r.setStatus(rs.getInt("status"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return r;
    }

    /**
     * Cập nhật trạng thái duyệt đơn
     */
    public void updateStatus(int rid, int status, int processedById) {
        try {
            String sql = """
                UPDATE RequestForLeave
                SET status = ?, processed_by = ?
                WHERE rid = ?
            """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, status);
            stm.setInt(2, processedById);
            stm.setInt(3, rid);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    // Các hàm mặc định
    @Override
    public ArrayList<RequestForLeave> list() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(RequestForLeave model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(RequestForLeave model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
   public int countByStatus(int eid, int status) {
    int count = 0;
    try {
        // Tạo kết nối mới mỗi lần để tránh bị đóng sớm
        String sql = """
            WITH Org AS (
                SELECT *, 0 as lvl FROM Employee e WHERE e.eid = ?
                UNION ALL
                SELECT c.*, o.lvl + 1 FROM Employee c JOIN Org o ON c.supervisorid = o.eid
            )
            SELECT COUNT(*) AS total
            FROM Org o JOIN RequestForLeave r ON r.created_by = o.eid
            WHERE r.status = ?
        """;

        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setInt(1, eid);
        stm.setInt(2, status);
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            count = rs.getInt("total");
        }

        rs.close();
        stm.close();
    } catch (SQLException ex) {
        Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
    }
    return count;
}


}
