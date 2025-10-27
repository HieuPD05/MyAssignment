<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Trang chủ – Hệ thống Quản lý Nghỉ phép</title>
<meta name="viewport" content="width=device-width, initial-scale=1" />
<style>
:root{
  /* ===== Tông theo màu nút Đăng nhập (#3b4b63) ===== */
  --base:#3b4b63; /* màu chủ đạo (như nút) */
  --base-strong:#334257; /* đậm hơn chút cho viền/hover */
  --base-soft:#51637d; /* nhạt hơn cho khối phụ */
  --base-pale:#6a7d97; /* nhạt nữa cho icon/label */
  --ink:#ffffff; /* chữ trắng */
  --muted:#e5edf7; /* chữ phụ */
  --line:#2d394a; /* viền */
  --accent:#facc15; /* vàng nhấn */
  --accent-2:#fde047; /* vàng sáng khi hover */
  --shadow:0 16px 40px rgba(0,0,0,.28);
  --radius:18px;
}
*{box-sizing:border-box}
/* Nền chung có ảnh + phủ sáng nhẹ */
html{
  min-height:100%;
  background: linear-gradient(180deg, rgba(255,255,255,.12), rgba(255,255,255,.2)),
              url('${pageContext.request.contextPath}/img/hinhnen3.jpg?v=6') center/cover no-repeat fixed;
}
body{
  margin:0; min-height:100dvh; position:relative; font-family: Inter,"Segoe UI",Arial,sans-serif; color:var(--ink); background: transparent;
}
body::after{content:""; position:fixed; inset:0; z-index:0; backdrop-filter: blur(2px) saturate(1.05)}
.topbar,.wrap{position:relative; z-index:1}

/* ===== Topbar (màu nút) ===== */
.topbar{
  position:sticky; top:0; z-index:10; background: var(--base); backdrop-filter: blur(8px);
  border-bottom:1px solid var(--line); box-shadow: var(--shadow);
  display:flex; align-items:center; justify-content:space-between; padding:12px 18px; color:var(--ink);
}
.brand{display:flex;align-items:center;gap:12px;font-weight:800}
.logo{ width:28px;height:28px;border-radius:9px; background:linear-gradient(135deg,var(--base-pale),var(--base)); color:#fff;
  display:grid; place-items:center; font-size:13px; box-shadow:0 6px 20px rgba(0,0,0,.25); }
.menu{display:flex;gap:10px}
.menu a{
  color:#fff; text-decoration:none; background:var(--base-strong); padding:9px 14px; border-radius:12px; border:1px solid var(--line);
  transition:transform .15s, background .15s, border-color .15s, color .15s;
}
.menu a:hover{background:var(--base-soft); border-color:#40516a; transform:translateY(-1px)}
.menu .logout{color:#fee2e2; background:#991b1b; border-color:#7f1d1d}

/* ===== Layout ===== */
.wrap{max-width:1100px;margin:44px auto;padding:0 20px;display:grid;gap:18px}

/* ===== Hero / Card theo màu nút ===== */
.hero{
  background: var(--base); border:1px solid var(--line); border-radius:calc(var(--radius) + 2px);
  padding:26px; box-shadow:var(--shadow); display:flex; align-items:center; gap:20px; color:var(--ink);
}
.avatar{
  width:64px;height:64px;border-radius:16px; background:var(--base-strong); color:#fff; font-weight:800;
  display:grid; place-items:center; border:1px solid #2b3748; box-shadow:var(--shadow);
}
.hero h1{margin:0 0 6px; font-size:28px}
.hero .muted{color:var(--muted)}
.chips{display:flex; gap:8px; flex-wrap:wrap; margin-top:10px}
.chip{ padding:6px 10px; border-radius:999px; background:var(--base-soft); border:1px solid #44556f; color:#fff; font-weight:600 }

.grid{display:grid; grid-template-columns:repeat(12,1fr); gap:16px}
.card{
  grid-column:span 4; background: var(--base); border:1px solid var(--line); border-radius: var(--radius);
  padding:20px; box-shadow:var(--shadow); transition:transform .18s, box-shadow .18s, border-color .18s, background .18s;
  display:flex; gap:14px; align-items:flex-start; min-height:96px; color:var(--ink);
}
.card:hover{ transform:translateY(-3px); border-color:#51637d; background:var(--base-strong); box-shadow:0 26px 60px rgba(0,0,0,.32); }
.icon{ width:44px;height:44px;border-radius:12px; background:linear-gradient(180deg,var(--base-pale),var(--base-soft)); border:1px solid #44556f; color:#fff; display:grid; place-items:center; }
.card h3{margin:2px 0 10px; font-size:18px; letter-spacing:.2px}
.card a{display:inline-block; text-decoration:none; color:var(--accent); font-weight:700}
.card a:hover{color:var(--accent-2)}

@media (max-width: 980px){ .card{grid-column:span 6} }
@media (max-width: 640px){
  .hero{flex-direction:column; align-items:flex-start}
  .card{grid-column:span 12}
  .menu{flex-wrap:wrap; justify-content:flex-end}
}
</style>
</head>
<body>

<!-- TOPBAR -->
<header class="topbar">
  <div class="brand">
    <div class="logo">HP</div>
    <span>Hệ thống Quản lý Nghỉ phép</span>
  </div>
  <nav class="menu">
    <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
    <a href="${pageContext.request.contextPath}/request/create">Tạo đơn</a>
    <a href="${pageContext.request.contextPath}/request/list">Danh sách</a>
    <c:if test="${sessionScope.allowed['/division/agenda']}">
      <a href="${pageContext.request.contextPath}/division/agenda">Agenda</a>
    </c:if>
    <a class="logout" href="${pageContext.request.contextPath}/logout">Logout</a>
  </nav>
</header>

<!-- CONTENT -->
<main class="wrap">
  <!-- HERO -->
  <section class="hero">
    <div class="avatar">
      <span>
        <c:choose>
          <c:when test="${not empty user.displayname}">
            ${fn:substring(user.displayname,0,1)}
          </c:when>
          <c:otherwise>U</c:otherwise>
        </c:choose>
      </span>
    </div>
    <div>
      <h1>Chào mừng, ${user.displayname}</h1>
      <div class="muted">Phòng ban: <b style="color:#fff">${user.employee.dept.name}</b></div>
      <div class="chips">
        <c:forEach var="r" items="${user.roles}">
          <span class="chip">${r.name}</span>
        </c:forEach>
      </div>
    </div>
  </section>

  <!-- ACTIONS -->
  <section class="grid">
    <div class="card">
      <div class="icon">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round">
          <path d="M12 20h9"/><path d="M16.5 3.5a2.12 2.12 0 1 1 3 3L7 19l-4 1 1-4 12.5-12.5z"/>
        </svg>
      </div>
      <div>
        <h3>Tạo đơn nghỉ phép</h3>
        <a href="${pageContext.request.contextPath}/request/create">Xem ngay →</a>
      </div>
    </div>

    <div class="card">
      <div class="icon">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round">
          <path d="M3 7h5l2 3h11v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
          <path d="M3 7V5a2 2 0 0 1 2-2h3l2 2h7a2 2 0 0 1 2 2v3"/>
        </svg>
      </div>
      <div>
        <h3>Danh sách đơn</h3>
        <a href="${pageContext.request.contextPath}/request/list">Xem ngay →</a>
      </div>
    </div>

    <c:if test="${sessionScope.allowed['/division/agenda']}">
      <div class="card">
        <div class="icon">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round">
            <rect x="3" y="4" width="18" height="18" rx="2"/>
            <line x1="16" y1="2" x2="16" y2="6"/>
            <line x1="8" y1="2" x2="8" y2="6"/>
            <line x1="3" y1="10" x2="21" y2="10"/>
          </svg>
        </div>
        <div>
          <h3>Tình hình lao động</h3>
          <a href="${pageContext.request.contextPath}/division/agenda">Xem ngay →</a>
        </div>
      </div>
    </c:if>
  </section>
</main>
</body>
</html>
