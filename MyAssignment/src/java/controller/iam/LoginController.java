package controller.iam;

import dal.RoleDBContext;
import dal.UserDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import model.iam.Feature;
import model.iam.Role;
import model.iam.User;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        UserDBContext db = new UserDBContext();
        User u = db.get(username, password);
        if (u != null) {
            RoleDBContext rdb = new RoleDBContext();
            u.setRoles(rdb.getByUserId(u.getId()));

            HashMap<String, Boolean> allowed = new HashMap<>();
            for (Role r : u.getRoles()) {
                for (Feature f : r.getFeatures()) {
                    allowed.put(f.getUrl(), true);
                }
            }

            HttpSession session = req.getSession();
            session.setAttribute("auth", u);
            session.setAttribute("allowed", allowed);
            resp.sendRedirect(req.getContextPath() + "/home");
        } else {
            req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không hợp lệ.");
            req.setAttribute("typedUsername", username);
            req.getRequestDispatcher("view/auth/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("view/auth/login.jsp").forward(req, resp);
    }
}
