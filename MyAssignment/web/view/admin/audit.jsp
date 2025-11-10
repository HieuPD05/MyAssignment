<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Audit – Nhật ký hệ thống</title>
<style>
:root{ --base:#3b4b63; --base-strong:#334257; --ink:#ffffff; --muted:#e5edf7; --line:#2d394a; --shadow:0 16px 40px rgba(0,0,0,.28); --radius:18px; }
html{ min-height:100%; background: linear-gradient(180deg, rgba(255,255,255,.12), rgba(255,255,255,.2)), url('${pageContext.request.contextPath}/img/hinhnen3.jpg') center/cover no-repeat fixed; }
body{margin:0;min-height:100dvh;font-family:Inter,"Segoe UI",Arial,sans-serif;color:var(--ink)}
body::after{content:"";position:fixed;inset:0;backdrop-filter:blur(2px) saturate(1.05);z-index:0}
.topbar{position:sticky;top:0;z-index:10;background:var(--base);border-bottom:1px solid var(--line); box-shadow:var(--shadow);display:flex;align-items:center;justify-content:space-between;padding:12px 18px}
.brand{display:flex;gap:10px;align-items:center;font-weight:800}
.logo{width:28px;height:28px;border-radius:9px;background:linear-gradient(135deg,#6a7d97,var(--base)); display:grid;place-items:center}
.menu{display:flex;gap:10px}
.menu a{color:#fff;text-decoration:none;background:var(--base-strong);padding:9px 14px;border-radius:12px;border:1px solid var(--line)}
.menu a:hover{background:var(--base-strong)}
.wrap{position:relative;z-index:1;max-width:1000px;margin:36px auto;padding:0 20px}
.panel{background:var(--base);border:1px solid var(--line);border-radius:var(--radius);box-shadow:var(--shadow);padding:18px}
table{width:100%;border-collapse:separate;border-spacing:0 8px;color:#fff}
thead th{padding:8px;text-align:left}
tbody tr{background:#334257;border:1px solid var(--line);border-radius:12px}
tbody td{padding:10px}
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
    <h2>🔍 Nhật ký hệ thống</h2>
    <table>
      <thead>
        <tr>
          <th>#</th>
          <th>Hành động</th>
          <th>Request ID</th>
          <th>Actor EID</th>
          <th>Thời gian</th>
          <th>Chi tiết</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="a" items="${logs}">
          <tr>
            <td>${a.id}</td>
            <td><span class="badge">${a.action}</span></td>
            <td>${a.requestId}</td>
            <td>${a.actorEmployeeId}</td>
            <td>${a.time}</td>
            <td>${a.details}</td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </section>
</main>
</body>
</html>
