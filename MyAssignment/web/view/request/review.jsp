<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Duyệt đơn nghỉ phép</title>
<style>
:root{ --base:#3b4b63; --base-strong:#334257; --ink:#ffffff; --muted:#e5edf7; --line:#2d394a; --shadow:0 16px 40px rgba(0,0,0,.28); --radius:18px; --success:#22c55e; --success-2:#16a34a; --danger:#ef4444; --danger-2:#dc2626; }
html{ min-height:100%; background: linear-gradient(180deg, rgba(255,255,255,.12), rgba(255,255,255,.2)), url('${pageContext.request.contextPath}/img/hinhnen3.jpg') center/cover no-repeat fixed; }
body{ margin:0; min-height:100dvh; font-family:Inter,"Segoe UI",Arial,sans-serif; color:var(--ink); }
body::after{content:"";position:fixed;inset:0;backdrop-filter:blur(2px) saturate(1.05);z-index:0}
.topbar{ position:sticky;top:0;z-index:10;background:var(--base);border-bottom:1px solid var(--line); box-shadow:var(--shadow);display:flex;align-items:center;justify-content:space-between;padding:12px 18px }
.brand{display:flex;gap:10px;align-items:center;font-weight:800}
.logo{width:28px;height:28px;border-radius:9px;background:linear-gradient(135deg,#6a7d97,var(--base)); display:grid;place-items:center}
.menu{display:flex;gap:10px}
.menu a{color:#fff;text-decoration:none;background:var(--base-strong);padding:9px 14px;border-radius:12px;border:1px solid var(--line)}
.menu a:hover{background:var(--base-soft)}
.wrap{position:relative;z-index:1;max-width:760px;margin:48px auto;padding:0 20px}
.panel{ background:var(--base);border:1px solid var(--line);border-radius:var(--radius);box-shadow:var(--shadow); padding:26px }
h2{margin:0 0 12px}
p{margin:8px 0;color:var(--muted)}
.label{color:#fff;font-weight:700}
.actions{display:flex;gap:12px;margin-top:16px}
.btn{flex:1;display:inline-block;text-align:center;padding:12px;border-radius:12px;border:1px solid var(--line); color:#fff;cursor:pointer;font-weight:800}
.btn-success{background:var(--success)} .btn-success:hover{background:var(--success-2)}
.btn-danger{background:var(--danger)} .btn-danger:hover{background:var(--danger-2)}
.back{display:inline-block;margin-top:16px;color:#fff;text-decoration:none;background:var(--base-strong); padding:10px 14px;border-radius:10px;border:1px solid var(--line)}
.back:hover{background:var(--base-soft)}
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
    <h2>📝 Duyệt đơn nghỉ phép</h2>
    <c:set var="raw" value="${request.reason}" />
    <c:set var="pos" value="${fn:indexOf(raw,']')}" />
    <c:set var="type" value="${ (not empty raw and fn:startsWith(raw,'[') and pos gt 0) ? fn:substring(raw,1, pos) : 'N/A' }" />
    <c:set var="body" value="${ (not empty raw and fn:startsWith(raw,'[') and pos gt 0 and fn:length(raw) ge pos+2) ? fn:substring(raw, pos+2, fn:length(raw)) : raw }" />

    <p><span class="label">Loại đơn:</span> ${type}</p>
    <p><span class="label">Từ ngày:</span> ${request.from}</p>
    <p><span class="label">Tới ngày:</span> ${request.to}</p>
    <p><span class="label">Lý do:</span> ${body}</p>

    <form action="${pageContext.request.contextPath}/request/review" method="POST">
      <input type="hidden" name="rid" value="${request.id}">
      <div class="actions">
        <button type="submit" name="action" value="approve" class="btn btn-success">✅ Approve</button>
        <button type="submit" name="action" value="reject" class="btn btn-danger">❌ Reject</button>
      </div>
    </form>

    <a href="${pageContext.request.contextPath}/request/list" class="back">← Quay lại danh sách</a>
  </section>
</main>
</body>
</html>
