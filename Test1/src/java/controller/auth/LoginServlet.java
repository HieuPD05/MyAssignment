package controller.auth;

import dal.AccountDBContext;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import model.Account;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        AccountDBContext db = new AccountDBContext();
        Account account = db.get(username, password);

        if (account != null) {
            // ✅ Lưu thông tin đăng nhập vào session
            HttpSession session = request.getSession();
            session.setAttribute("account", account);
            response.sendRedirect("home.jsp"); // login success
        } else {
            // ❌ Sai thông tin
            request.setAttribute("error", "Login failed!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
