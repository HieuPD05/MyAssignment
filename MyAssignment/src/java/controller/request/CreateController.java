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

/**
 * @author sonnt
 */
@WebServlet(urlPatterns = "/request/create")
public class CreateController extends BaseRequiredAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        // Hiển thị form tạo đơn
        req.getRequestDispatcher("/view/request/create.jsp").forward(req, resp);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        try {
            // Lấy dữ liệu từ form
            String from_raw = req.getParameter("from");
            String to_raw = req.getParameter("to");
            String reason = req.getParameter("reason");

            // Kiểm tra hợp lệ
            if (from_raw == null || to_raw == null || reason == null || reason.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
                req.getRequestDispatcher("/view/request/create.jsp").forward(req, resp);
                return;
            }

            Date from = Date.valueOf(from_raw);
            Date to = Date.valueOf(to_raw);

            // Tạo đối tượng Request
            RequestForLeave r = new RequestForLeave();
            r.setFrom(from);
            r.setTo(to);
            r.setReason(reason);
            r.setCreated_by(user.getEmployee());
            r.setStatus(0); // In progress

            // Lưu vào DB
            RequestForLeaveDBContext db = new RequestForLeaveDBContext();
            db.insert(r);

            // Chuyển hướng về danh sách kèm thông báo
            req.getSession().setAttribute("message", "✅ Tạo đơn nghỉ phép thành công!");
            resp.sendRedirect(req.getContextPath() + "/request/list");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ Lỗi khi tạo đơn nghỉ phép: " + e.getMessage());
            req.getRequestDispatcher("/view/request/create.jsp").forward(req, resp);
        }
    }
}
