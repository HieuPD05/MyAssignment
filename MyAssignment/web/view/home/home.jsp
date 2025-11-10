<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/core" %>
<%@taglib uri="http://jakarta.ee/tags/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Trang chủ – Hệ thống Quản lý Nghỉ phép</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <style>
            :root{
                --base:#3b4b63;
                --base-strong:#334257;
                --base-soft:#51637d;
                --base-pale:#6a7d97;
                --ink:#ffffff;
                --muted:#e5edf7;
                --line:#2d394a;
                --accent:#facc15;
                --accent-2:#fde047;
                --shadow:0 16px 40px rgba(0,0,0,.28);
                --radius:18px;
            }
            *{
                box-sizing:border-box
            }
            html{
                min-height:100%;
                background: linear-gradient(180deg, rgba(255,255,255,.12), rgba(255,255,255,.2)),
                    url('${pageContext.request.contextPath}/img/hinhnen3.jpg?v=6') center/cover no-repeat fixed;
            }
            body{
                margin:0;
                min-height:100dvh;
                position:relative;
                font-family: Inter,"Segoe UI",Arial,sans-serif;
                color:var(--ink);
            }
            body::after{
                content:"";
                position:fixed;
                inset:0;
                z-index:0;
                backdrop-filter: blur(2px) saturate(1.05)
            }
            .topbar,.wrap{
                position:relative;
                z-index:1
            }
            .topbar{
                position:sticky;
                top:0;
                z-index:10;
                background: var(--base);
                border-bottom:1px solid var(--line);
                box-shadow: var(--shadow);
                display:flex;
                align-items:center;
                justify-content:space-between;
                padding:12px 18px;
                color:var(--ink);
            }
            .brand{
                display:flex;
                align-items:center;
                gap:12px;
                font-weight:800
            }
            .logo{
                width:28px;
                height:28px;
                border-radius:9px;
                background:linear-gradient(135deg,var(--base-pale),var(--base));
                display:grid;
                place-items:center;
                color:#fff;
            }
            .menu{
                display:flex;
                gap:10px
            }
            .menu a{
                color:#fff;
                text-decoration:none;
                background:var(--base-strong);
                padding:9px 14px;
                border-radius:12px;
                border:1px solid var(--line);
            }
            .menu a:hover{
                background:var(--base-soft)
            }
            .menu .logout{
                background:#991b1b;
                border-color:#7f1d1d
            }
            .wrap{
                max-width:1100px;
                margin:44px auto;
                padding:0 20px;
                display:grid;
                gap:18px
            }
            .hero{
                background: var(--base);
                border:1px solid var(--line);
                border-radius:calc(var(--radius) + 2px);
                padding:26px;
                box-shadow:var(--shadow);
                display:flex;
                align-items:center;
                gap:20px;
            }
            .avatar{
                width:64px;
                height:64px;
                border-radius:16px;
                background:var(--base-strong);
                display:grid;
                place-items:center;
                border:1px solid #2b3748;
            }
            .hero h1{
                margin:0 0 6px;
                font-size:28px
            }
            .hero .muted{
                color:var(--muted)
            }
            .chips{
                display:flex;
                gap:8px;
                flex-wrap:wrap;
                margin-top:10px
            }
            .chip{
                padding:6px 10px;
                border-radius:999px;
                background:var(--base-soft);
                border:1px solid #44556f;
                color:#fff;
                font-weight:600
            }
            .grid{
                display:grid;
                grid-template-columns:repeat(12,1fr);
                gap:16px
            }
            .card{
                grid-column:span 4;
                background: var(--base);
                border:1px solid var(--line);
                border-radius: var(--radius);
                padding:20px;
                box-shadow:var(--shadow);
                display:flex;
                gap:14px;
                align-items:flex-start;
                min-height:96px;
            }
            .card:hover{
                transform:translateY(-3px);
                border-color:#51637d;
                background:var(--base-strong);
            }
            .icon{
                width:44px;
                height:44px;
                border-radius:12px;
                background:linear-gradient(180deg,var(--base-pale),var(--base-soft));
                border:1px solid #44556f;
                display:grid;
                place-items:center;
            }
            .card h3{
                margin:2px 0 10px;
                font-size:18px
            }
            .card a{
                text-decoration:none;
                color:#fde68a;
                font-weight:700
            }
            .card a:hover{
                color:#fff
            }
            @media (max-width: 980px){
                .card{
                    grid-column:span 6
                }
            }
            @media (max-width: 640px){
                .hero{
                    flex-direction:column;
                    align-items:flex-start
                }
                .card{
                    grid-column:span 12
                }
                .menu{
                    flex-wrap:wrap
                }
            }
        </style>
    </head>
    <body>
        <header class="topbar">
            <div class="brand"><div class="logo">HP</div><span>Hệ thống Quản lý Nghỉ phép</span></div>
            <nav class="menu">
                <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                <a href="${pageContext.request.contextPath}/request/create">Tạo đơn</a>
                <a href="${pageContext.request.contextPath}/request/list">Danh sách</a>
                <c:if test="${sessionScope.allowed['/division/agenda']}"><a href="${pageContext.request.contextPath}/division/agenda">Agenda</a></c:if>
                <c:if test="${sessionScope.allowed['/hr/employee']}"><a href="${pageContext.request.contextPath}/hr/employee">HR</a></c:if>
                <c:if test="${sessionScope.allowed['/admin/audit']}"><a href="${pageContext.request.contextPath}/admin/audit">Audit</a></c:if>
                <a class="logout" href="${pageContext.request.contextPath}/logout">Logout</a>
            </nav>
        </header>

        <main class="wrap">
            <section class="hero">
                <div class="avatar">
                    <span style="color:#fff;font-weight:800">
                        <c:choose><c:when test="${not empty user.displayname}">${fn:substring(user.displayname,0,1)}</c:when><c:otherwise>U</c:otherwise></c:choose>
                            </span>
                        </div>
                        <div>
                                <h1>Chào mừng, ${user.displayname}</h1>
                    <div class="muted">Phòng ban: <b style="color:#fff">${user.employee.dept.name}</b></div>
                    <div class="chips">
                        <c:forEach var="r" items="${user.roles}"><span class="chip">${r.name}</span></c:forEach>
                        </div>
                    </div>
                </section>

                <section class="grid">
                    <div class="card">
                        <div class="icon">📝</div><div><h3>Tạo đơn nghỉ phép</h3><a href="${pageContext.request.contextPath}/request/create">Xem ngay →</a></div>
                </div>
                <div class="card">
                    <div class="icon">📁</div><div><h3>Danh sách đơn</h3><a href="${pageContext.request.contextPath}/request/list">Xem ngay →</a></div>
                </div>
                <c:if test="${sessionScope.allowed['/division/agenda']}">
                    <div class="card">
                        <div class="icon">📆</div><div><h3>Tình hình lao động</h3><a href="${pageContext.request.contextPath}/division/agenda">Xem ngay →</a></div>
                    </div>
                </c:if>
                <c:if test="${sessionScope.allowed['/hr/employee']}">
                    <div class="card">
                        <div class="icon">👥</div><div><h3>HR – Nhân sự</h3><a href="${pageContext.request.contextPath}/hr/employee">Xem ngay →</a></div>
                    </div>
                </c:if>
                <c:if test="${sessionScope.allowed['/admin/audit']}">
                    <div class="card">
                        <div class="icon">🔍</div><div><h3>Nhật ký hệ thống</h3><a href="${pageContext.request.contextPath}/admin/audit">Xem ngay →</a></div>
                    </div>
                </c:if>
            </section>
        </main>
    </body>
</html>
