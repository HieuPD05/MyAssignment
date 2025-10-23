package controller.division;

import controller.iam.BaseRequiredAuthorizationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import model.Employee;
import model.RequestForLeave;
import model.iam.User;

@WebServlet(urlPatterns = "/division/agenda")
public class ViewAgendaController extends BaseRequiredAuthorizationController {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        processGet(req, resp, user);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        try {
            // Lấy khoảng thời gian từ form (hoặc mặc định)
            String from_raw = req.getParameter("from");
            String to_raw = req.getParameter("to");

            LocalDate from = (from_raw == null || from_raw.isEmpty())
                    ? LocalDate.now().withDayOfMonth(1)
                    : LocalDate.parse(from_raw);
            LocalDate to = (to_raw == null || to_raw.isEmpty())
                    ? from.plusDays(7)
                    : LocalDate.parse(to_raw);

            // Lấy tất cả đơn nghỉ phép của cả phòng
            RequestForLeaveDBContext db = new RequestForLeaveDBContext();
            ArrayList<RequestForLeave> leaves = db.getByEmployeeAndSubodiaries(user.getEmployee().getId());

            // Dựng danh sách nhân viên
            Map<Integer, Employee> employees = new LinkedHashMap<>();
            for (RequestForLeave r : leaves) {
                employees.putIfAbsent(r.getCreated_by().getId(), r.getCreated_by());
            }

            // Dựng bảng trạng thái (true = nghỉ)
            Map<Integer, Map<LocalDate, Boolean>> agenda = new LinkedHashMap<>();

            for (Employee e : employees.values()) {
                Map<LocalDate, Boolean> days = new LinkedHashMap<>();
                LocalDate tmp = from;
                while (!tmp.isAfter(to)) {
                    days.put(tmp, false);
                    tmp = tmp.plusDays(1);
                }

                for (RequestForLeave r : leaves) {
                    if (r.getCreated_by().getId() == e.getId() && r.getStatus() == 1) { // chỉ tính approved
                        LocalDate start = r.getFrom().toLocalDate();
                        LocalDate end = r.getTo().toLocalDate();
                        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                            if (days.containsKey(d)) {
                                days.put(d, true); // nghỉ
                            }
                        }
                    }
                }
                agenda.put(e.getId(), days);
            }

            req.setAttribute("from", from);
            req.setAttribute("to", to);
            req.setAttribute("employees", employees.values());
            req.setAttribute("agenda", agenda);

            req.getRequestDispatcher("../view/division/agenda.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Lỗi khi tải dữ liệu Agenda!");
        }
    }
}
