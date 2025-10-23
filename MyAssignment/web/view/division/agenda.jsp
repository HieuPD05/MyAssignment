<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tình hình lao động</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f7fa;
            margin: 30px;
        }
        h2 {
            color: #004a99;
            margin-bottom: 20px;
        }
        form {
            margin-bottom: 20px;
            display: flex;
            gap: 10px;
            align-items: center;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background: #fff;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        th, td {
            border: 1px solid #ddd;
            text-align: center;
            padding: 8px;
        }
        th {
            background: #007acc;
            color: white;
        }
        .work { background: #c8e6c9; }   /* xanh: đi làm */
        .off  { background: #ffcdd2; }   /* đỏ: nghỉ phép */
    </style>
</head>
<body>

    <h2>📅 Agenda – Tình hình lao động của phòng</h2>

    <form action="${pageContext.request.contextPath}/division/agenda" method="GET">
        <label>Từ ngày:</label>
        <input type="date" name="from" value="${from}">
        <label>Đến ngày:</label>
        <input type="date" name="to" value="${to}">
        <button type="submit">Xem</button>
    </form>

    <table>
        <tr>
            <th>Nhân sự</th>
            <c:forEach var="d" items="${agenda[employees.iterator().next().id].keySet()}">
                <th>${d}</th>
            </c:forEach>
        </tr>

        <c:forEach var="e" items="${employees}">
            <tr>
                <td><b>${e.name}</b></td>
                <c:forEach var="entry" items="${agenda[e.id].entrySet()}">
                    <td class="${entry.value ? 'off' : 'work'}"></td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>

</body>
</html>
