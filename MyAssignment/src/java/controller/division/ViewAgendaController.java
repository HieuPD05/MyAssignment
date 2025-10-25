package controller.division;

import controller.iam.BaseRequiredAuthorizationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
            String from_raw = req.getParameter("from");
            String to_raw   = req.getParameter("to");

            LocalDate from = (from_raw == null || from_raw.isEmpty())
                    ? LocalDate.now().withDayOfMonth(1)
                    : LocalDate.parse(from_raw);
            LocalDate to = (to_raw == null || to_raw.isEmpty())
                    ? from.plusDays(7)
                    : LocalDate.parse(to_raw);

            // danh sách ngày cho header
            List<LocalDate> dates = new ArrayList<>();
            for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) dates.add(d);

            RequestForLeaveDBContext db = new RequestForLeaveDBContext();
            ArrayList<RequestForLeave> leaves = db.getByEmployeeAndSubodiaries(user.getEmployee().getId());

            Map<Integer, Employee> employees = new LinkedHashMap<>();
            for (RequestForLeave r : leaves) {
                employees.putIfAbsent(r.getCreated_by().getId(), r.getCreated_by());
            }

            Map<Integer, Map<LocalDate, Boolean>> agenda = new LinkedHashMap<>();
            for (Employee e : employees.values()) {
                Map<LocalDate, Boolean> days = new LinkedHashMap<>();
                for (LocalDate d : dates) days.put(d, false);

                for (RequestForLeave r : leaves) {
                    if (r.getCreated_by().getId() == e.getId() && r.getStatus() == 1) {
                        LocalDate s = r.getFrom().toLocalDate();
                        LocalDate t = r.getTo().toLocalDate();
                        for (LocalDate d = s; !d.isAfter(t); d = d.plusDays(1)) {
                            if (days.containsKey(d)) days.put(d, true);
                        }
                    }
                }
                agenda.put(e.getId(), days);
            }

            req.setAttribute("fromStr", from.toString());
            req.setAttribute("toStr", to.toString());
            req.setAttribute("dates", dates);
            req.setAttribute("employees", employees.values());
            req.setAttribute("agenda", agenda);

            req.getRequestDispatcher("../view/division/agenda.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Lỗi khi tải dữ liệu Agenda!");
        }
    }
}
