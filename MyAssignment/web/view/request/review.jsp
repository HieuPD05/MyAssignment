<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Duyệt đơn nghỉ phép</title>
    <style>
        body {
            font-family: "Segoe UI", Arial, sans-serif;
            background-color: #f5f7fa;
            margin: 50px;
        }
        .review-box {
            width: 420px;
            margin: 0 auto;
            background: #ffffff;
            border-radius: 12px;
            padding: 25px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            color: #004a99;
            margin-bottom: 25px;
        }
        p {
            margin: 8px 0;
            font-size: 16px;
            color: #333;
        }
        .label {
            font-weight: bold;
            color: #004a99;
        }
        textarea {
            width: 100%;
            resize: none;
            border-radius: 6px;
            padding: 6px;
            border: 1px solid #ccc;
            font-family: Arial;
        }
        .actions {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }
        button {
            width: 48%;
            border: none;
            padding: 10px;
            font-weight: bold;
            border-radius: 6px;
            cursor: pointer;
            font-size: 15px;
            color: white;
            transition: 0.3s;
        }
        .approve { background-color: #4CAF50; }
        .reject  { background-color: #E53935; }
        .approve:hover { background-color: #43A047; }
        .reject:hover { background-color: #C62828; }
        .back {
            display: block;
            text-align: center;
            margin-top: 20px;
            text-decoration: none;
            color: #007acc;
            font-weight: bold;
        }
        .back:hover { text-decoration: underline; }
    </style>
</head>
<body>

    <div class="review-box">
        <h2>📝 Duyệt đơn nghỉ phép</h2>

        <p><span class="label">Từ ngày:</span> ${request.from}</p>
        <p><span class="label">Tới ngày:</span> ${request.to}</p>
        <p><span class="label">Lý do:</span> ${request.reason}</p>

        <form action="${pageContext.request.contextPath}/request/review" method="POST">
            <input type="hidden" name="rid" value="${request.id}">

            <div class="actions">
                <button type="submit" name="action" value="approve" class="approve">✅ Approve</button>
                <button type="submit" name="action" value="reject" class="reject">❌ Reject</button>
            </div>
        </form>

        <a href="${pageContext.request.contextPath}/request/list" class="back">← Quay lại danh sách</a>
    </div>

</body>
</html>
