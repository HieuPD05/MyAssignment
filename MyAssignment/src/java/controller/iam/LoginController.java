/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.iam;

import dal.UserDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.iam.User;

/**
 *
 * @author sonnt
 */
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
        // Lưu session
        HttpSession session = req.getSession();
        session.setAttribute("auth", u);

        // ✅ CHUYỂN HƯỚNG ĐẾN TRANG HOME
        resp.sendRedirect(req.getContextPath() + "/home");
    } else {
        // Sai tài khoản → quay lại login
        req.setAttribute("message", "❌ Sai tên đăng nhập hoặc mật khẩu!");
        req.getRequestDispatcher("view/auth/message.jsp").forward(req, resp);
    }
}


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("view/auth/login.jsp").forward(req, resp);
    }
}
