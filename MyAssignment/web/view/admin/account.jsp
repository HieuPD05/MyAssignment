<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Quản lý tài khoản</title>
  <style>
    body{font-family:Segoe UI,Arial;margin:24px}
    h2{margin-bottom:12px}
    .msg{padding:10px;border-radius:6px;margin:10px 0}
    .ok{background:#e9fff0;border:1px solid #b6f0c8;color:#0b6b2f}
    .err{background:#ffecec;border:1px solid #ffbcbc;color:#9b1111}
    table{border-collapse:collapse;width:100%;margin-top:12px}
    th,td{border:1px solid #ddd;padding:8px}
    th{background:#f5f7fb;text-align:left}
    form.inline{display:inline}
    .grid{display:grid;grid-template-columns:1fr 1fr;gap:24px;margin-top:22px}
    input,select{width:100%;padding:8px;border:1px solid #cfd4dc;border-radius:8px}
    label{display:block;margin-top:10px;font-weight:600}
    .btn{padding:8px 14px;border:0;border-radius:8px;background:#3b4b63;color:#fff;cursor:pointer}
    .btn.del{background:#b71c1c}
  </style>
</head>
<body>
  <jsp:include page="/view/common/bell.jspf"/>

  <h2>Admin – Quản lý tài khoản</h2>

  <c:if test="${not empty msg}"><div class="msg ok">${msg}</div></c:if>
  <c:if test="${not empty error}"><div class="msg err">${error}</div></c:if>

  <div class="grid">
    <div>
      <h3>Danh sách tài khoản</h3>
      <table>
        <thead>
          <tr><th>UID</th><th>Username</th><th>Display</th><th>Thao tác</th></tr>
        </thead>
        <tbody>
        <c:forEach var="u" items="${users}">
          <tr>
            <td>${u.uid}</td>
            <td>${u.username}</td>
            <td>${u.displayname}</td>
            <td>
              <form class="inline" method="post" action="${pageContext.request.contextPath}/admin/account">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="uid" value="${u.uid}">
                <button class="btn del" onclick="return confirm('Xóa tài khoản #${u.uid}?')">Xóa</button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>

    <div>
      <h3>Tạo tài khoản mới</h3>
      <form method="post" action="${pageContext.request.contextPath}/admin/account">
        <input type="hidden" name="action" value="create">
        <label>Username</label>
        <input type="text" name="username" placeholder="vd: mrX" required>

        <label>Display name</label>
        <input type="text" name="display" placeholder="Họ tên..." required>

        <label>Mật khẩu (demo)</label>
        <input type="text" name="password" value="123" required>

        <label>Gán Role</label>
        <select name="role">
          <option value="">-- chọn --</option>
          <option value="ADMIN">ADMIN</option>
          <option value="CEO">CEO</option>
          <option value="HEAD">HEAD</option>
          <option value="TEAM_LEAD">TEAM_LEAD</option>
          <option value="EMPLOYEE">EMPLOYEE</option>
        </select>

        <label>Enroll vào Employee (EID) – tuỳ chọn</label>
        <input type="number" name="eid" placeholder="ví dụ: 12">

        <div style="margin-top:12px">
          <button class="btn">Tạo tài khoản</button>
        </div>
      </form>
    </div>
  </div>
</body>
</html>
