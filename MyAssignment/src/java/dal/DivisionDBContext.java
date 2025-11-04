package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Department;

/** Dùng cho dropdown phòng ban, hoặc duyệt phòng ở Agenda/HR page */
public class DivisionDBContext extends DBContext<Department> {

    public ArrayList<Department> listAll(){
        ArrayList<Department> ds = new ArrayList<>();
        try{
            String sql = "SELECT did, dname FROM Division ORDER BY dname";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()){
                Department d = new Department();
                d.setId(rs.getInt("did"));
                d.setName(rs.getString("dname"));
                ds.add(d);
            }
            rs.close(); stm.close();
        }catch(SQLException ex){
            Logger.getLogger(DivisionDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return ds;
    }

    @Override public ArrayList<Department> list(){ return listAll(); }
    @Override public Department get(int id){ throw new UnsupportedOperationException("Not supported."); }
    @Override public void insert(Department m){ throw new UnsupportedOperationException("Not supported."); }
    @Override public void update(Department m){ throw new UnsupportedOperationException("Not supported."); }
    @Override public void delete(Department m){ throw new UnsupportedOperationException("Not supported."); }
}
