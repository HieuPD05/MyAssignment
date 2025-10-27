<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Agenda – Tình hình lao động của phòng</title>
  <style>
    :root{
      --base:#3b4b63; --base-strong:#334257; --base-soft:#51637d;
      --ink:#ffffff; --muted:#e5edf7; --line:#2d394a;
      --shadow:0 16px 40px rgba(0,0,0,.28); --radius:18px;
      --ok:#22c55e; --bad:#ef4444;
    }
    html{
      min-height:100%;
      background: linear-gradient(180deg, rgba(255,255,255,.12), rgba(255,255,255,.2)),
                  url('${pageContext.request.contextPath}/img/hinhnen3.jpg') center/cover no-repeat fixed;
    }
    body{margin:0;min-height:100dvh;font-family:Inter,"Segoe UI",Arial,sans-serif;color:var(--ink)}
    body::after{content:"";position:fixed;inset:0;backdrop-filter:blur(2px) saturate(1.05);z-index:0}

    .topbar{position:sticky;top:0;z-index:10;background:var(--base);border-bottom:1px solid var(--line);
      box-shadow:var(--shadow);display:flex;align-items:center;justify-content:space-between;padding:10px 16px}
    .brand{display:flex;gap:10px;align-items:center;font-weight:800}
    .logo{width:28px;height:28px;border-radius:9px;background:linear-gradient(135deg,#6a7d97,var(--base)); display:grid;place-items:center}
    .menu{display:flex;gap:10px}
    .menu a{color:#fff;text-decoration:none;background:var(--base-strong);padding:8px 12px;border-radius:12px;border:1px solid var(--line)}
    .menu a:hover{background:var(--base-soft)}

    .wrap{position:relative;z-index:1;max-width:1200px;margin:22px auto;padding:0 16px}
    .panel{background:var(--base);border:1px solid var(--line);border-radius:var(--radius);box-shadow:var(--shadow);padding:16px}
    h2{margin:0 0 12px}

    form.filters{display:flex;gap:10px;align-items:center;margin-bottom:12px;flex-wrap:wrap}
    label{font-weight:800}
    .input{padding:8px 10px;border-radius:10px;background:var(--base-strong);color:#fff;border:1px solid var(--line)}
    .btn{background:var(--base);border:1px solid var(--line);color:#fff;padding:8px 12px;border-radius:10px;font-weight:800;cursor:pointer}
    .btn:hover{background:var(--base-soft)}

    /* ====== XỬ LÝ TRÀN: bọc bảng để CUỘN NGANG ====== */
    .table-wrap{
      overflow-x:auto; overflow-y:hidden;
      -webkit-overflow-scrolling: touch;
      padding-bottom:8px;
      border-radius:12px;
      box-shadow: inset 0 -6px 10px rgba(0,0,0,.08);
    }

    table{
      width:100%;
      border-collapse:separate;
      border-spacing:6px 10px;
      color:#fff;
      table-layout: fixed;           /* giúp cột có width cố định */
      white-space:nowrap;            /* không xuống dòng tiêu đề ngày */
      min-width: 900px;              /* đảm bảo có không gian tối thiểu */
    }

    thead th{
      padding:6px 6px; text-align:center; font-size:12px; color:#dbe3f3;
      position: sticky; top:0; z-index:3; background:var(--base); /* header dính khi cuộn dọc */
    }

    tbody tr{background:var(--base-strong);border:1px solid var(--line);box-shadow:var(--shadow);border-radius:12px}
    tbody td{padding:6px; text-align:center; height:34px; border-radius:8px}

    /* ====== GHIM CỘT “NHÂN SỰ” BÊN TRÁI ====== */
    th.sticky-col, td.sticky-col{
      position: sticky; left:0; z-index:4;
      background:var(--base);
      box-shadow: 4px 0 8px rgba(0,0,0,.15); /* viền đổ bóng để dễ nhìn khi cuộn */
    }
    /* Ô dữ liệu ở cột đầu dùng nền cùng hàng */
    td.sticky-col{
      background:var(--base-strong);
      font-weight:800; text-align:left; padding-left:10px;
    }

    /* Kích thước cố định cho cột ngày (nhỏ gọn) */
    .col-day{ width:50px; min-width:50px; max-width:50px; }
    .col-name{ width:180px; min-width:180px; max-width:180px; } /* cột Nhân sự */

    /* === MÀU Ô LỊCH === */
    td.work{
      background: linear-gradient(180deg, var(--ok) 0%, #1ea851 100%);
      border:1px solid #178a41;
      box-shadow: inset 0 1px 0 rgba(255,255,255,.12);
    }
    td.off{
      background: linear-gradient(180deg, var(--bad) 0%, #d93c3c 100%);
      border:1px solid #b72f2f;
      box-shadow: inset 0 1px 0 rgba(255,255,255,.12);
    }
    tbody td.work:hover, tbody td.off:hover{ filter: brightness(1.04); }

    .empty{padding:30px;text-align:center;border:1px dashed var(--line);border-radius:12px;background:var(--base-strong)}
  </style>
</head>
<body>
<header class="topbar">
  <div class="brand"><div class="logo">HP</div>Hệ thống Quản lý Nghỉ phép</div>
  <nav class="menu">
    <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
    <c:if test="${sessionScope.allowed['/request/list']}">
      <a href="${pageContext.request.contextPath}/request/list">Danh sách</a>
    </c:if>
    <c:if test="${sessionScope.allowed['/request/create']}">
      <a href="${pageContext.request.contextPath}/request/create">Tạo đơn</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
  </nav>
</header>

<main class="wrap">
  <section class="panel">
    <h2>📆 Agenda – Tình hình lao động của phòng</h2>

    <form class="filters" action="${pageContext.request.contextPath}/division/agenda" method="GET">
      <label>Từ ngày:</label>
      <input class="input" type="date" name="from" value="${fromStr}">
      <label>Đến ngày:</label>
      <input class="input" type="date" name="to" value="${toStr}">
      <button class="btn" type="submit">Xem</button>
    </form>

    <c:choose>
      <c:when test="${not empty employees}">
        <div class="table-wrap">
          <table>
            <thead>
              <tr>
                <th class="sticky-col col-name">Nhân sự</th>
                <c:forEach var="d" items="${dates}">
                  <th class="col-day">${d}</th>
                </c:forEach>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="e" items="${employees}">
                <tr>
                  <td class="sticky-col col-name">${e.name}</td>
                  <c:forEach var="d" items="${dates}">
                    <c:set var="off" value="${agenda[e.id][d]}"/>
                    <td class="${off ? 'off' : 'work'} col-day"></td>
                  </c:forEach>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </c:when>
      <c:otherwise>
        <div class="empty">
          Không có dữ liệu để hiển thị.<br/>Vui lòng chọn khoảng thời gian khác.
        </div>
      </c:otherwise>
    </c:choose>
  </section>
</main>
</body>
</html>
