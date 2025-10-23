<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách đơn nghỉ phép</title>
    <style>
        body {
            font-family: "Segoe UI", Arial, sans-serif;
            background: #f4f7fa;
            margin: 0;
            padding: 0;
        }

        .navbar {
            background: #007acc;
            color: white;
            padding: 12px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .navbar a {
            color: white;
            text-decoration: none;
            margin: 0 10px;
            font-weight: bold;
        }

        h2 {
            color: #004a99;
            margin: 20px;
        }

        table {
            width: 95%;
            margin: 0 auto;
            border-collapse: collapse;
            background: white;
            border-radius: 10px;
            overflow: hidden;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: center;
        }

        th {
            background: #007acc;
            color: white;
        }

        tr:nth-child(even) {
            background: #f9f9f9;
        }

        a {
            color: #004a99;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        .status-approved {
            color: #2e7d32;
            font-weight: bold;
            background: #e8f5e9;
        }

        .status-rejected {
            color: #c62828;
            font-weight: bold;
            background: #ffebee;
        }

        .status-progress {
            color: #ff9800;
            font-weight: bold;
            background: #fff8e1;
        }

        .top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 95%;
            margin: 20px auto;
        }

        .add-btn {
            background: #007acc;
            color: white;
            border: none;
            padding: 8px 14px;
            border-radius: 6px;
            cursor: pointer;
            text-decoration: none;
        }

        .add-btn:hover {
            background: #005f99;
        }

        .msg {
            width: 90%;
            margin: 15px auto;
            padding: 10px;
            border-radius: 6px;
            text-align: center;
            background: #e8f5e9;
            border: 2px solid #4caf50;
            color: #2e7d32;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <div class="navbar">
        <div><b>📋 Danh sách đơn nghỉ phép</b></div>
        <div>
            <a href="${pageContext.request.contextPath}/home">🏠 Trang chủ</a>
            <a href="${pageContext.request.contextPath}/request/create">📝 Tạo đơn</a>
            <a href="${pageContext.request.contextPath}/logout">🚪 Logout</a>
        </div>
    </div>

    <div class="top-bar">
        <h2>📁 Danh sách đơn nghỉ phép</h2>
        <a href="${pageContext.request.contextPath}/request/create" class="add-btn">+ Tạo đơn mới</a>
    </div>

    <!-- Hiển thị thông báo tạo đơn thành công -->
    <c:if test="${not empty sessionScope.message}">
        <div class="msg">${sessionScope.message}</div>
        <c:remove var="message" scope="session"/>
    </c:if>

    <table>
        <tr>
            <th>Title</th>
            <th>From</th>
            <th>To</th>
            <th>Created By</th>
            <th>Status</th>
            <th>Processed By</th>
        </tr>

        <c:forEach var="r" items="${rfls}">
            <tr>
                <td><a href="${pageContext.request.contextPath}/request/review?rid=${r.id}">${r.reason}</a></td>
                <td>${r.from}</td>
                <td>${r.to}</td>
                <td>${r.created_by.name}</td>
                <td>
                    <c:choose>
                        <c:when test="${r.status == 1}">
                            <span class="status-approved">Approved</span>
                        </c:when>
                        <c:when test="${r.status == 2}">
                            <span class="status-rejected">Rejected</span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-progress">In Progress</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:if test="${r.processed_by != null}">
                        ${r.processed_by.name}
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>

</body>
</html>
