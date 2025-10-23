package controller.auth;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String user = req.getParameter("user");
        String pass = req.getParameter("pass");

        String c_user = getInitParameter("username");
        String c_pass = getInitParameter("password");
        PrintWriter out = resp.getWriter();

        if (user != null && pass != null
                && user.equals(c_user) && pass.equals(c_pass)) {

            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            out.print("Login successfull");

        } else {
            // ✅ chuyển sang trang thông báo thất bại
            out.print("Login failed");

        }
    }
}
