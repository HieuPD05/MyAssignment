package dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Course;

public class CourseDBContext extends DBContext<Course> {

    @Override
    public void insert(Course c) {
        try {
            String sql = "INSERT INTO Course(name, [from], [to], online, subject, created_by) "
                       + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, c.getName());
            stm.setString(2, c.getFrom());
            stm.setString(3, c.getTo());
            stm.setBoolean(4, c.isOnline());
            stm.setString(5, c.getSubject());
            stm.setString(6, c.getCreatedBy().getUsername());
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CourseDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @Override public ArrayList<Course> list() { return null; }
    @Override public Course get(int id) { return null; }
    @Override public void update(Course model) {}
    @Override public void delete(Course model) {}
}
