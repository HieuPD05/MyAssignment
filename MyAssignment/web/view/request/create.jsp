<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tạo đơn nghỉ phép</title>
    <style>
        body { font-family:"Segoe UI",Arial,sans-serif; background:#f4f7fa; margin:0; padding:0; }
        .navbar { background:#007acc; color:#fff; padding:12px 20px; display:flex; justify-content:space-between; }
        .navbar a { color:#fff; text-decoration:none; margin:0 10px; font-weight:bold; }
        .container { width:450px; margin:60px auto; background:#fff; padding:25px; border-radius:12px; box-shadow:0 2px 8px rgba(0,0,0,0.1); }
        h2 { color:#004a99; text-align:center; }
        label { font-weight:bold; color:#333; }
        input[type="date"], textarea { width:100%; padding:8px; margin-top:5px; margin-bottom:15px; border:1px solid #ccc; border-radius:6px; }
        textarea { resize:none; }
        input[type="submit"] { background:#007acc; color:#fff; border:none; padding:10px 15px; border-radius:6px; font-size:16px; cursor:pointer; }
        input[type="submit"]:hover { background:#005f99; }
        .error { background:#ffebee; color:#c62828; border:2px solid #f44336; padding:10px; border-radius:6px; margin-bottom:15px; }
    </style>
</head>
<body>

    <div class="navbar">
        <div><b>📝 Tạo đơn nghỉ phép</b></div>
        <div>
            <a href="${pageContext.request.contextPath}/home">🏠 Trang chủ</a>

            <c:if test="${sessionScope.allowed['/request/list']}">
                <a href="${pageContext.request.contextPath}/request/list">📋 Danh sách</a>
            </c:if>

            <c:if test="${sessionScope.allowed['/division/agenda']}">
                <a href="${pageContext.request.contextPath}/division/agenda">📆 Agenda</a>
            </c:if>

            <a href="${pageContext.request.contextPath}/logout">🚪 Logout</a>
        </div>
    </div>

    <div class="container">
        <h2>Đơn xin nghỉ phép</h2>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/request/create" method="post">
            <label>Từ ngày:</label>
            <input type="date" name="from" required>

            <label>Đến ngày:</label>
            <input type="date" name="to" required>

            <label>Lý do nghỉ phép:</label>
            <textarea name="reason" rows="4" placeholder="Nhập lý do nghỉ phép..." required></textarea>

            <div style="text-align:center;">
                <input type="submit" value="Gửi đơn">
            </div>
        </form>
    </div>
</body>
</html>
