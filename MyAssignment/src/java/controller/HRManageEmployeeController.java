package controller;

import dal.AccountRequestDBContext;
import dal.EmployeeDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name="HRManageEmployeeController", urlPatterns={"/hr/employee/manage"})
public class HRManageEmployeeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Trang quản lý dùng chung view employee.jsp
        req.getRequestDispatcher("/view/hr/employee.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("updateStatus".equals(action)) {
                int eid = Integer.parseInt(req.getParameter("eid"));
                String status = req.getParameter("status"); // Official/Probation/Terminated
                new EmployeeDBContext().updateStatus(eid, status);
                req.setAttribute("msg", "Cập nhật trạng thái thành công!");
            } else if ("requestAccount".equals(action)) {
                // HR tạo yêu cầu lập tài khoản gửi Admin
                int requesterEid = Integer.parseInt(req.getParameter("requesterEid"));
                String name = req.getParameter("targetName");
                String div = req.getParameter("targetDiv");         // IT/HR/MKT
                String pos = req.getParameter("targetPosition");    // Employee/Team Lead...
                String note = req.getParameter("note");
                new AccountRequestDBContext().create(requesterEid, name, div, pos, note);
                req.setAttribute("msg", "Đã gửi yêu cầu lập tài khoản tới Admin!");
            }
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
        }
        req.getRequestDispatcher("/view/hr/employee.jsp").forward(req, resp);
    }
}
