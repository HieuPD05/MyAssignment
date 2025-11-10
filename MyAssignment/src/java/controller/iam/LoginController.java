package controller.iam;

import dal.UserDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.iam.User;

public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/view/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        req.setAttribute("typedUsername", username);

        try {
            UserDBContext udb = new UserDBContext();
            User u = udb.getByUsername(username);
            if (udb.verifyAndAutoHash(u, password)) {
                HttpSession ss = req.getSession();
                ss.setAttribute("auth", u);                    // User
                ss.setAttribute("uid",  u.getUid());           // int
                Integer eid = udb.getEidByUid(u.getUid());     // có thể null (admin)
                if (eid != null) ss.setAttribute("eid", eid);
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }
            req.setAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            req.getRequestDispatcher("/view/auth/login.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
