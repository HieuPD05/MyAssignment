<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Agenda – Tình hình lao động của phòng</title>
    <style>
        body { font-family:"Segoe UI",Arial,sans-serif; background:#f2f5f9; margin:0; padding:0; }
        .navbar { background:#007acc; color:#fff; padding:12px 20px; display:flex; justify-content:space-between; align-items:center; }
        .navbar h1 { font-size:20px; margin:0; }
        .navbar .menu a { color:#fff; text-decoration:none; font-weight:bold; margin:0 10px; }
        .navbar .menu a:hover { text-decoration:underline; }
        .container { margin:40px; }
        h2 { color:#004a99; margin-bottom:25px; display:flex; align-items:center; gap:10px; }
        h2::before { content:"📅"; font-size:28px; }
        form { margin-bottom:25px; display:flex; gap:10px; align-items:center; }
        label { font-weight:bold; color:#003366; }
        input[type="date"], button { padding:8px 10px; border-radius:6px; border:1px solid #aaa; font-size:14px; }
        button { background:#007acc; color:#fff; border:none; font-weight:bold; cursor:pointer; transition:background .3s; }
        button:hover { background:#005fa3; }
        table { width:100%; border-collapse:collapse; background:#fff; box-shadow:0 3px 8px rgba(0,0,0,0.15); }
        th, td { border:2px solid #999; text-align:center; padding:10px; font-size:15px; }
        th { background:#0066cc; color:#fff; text-transform:uppercase; }
        td:first-child { font-weight:bold; background:#f8f9fa; color:#003366; }
        .work { background:#81c784; }
        .off { background:#e53935; color:#fff; }
        td.work:hover { background:#66bb6a; }
        td.off:hover { background:#c62828; }
        .empty { text-align:center; padding:40px; color:#666; background:#fff; border:2px dashed #ccc; border-radius:10px; }
    </style>
</head>
<body>

    <div class="navbar">
        <h1>📆 Agenda - Tình hình lao động</h1>
        <div class="menu">
            <a href="${pageContext.request.contextPath}/home">🏠 Trang chủ</a>

            <c:if test="${sessionScope.allowed['/request/list']}">
                <a href="${pageContext.request.contextPath}/request/list">📋 Danh sách</a>
            </c:if>
            <c:if test="${sessionScope.allowed['/request/create']}">
                <a href="${pageContext.request.contextPath}/request/create">📝 Tạo đơn</a>
            </c:if>

            <c:if test="${sessionScope.allowed['/division/agenda']}">
                <a href="${pageContext.request.contextPath}/division/agenda">📆 Agenda</a>
            </c:if>

            <a href="${pageContext.request.contextPath}/logout">🚪 Logout</a>
        </div>
    </div>

    <div class="container">
        <h2>Agenda – Tình hình lao động của phòng</h2>

        <form action="${pageContext.request.contextPath}/division/agenda" method="GET">
            <label>Từ ngày:</label>
            <input type="date" name="from" value="${fromStr}">
            <label>Đến ngày:</label>
            <input type="date" name="to" value="${toStr}">
            <button type="submit">Xem</button>
        </form>

        <c:choose>
            <c:when test="${not empty employees}">
                <table>
                    <tr>
                        <th>Nhân sự</th>
                        <c:forEach var="d" items="${dates}">
                            <th>${d}</th>
                        </c:forEach>
                    </tr>

                    <c:forEach var="e" items="${employees}">
                        <tr>
                            <td>${e.name}</td>
                            <c:forEach var="d" items="${dates}">
                                <c:set var="off" value="${agenda[e.id][d]}"/>
                                <td class="${off ? 'off' : 'work'}"></td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty">
                    Không có dữ liệu để hiển thị.<br/>Vui lòng chọn khoảng thời gian khác.
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</body>
</html>
