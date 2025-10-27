package controller.request;

import controller.iam.BaseRequiredAuthenticationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/request/cancel")
public class CancelController extends BaseRequiredAuthenticationController {

    // Không cho hủy bằng GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    // POST: chỉ hủy (xóa) nếu là chủ đơn & status=0
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        int rid = Integer.parseInt(req.getParameter("rid"));

        // (Tuỳ chọn) kiểm tra tồn tại để báo lỗi rõ ràng hơn
        RequestForLeave r = new RequestForLeaveDBContext().get(rid);
        if (r == null) {
            req.getSession().setAttribute("message", "❌ Không tìm thấy đơn.");
            resp.sendRedirect(req.getContextPath() + "/request/list");
            return;
        }

        int rows = new RequestForLeaveDBContext()
                .deleteIfOwnerAndPending(rid, user.getEmployee().getId());

        if (rows > 0) {
            req.getSession().setAttribute("message", "🗑️ Đã hủy đơn.");
        } else {
            req.getSession().setAttribute("message",
                "❌ Không thể hủy (đơn đã duyệt/bị từ chối hoặc không phải của bạn).");
        }
        resp.sendRedirect(req.getContextPath() + "/request/list");
    }
}
