<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>HR – Quản lý nhân viên</title>
<style>
.container{max-width:1100px;margin:18px auto;padding:0 14px}
h1{margin:14px 0}
table{width:100%;border-collapse:collapse;background:#fff;border:1px solid #e5e7eb}
th,td{padding:10px;border-bottom:1px solid #e5e7eb;text-align:left;vertical-align:middle}
select,input{padding:8px;border:1px solid #cbd5e1;border-radius:8px}
.btn{padding:8px 12px;border:none;border-radius:8px;background:#1f2937;color:#fff;cursor:pointer}
.row{display:grid;grid-template-columns:1fr 1fr;gap:12px;margin:14px 0}
.card{border:1px solid #e5e7eb;border-radius:10px;padding:14px;background:#fff}
</style>
</head>
<body>
<jsp:include page="/view/common/topbar.jsp"/>
<div class="container">
  <h1>HR – Quản lý nhân viên</h1>

  <c:if test="${not empty param.msg}">
    <div style="margin:8px 0;color:#16a34a">✅ ${param.msg}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div style="margin:8px 0;color:#dc2626">❌ ${error}</div>
  </c:if>

  <div class="card">
    <h3>Cập nhật trạng thái nhân viên</h3>
    <table>
      <thead><tr><th>EID</th><th>Tên</th><th>Vị trí</th><th>Phòng</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
      <tbody>
        <c:forEach items="${emps}" var="e">
          <tr>
            <td>${e['eid']}</td>
<td>${e['ename']}</td>
<td>${e['position']}</td>
<td>${e['div']}</td>

            <td>
              <form method="post" action="${pageContext.request.contextPath}/hr/employee">
                <input type="hidden" name="action" value="update_status">
                <input type="hidden" name="eid" value="${e.eid}">
                <select name="status">
                  <option ${e.status=='Official'?'selected':''}>Official</option>
                  <option ${e.status=='Probation'?'selected':''}>Probation</option>
                  <option ${e.status=='Terminated'?'selected':''}>Terminated</option>
                </select>
                <button class="btn" type="submit">Lưu</button>
              </form>
            </td>
            <td></td>
          </tr>
        </c:forEach>
        <c:if test="${empty emps}">
          <tr><td colspan="6">Không có dữ liệu.</td></tr>
        </c:if>
      </tbody>
    </table>
  </div>

  <div class="card">
    <h3>Gửi yêu cầu lập tài khoản (gửi tới Admin)</h3>
    <form method="post" action="${pageContext.request.contextPath}/hr/employee">
      <input type="hidden" name="action" value="request_account">
      <div class="row">
        <div>
          <label>Họ tên</label><br>
          <input type="text" name="target_name" required style="width:100%">
        </div>
        <div>
          <label>Phòng ban</label><br>
          <select name="target_div" required style="width:100%">
            <option value="IT">IT</option>
            <option value="HR">HR</option>
            <option value="MKT">Marketing</option>
          </select>
        </div>
        <div>
          <label>Vị trí</label><br>
          <select name="target_position" required style="width:100%">
            <option>Employee</option>
            <option>Team Lead</option>
          </select>
        </div>
        <div>
          <label>Ghi chú</label><br>
          <input type="text" name="note" style="width:100%">
        </div>
      </div>
      <button class="btn" type="submit">Gửi yêu cầu</button>
    </form>
  </div>
</div>
</body>
</html>
