<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>HR – Quản lý nhân sự</title>
<style>
:root{ --base:#3b4b63; --base-strong:#334257; --base-soft:#51637d; --ink:#ffffff; --muted:#e5edf7; --line:#2d394a; --shadow:0 16px 40px rgba(0,0,0,.28); --radius:18px; }
html{ min-height:100%; background: linear-gradient(180deg, rgba(255,255,255,.12), rgba(255,255,255,.2)), url('${pageContext.request.contextPath}/img/hinhnen3.jpg') center/cover no-repeat fixed; }
body{margin:0;min-height:100dvh;font-family:Inter,"Segoe UI",Arial,sans-serif;color:var(--ink)}
body::after{content:"";position:fixed;inset:0;backdrop-filter:blur(2px) saturate(1.05);z-index:0}
.topbar{position:sticky;top:0;z-index:10;background:var(--base);border-bottom:1px solid var(--line); box-shadow:var(--shadow);display:flex;align-items:center;justify-content:space-between;padding:12px 18px}
.brand{display:flex;gap:10px;align-items:center;font-weight:800}
.logo{width:28px;height:28px;border-radius:9px;background:linear-gradient(135deg,#6a7d97,var(--base)); display:grid;place-items:center}
.menu{display:flex;gap:10px}
.menu a{color:#fff;text-decoration:none;background:var(--base-strong);padding:9px 14px;border-radius:12px;border:1px solid var(--line)}
.menu a:hover{background:var(--base-soft)}
.wrap{position:relative;z-index:1;max-width:1000px;margin:36px auto;padding:0 20px}
.panel{background:var(--base);border:1px solid var(--line);border-radius:var(--radius);box-shadow:var(--shadow);padding:18px}
h2{margin:0 0 14px}
.filters{display:flex;gap:10px;align-items:center;flex-wrap:wrap;margin-bottom:12px}
.input{padding:8px 10px;border-radius:10px;background:#334257;color:#fff;border:1px solid var(--line)}
table{width:100%;border-collapse:separate;border-spacing:0 10px;color:#fff}
thead th{padding:8px;text-align:left}
tbody tr{background:#334257;border:1px solid var(--line);border-radius:12px}
tbody td{padding:10px}
.btn{padding:8px 12px;border-radius:10px;border:1px solid var(--line);background:#2b3748;color:#fff;font-weight:700;cursor:pointer}
.btn:hover{background:#42516a}
.badge{padding:4px 8px;border-radius:999px;border:1px solid #44556f;background:#1f2937}
</style>
</head>
<body>
<header class="topbar">
  <div class="brand"><div class="logo">HP</div>Hệ thống Quản lý Nghỉ phép</div>
  <nav class="menu">
    <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
    <a href="${pageContext.request.contextPath}/request/list">Danh sách</a>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
  </nav>
</header>

<main class="wrap">
  <section class="panel">
    <h2>👥 HR – Quản lý nhân sự</h2>

    <form class="filters" action="${pageContext.request.contextPath}/hr/employee" method="get">
      <label>Phòng ban:</label>
      <select class="input" name="did" onchange="this.form.submit()">
        <c:forEach var="d" items="${divisions}">
          <option value="${d.id}" ${did == d.id ? 'selected' : ''}>${d.name}</option>
        </c:forEach>
      </select>
      <noscript><button class="btn" type="submit">Xem</button></noscript>
    </form>

    <table>
      <thead>
        <tr>
          <th>Mã</th>
          <th>Họ tên</th>
          <th>Trạng thái</th>
          <th>Cập nhật</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="e" items="${employees}">
          <tr>
            <td>${e.id}</td>
            <td>${e.name}</td>
            <td>
              <span class="badge">
                <c:choose>
                  <c:when test="${e.employmentStatus == 1}">Chính thức</c:when>
                  <c:when test="${e.employmentStatus == 0}">Thử việc</c:when>
                  <c:when test="${e.employmentStatus == 2}">Nghỉ việc</c:when>
                  <c:otherwise>—</c:otherwise>
                </c:choose>
              </span>
            </td>
            <td>
              <form action="${pageContext.request.contextPath}/hr/employee" method="post" style="display:flex; gap:8px; align-items:center">
                <input type="hidden" name="eid" value="${e.id}"/>
                <input type="hidden" name="did" value="${did}"/>
                <select class="input" name="status">
                  <option value="">—</option>
                  <option value="0" ${e.employmentStatus == 0 ? 'selected' : ''}>Thử việc</option>
                  <option value="1" ${e.employmentStatus == 1 ? 'selected' : ''}>Chính thức</option>
                  <option value="2" ${e.employmentStatus == 2 ? 'selected' : ''}>Nghỉ việc</option>
                </select>
                <button class="btn" type="submit">Lưu</button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </section>
</main>
</body>
</html>
