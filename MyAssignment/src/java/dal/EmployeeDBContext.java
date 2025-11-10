package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Department;
import model.Employee;

public class EmployeeDBContext extends DBContext<Employee> {

    /** Trả về did của 1 nhân viên */
    public Integer getDivisionIdByEmployeeId(int eid) {
        Integer did = null;
        try {
            PreparedStatement stm = connection.prepareStatement(
                "SELECT did FROM Employee WHERE eid=?"
            );
            stm.setInt(1, eid);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) did = rs.getInt("did");
            rs.close(); stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally { closeConnection(); }
        return did;
    }

    /** Lấy toàn bộ nhân viên của 1 phòng (có cả người chưa có đơn) */
    public ArrayList<Employee> listByDivision(int did) {
        ArrayList<Employee> emps = new ArrayList<>();
        try {
            String sql = """
                SELECT e.eid, e.ename, e.did, d.dname
                FROM Employee e
                JOIN Division d ON d.did = e.did
                WHERE e.did = ?
                ORDER BY e.ename
            """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, did);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getString("ename"));

                Department dept = new Department();
                dept.setId(rs.getInt("did"));
                dept.setName(rs.getString("dname"));
                e.setDept(dept);

                emps.add(e);
            }
            rs.close(); stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally { closeConnection(); }
        return emps;
    }
    /** Cập nhật tình trạng nhân viên: 0|1|2 -> Terminated|Official|Probation
 *  Trả về số dòng ảnh hưởng.
 */
public int updateEmploymentStatus(int eid, Integer status) {
    if (status == null) return 0;

    // Mapping theo yêu cầu: Chính thức / Nghỉ việc / Thử việc
    // -> 1 = Official, 0 = Terminated, 2 = Probation
    String newStatus;
    switch (status) {
        case 1: newStatus = "Official";   break;
        case 0: newStatus = "Terminated"; break;
        case 2: newStatus = "Probation";  break;
        default: return 0;
    }

    int rows = 0;
    try {
        String sql = "UPDATE Employee SET employment_status = ? WHERE eid = ?";
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setNString(1, newStatus);
        stm.setInt(2, eid);
        rows = stm.executeUpdate();
        stm.close();
    } catch (SQLException ex) {
        Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        closeConnection();
    }
    return rows;
}
 public void updateStatus(int eid, String status) throws SQLException {
        String sql = "UPDATE Employee SET employment_status=? WHERE eid=?";
        try (PreparedStatement s = connection.prepareStatement(sql)) {
            s.setString(1, status);
            s.setInt(2, eid);
            s.executeUpdate();
        }
    }


    @Override public ArrayList<Employee> list() { throw new UnsupportedOperationException("Not supported."); }
    @Override public Employee get(int id) { throw new UnsupportedOperationException("Not supported."); }
    @Override public void insert(Employee model) { throw new UnsupportedOperationException("Not supported."); }
    @Override public void update(Employee model) { throw new UnsupportedOperationException("Not supported."); }
    @Override public void delete(Employee model) { throw new UnsupportedOperationException("Not supported."); }
}
