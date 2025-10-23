package controller.employee;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import model.Employee;

public class ListEmployeeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
              PrintWriter out = resp.getWriter();
            out.print("Access denied");
            return;
        }

        List<Employee> list = (List<Employee>) session.getAttribute("employees");
        req.setAttribute("list", list);
        req.getRequestDispatcher("../emp/list.jsp").forward(req, resp);
    }
}
