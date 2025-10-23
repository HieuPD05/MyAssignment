<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>📋 Danh sách đơn nghỉ phép</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f7fa;
            margin: 30px;
        }
        h2 {
            color: #003366;
            text-align: left;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        h2::before {
            content: "🗂️";
            font-size: 28px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background: #fff;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: center;
        }
        th {
            background-color: #007acc;
            color: white;
            text-transform: uppercase;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        a {
            color: #004a99;
            text-decoration: none;
            font-weight: bold;
        }
        a:hover {
            text-decoration: underline;
            color: #0066cc;
        }
        .status {
            font-weight: bold;
            border-radius: 5px;
            padding: 4px 10px;
            display: inline-block;
            min-width: 90px;
        }
        .status.inprogress { color: #ff9800; background: #fff3cd; }
        .status.approved  { color: #2e7d32; background: #e8f5e9; }
        .status.rejected  { color: #c62828; background: #ffebee; }
        .topbar {
            margin-bottom: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .btn {
            background: #007acc;
            color: white;
            padding: 8px 14px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: bold;
            transition: 0.3s;
        }
        .btn:hover { background: #005fa3; }
    </style>
</head>
<body>

    <div class="topbar">
        <h2>Danh sách đơn nghỉ phép</h2>
        <a href="${pageContext.request.contextPath}/request/create" class="btn">+ Tạo đơn mới</a>
    </div>

    <table>
        <tr>
            <th>Title</th>
            <th>From</th>
            <th>To</th>
            <th>Created By</th>
            <th>Status</th>
            <th>Processed By</th>
        </tr>

        <c:forEach var="r" items="${requests}">
            <tr>
                <td>
                    <a href="${pageContext.request.contextPath}/request/review?rid=${r.id}">
                        ${r.reason}
                    </a>
                </td>
                <td>${r.from}</td>
                <td>${r.to}</td>
                <td>${r.created_by.name}</td>
                <td>
                    <c:choose>
                        <c:when test="${r.status == 0}">
                            <span class="status inprogress">In Progress</span>
                        </c:when>
                        <c:when test="${r.status == 1}">
                            <span class="status approved">Approved</span>
                        </c:when>
                        <c:when test="${r.status == 2}">
                            <span class="status rejected">Rejected</span>
                        </c:when>
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
