package dal;

import java.util.ArrayList;
import model.iam.Role;
import model.iam.Feature;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Lấy Role + Feature cho một user (để authorize theo URL feature) */
public class RoleDBContext extends DBContext<Role> {

    public ArrayList<Role> getByUserId(int uid) {
        ArrayList<Role> roles = new ArrayList<>();
        try {
            String sql = """
            SELECT r.rid, r.rname, f.fid, f.url
            FROM [User] u
            INNER JOIN [UserRole] ur ON u.uid = ur.uid
            INNER JOIN [Role] r ON r.rid = ur.rid
            INNER JOIN [RoleFeature] rf ON rf.rid = r.rid
            INNER JOIN [Feature] f ON f.fid = rf.fid
            WHERE u.uid = ?
            ORDER BY r.rid, f.fid
            """;
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, uid);
            ResultSet rs = stm.executeQuery();

            Role current = new Role();
            current.setId(-1);

            while (rs.next()) {
                int rid = rs.getInt("rid");
                if (rid != current.getId()) {
                    current = new Role();
                    current.setId(rid);
                    current.setName(rs.getString("rname"));
                    roles.add(current);
                }
                Feature f = new Feature();
                f.setId(rs.getInt("fid"));
                f.setUrl(rs.getString("url"));
                current.getFeatures().add(f);
            }
            rs.close(); stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(RoleDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return roles;
    }

    @Override public ArrayList<Role> list() { throw new UnsupportedOperationException("Not supported."); }
    @Override public Role get(int id)       { throw new UnsupportedOperationException("Not supported."); }
    @Override public void insert(Role m)    { throw new UnsupportedOperationException("Not supported."); }
    @Override public void update(Role m)    { throw new UnsupportedOperationException("Not supported."); }
    @Override public void delete(Role m)    { throw new UnsupportedOperationException("Not supported."); }
}
