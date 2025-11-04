package controller.division;

import controller.iam.BaseRequiredAuthorizationController;
import dal.DivisionDBContext;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import model.Department;
import model.Employee;
import model.RequestForLeave;
import model.iam.Role;
import model.iam.User;

@WebServlet(urlPatterns = "/division/agenda")
public class ViewAgendaController extends BaseRequiredAuthorizationController {

    private boolean isCEO(User u){
        for (Role r : u.getRoles()){
            if ("CEO".equalsIgnoreCase(r.getName())) return true;
        }
        return false;
    }

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

            // --- Chọn phòng cho Agenda ---
            Integer did = null;
            String did_raw = req.getParameter("did");
            boolean ceo = isCEO(user);

            if (ceo) {
                if (did_raw != null && !did_raw.isEmpty()) {
                    did = Integer.parseInt(did_raw);
                } else {
                    did = user.getEmployee().getDept() != null ? user.getEmployee().getDept().getId() : null;
                }
            } else {
                // không phải CEO: chỉ xem phòng của chính mình
                did = user.getEmployee().getDept() != null ? user.getEmployee().getDept().getId() : null;
            }

            // Lấy toàn bộ request (theo org tree user) – như cũ
            RequestForLeaveDBContext db = new RequestForLeaveDBContext();
            ArrayList<RequestForLeave> leaves = db.getByEmployeeAndSubodiaries(user.getEmployee().getId());

            // Lọc theo Division nếu có did (CEO hoặc người khác đều áp dụng)
            if (did != null) {
                Iterator<RequestForLeave> it = leaves.iterator();
                while (it.hasNext()) {
                    RequestForLeave r = it.next();
                    if (r.getCreated_by() == null || r.getCreated_by().getDept() == null) continue; // nếu không join dept
                    Integer rdid = r.getCreated_by().getDept().getId();
                    if (rdid == null || rdid.intValue() != did.intValue()) it.remove();
                }
            }

            // Tập nhân viên xuất hiện
            Map<Integer, Employee> employees = new LinkedHashMap<>();
            for (RequestForLeave r : leaves) {
                employees.putIfAbsent(r.getCreated_by().getId(), r.getCreated_by());
            }

            // Bảng agenda: mặc định false (đi làm), true = off nếu có đơn Approved
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

            // danh sách Division cho CEO chọn
            ArrayList<Department> divisions = new DivisionDBContext().listAll();

            req.setAttribute("fromStr", from.toString());
            req.setAttribute("toStr", to.toString());
            req.setAttribute("dates", dates);
            req.setAttribute("employees", employees.values());
            req.setAttribute("agenda", agenda);
            req.setAttribute("divisions", divisions);
            req.setAttribute("did", did);
            req.setAttribute("isCEO", ceo);

            req.getRequestDispatcher("../view/division/agenda.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Lỗi khi tải dữ liệu Agenda!");
        }
    }
}
