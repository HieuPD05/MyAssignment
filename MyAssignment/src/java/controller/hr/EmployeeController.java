package controller.hr;

import controller.iam.BaseRequiredAuthorizationController;
import dal.DivisionDBContext;
import dal.EmployeeDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import model.Department;
import model.Employee;
import model.iam.User;

/**
 * /hr/employee
 * - GET: chọn phòng (did) và xem danh sách + trạng thái
 * - POST: cập nhật trạng thái employment_status (0/1/2)
 */
@WebServlet(urlPatterns = "/hr/employee")
public class EmployeeController extends BaseRequiredAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        String did_raw = req.getParameter("did");
        Integer did = null;
        if (did_raw != null && !did_raw.isEmpty()) {
            did = Integer.parseInt(did_raw);
        } else if (user.getEmployee() != null && user.getEmployee().getDept() != null) {
            did = user.getEmployee().getDept().getId();
        }

        ArrayList<Department> divisions = new DivisionDBContext().listAll();
        ArrayList<Employee> employees = new ArrayList<>();
        if (did != null) {
            employees = new EmployeeDBContext().listByDivision(did);
        }

        req.setAttribute("divisions", divisions);
        req.setAttribute("employees", employees);
        req.setAttribute("did", did);
        req.getRequestDispatcher("../view/hr/employee.jsp").forward(req, resp);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        String eid_raw = req.getParameter("eid");
        String status_raw = req.getParameter("status"); // null|0|1|2
        Integer status = null;
        if (status_raw != null && !status_raw.isEmpty()) {
            status = Integer.valueOf(status_raw);
        }
        int eid = Integer.parseInt(eid_raw);

        new EmployeeDBContext().updateEmploymentStatus(eid, status);

        // quay về danh sách của phòng hiện tại
        String did = req.getParameter("did");
        resp.sendRedirect(req.getContextPath() + "/hr/employee?did=" + (did == null ? "" : did));
    }
}
