package dal;

import model.Employee;
import model.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** HR page: quản lý danh sách nhân sự theo phòng + cập nhật trạng thái làm việc */
public class EmployeeDBContext extends DBContext<Employee> {

    /** Lấy nhân viên theo Division (did) */
    public ArrayList<Employee> listByDivision(int did){
        ArrayList<Employee> list = new ArrayList<>();
        try{
            // Nếu HP.sql chưa có cột employment_status, bỏ cột này trong SELECT.
            String sql = "SELECT eid, ename, did, supervisorid, employment_status FROM Employee WHERE did = ? ORDER BY ename";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, did);
            ResultSet rs = stm.executeQuery();
            while (rs.next()){
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getString("ename"));

                Department d = new Department();
                d.setId(rs.getInt("did"));
                e.setDept(d);

                // supervisor (nếu cần hiển thị)
                // int sup = rs.getInt("supervisorid"); if (!rs.wasNull()) { Employee s = new Employee(); s.setId(sup); e.setSupervisor(s); }

                // employment_status: 0/1/2 hoặc null
                e.setEmploymentStatus((Integer) rs.getObject("employment_status"));

                list.add(e);
            }
            rs.close(); stm.close();
        }catch(SQLException ex){
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return list;
    }

    /** HR cập nhật trạng thái làm việc: 0=Probation,1=Official,2=Terminated */
    public int updateEmploymentStatus(int eid, Integer status){
        try{
            String sql = "UPDATE Employee SET employment_status = ? WHERE eid = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            if (status==null) stm.setNull(1, Types.INTEGER);
            else stm.setInt(1, status);
            stm.setInt(2, eid);
            int rows = stm.executeUpdate();
            stm.close();
            return rows;
        }catch(SQLException ex){
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return 0;
    }

    @Override public ArrayList<Employee> list(){ throw new UnsupportedOperationException("Not supported."); }
    @Override public Employee get(int id){ throw new UnsupportedOperationException("Not supported."); }
    @Override public void insert(Employee m){ throw new UnsupportedOperationException("Not supported."); }
    @Override public void update(Employee m){ throw new UnsupportedOperationException("Not supported."); }
    @Override public void delete(Employee m){ throw new UnsupportedOperationException("Not supported."); }
}
