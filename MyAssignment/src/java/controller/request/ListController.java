package controller.request;

import controller.iam.BaseRequiredAuthenticationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/request/list")
public class ListController extends BaseRequiredAuthenticationController {

    @SuppressWarnings("unchecked")
    private boolean canList(HttpServletRequest req) {
        Object obj = req.getSession().getAttribute("allowed");
        if (obj == null) return false;
        try {
            HashMap<String, Boolean> allowed = (HashMap<String, Boolean>) obj;
            Boolean v = allowed.get("/request/list");
            return v != null && v;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    private void showPermissionError(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("permError", true);
        req.setAttribute("error",
            "❌ Tài khoản của bạn chưa đủ quyền xem danh sách đơn. "
          + "Vui lòng liên hệ quản lý để được cấp quyền.");
        req.getRequestDispatcher("/view/request/list.jsp").forward(req, resp);
    }

    private Integer parseStatusFilter(String s) {
        if (s == null || s.trim().isEmpty() || "all".equalsIgnoreCase(s)) return null;
        try {
            int v = Integer.parseInt(s.trim());
            if (v == 0 || v == 1 || v == 2) return v;
        } catch (NumberFormatException ignore) {}
        return null;
    }

    private void renderList(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        String statusParam = req.getParameter("status");        // all|0|1|2
        Integer statusFilter = parseStatusFilter(statusParam);  // null = all

        String q = req.getParameter("q"); // tìm theo tên người tạo (substring)
        String qNorm = (q == null) ? "" : q.trim().toLowerCase();

        RequestForLeaveDBContext db = new RequestForLeaveDBContext();
        ArrayList<RequestForLeave> all =
                db.getByEmployeeAndSubodiaries(user.getEmployee().getId());

        ArrayList<RequestForLeave> filtered = new ArrayList<>();
        for (RequestForLeave r : all) {
            if (statusFilter != null && r.getStatus() != statusFilter) continue;
            if (!qNorm.isEmpty()) {
                String name = r.getCreated_by() != null && r.getCreated_by().getName() != null
                        ? r.getCreated_by().getName().toLowerCase()
                        : "";
                if (!name.contains(qNorm)) continue;
            }
            filtered.add(r);
        }

        req.setAttribute("rfls", filtered);
        req.setAttribute("statusSelected", statusFilter == null ? "all" : String.valueOf(statusFilter));
        req.setAttribute("q", q == null ? "" : q);

        req.getRequestDispatcher("/view/request/list.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        if (!canList(req)) { showPermissionError(req, resp); return; }
        renderList(req, resp, user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        if (!canList(req)) { showPermissionError(req, resp); return; }
        renderList(req, resp, user);
    }
}
