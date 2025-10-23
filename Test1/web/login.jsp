package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hi?n th? form login
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // L?y th�ng tin ng??i d�ng nh?p
        String userInput = request.getParameter("username");
        String passInput = request.getParameter("password");

        // L?y th�ng tin t? init-param trong web.xml
        String configUser = getServletConfig().getInitParameter("username");
        String configPass = getServletConfig().getInitParameter("password");

        // So s�nh
        if (userInput != null && passInput != null
                && userInput.equals(configUser)
                && passInput.equals(configPass)) {

            // ? L?u v�o session
            HttpSession session = request.getSession();
            session.setAttribute("loggedUser", userInput);
            response.sendRedirect("home.jsp");

        } else {
            // ? Sai t�i kho?n
            request.setAttribute("error", "Login failed!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
