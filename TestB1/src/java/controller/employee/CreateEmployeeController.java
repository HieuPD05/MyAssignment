package controller.employee;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import model.Employee;

public class CreateEmployeeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            PrintWriter out = resp.getWriter();
            out.print("Access denied");

        } else {
            req.getRequestDispatcher("../emp/create.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            PrintWriter out = resp.getWriter();
            out.print("Access denied");
            return;
        }

        String name = req.getParameter("name");
        String gender = req.getParameter("gender");
        String dob = req.getParameter("dob");
        String[] skills = req.getParameterValues("skills");

        if (name == null || name.isEmpty() || dob == null || skills == null) {
            req.setAttribute("msg", "Please fill in all fields!");
            req.getRequestDispatcher("../emp/create.jsp").forward(req, resp);
            return;
        }

        // ✅ Tạo đối tượng Employee
        Employee emp = new Employee(name, gender, dob, Arrays.asList(skills));

        // ✅ Lấy danh sách từ session
        List<Employee> list = (List<Employee>) session.getAttribute("employees");
        if (list == null) {
            list = new ArrayList<>();
        }

        // ✅ Thêm vào danh sách
        list.add(emp);
        session.setAttribute("employees", list);

        // ✅ Hiển thị thông báo "added {n}"
        int count = list.size();
        req.setAttribute("msg", "added {" + count + "}");

        // ✅ Quay lại form
        req.getRequestDispatcher("../emp/create.jsp").forward(req, resp);
    }
}
