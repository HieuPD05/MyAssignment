<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang chủ - Hệ thống Quản lý Nghỉ phép</title>
    <style>
        body {
            font-family: "Segoe UI", Arial, sans-serif;
            background: #f4f7fa;
            margin: 0;
            padding: 0;
        }

        /* ===== NAVBAR ===== */
        .navbar {
            background: #007acc;
            padding: 12px 20px;
            color: #fff;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .navbar h1 {
            font-size: 20px;
            margin: 0;
        }

        .menu a {
            color: #fff;
            margin: 0 10px;
            text-decoration: none;
            font-weight: bold;
        }

        .menu a:hover {
            text-decoration: underline;
        }

        /* ===== CONTENT ===== */
        .container {
            margin: 60px auto;
            text-align: center;
            max-width: 900px;
        }

        h2 {
            color: #004a99;
            margin-bottom: 10px;
        }

        .info {
            margin-top: 10px;
            font-size: 17px;
        }

        /* ===== DASHBOARD ===== */
        .dashboard {
            margin-top: 40px;
        }

        .dash-title {
            color: #004a99;
            font-size: 20px;
            font-weight: bold;
            margin-bottom: 20px;
        }

        .dash-cards {
            display: flex;
            justify-content: center;
            gap: 30px;
            flex-wrap: wrap;
        }

        .card {
            width: 180px;
            border-radius: 12px;
            padding: 20px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
            text-align: center;
            transition: transform 0.2s;
        }

        .card:hover {
            transform: translateY(-5px);
        }

        .inprogress {
            background: #fff3cd;
            border: 2px solid #ffeb3b;
        }

        .approved {
            background: #e8f5e9;
            border: 2px solid #4caf50;
        }

        .rejected {
            background: #ffebee;
            border: 2px solid #f44336;
        }

        .card h2 {
            margin: 0;
            font-size: 36px;
        }

        .inprogress h2 { color: #ff9800; }
        .approved h2 { color: #2e7d32; }
        .rejected h2 { color: #c62828; }

        .card p {
            margin-top: 8px;
            color: #555;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <!-- NAVIGATION BAR -->
    <div class="navbar">
        <h1>🌐 Hệ thống Quản lý Nghỉ phép</h1>
        <div class="menu">
            <a href="${pageContext.request.contextPath}/home">🏠 Trang chủ</a>
            <a href="${pageContext.request.contextPath}/request/create">📝 Tạo đơn</a>
            <a href="${pageContext.request.contextPath}/request/list">📋 Danh sách</a>
            <a href="${pageContext.request.contextPath}/division/agenda">📆 Agenda</a>
            <a href="${pageContext.request.contextPath}/logout">🚪 Logout</a>
        </div>
    </div>

    <!-- MAIN CONTENT -->
    <div class="container">
        <h2>Xin chào, ${user.displayname} 👋</h2>
        <div class="info">
            <p>Bạn đang đăng nhập với vai trò:
                <c:forEach var="r" items="${user.roles}" varStatus="loop">
                    <b>${r.name}</b><c:if test="${!loop.last}">, </c:if>
                </c:forEach>
            </p>
            <p>Phòng ban: <b>${user.employee.dept.name}</b></p>
        </div>

        <p style="margin-top:25px; font-size:16px; color:#444;">
            Hãy chọn một chức năng ở thanh menu phía trên để bắt đầu làm việc.
        </p>

        <!-- DASHBOARD -->
        <div class="dashboard">
            <div class="dash-title">📊 Thống kê đơn nghỉ phép</div>
            <div class="dash-cards">
                <div class="card inprogress">
                    <h2>${inprogress}</h2>
                    <p>Đơn đang xử lý</p>
                </div>
                <div class="card approved">
                    <h2>${approved}</h2>
                    <p>Đơn đã duyệt</p>
                </div>
                <div class="card rejected">
                    <h2>${rejected}</h2>
                    <p>Đơn bị từ chối</p>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
