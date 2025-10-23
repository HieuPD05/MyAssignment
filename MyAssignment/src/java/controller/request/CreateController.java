package controller.request;

import controller.iam.BaseRequiredAuthorizationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/request/create")
public class CreateController extends BaseRequiredAuthorizationController {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        // Bắt buộc đặt đầu tiên để đọc UTF-8 đúng
        req.setCharacterEncoding("UTF-8");

        try {
            Date from = Date.valueOf(req.getParameter("from"));
            Date to = Date.valueOf(req.getParameter("to"));
            String reason = req.getParameter("reason");

            if (reason == null || reason.trim().isEmpty()) {
                throw new Exception("Reason cannot be empty");
            }

            RequestForLeave r = new RequestForLeave();
            r.setCreated_by(user.getEmployee());
            r.setFrom(from);
            r.setTo(to);
            r.setReason(reason);
            r.setStatus(0); // In Progress

            RequestForLeaveDBContext db = new RequestForLeaveDBContext();
            db.insert(r);

            resp.sendRedirect("list");
        } catch (Exception e) {
            req.setAttribute("error", "❗ Vui lòng nhập đầy đủ thông tin hợp lệ!");
            req.getRequestDispatcher("../view/request/create.jsp").forward(req, resp);
        }
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        req.getRequestDispatcher("../view/request/create.jsp").forward(req, resp);
    }
}
