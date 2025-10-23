<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tạo đơn nghỉ phép</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f5f7fa; margin: 40px; }
        .form-container {
            width: 400px; margin: 0 auto; background: #fff;
            padding: 25px; border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 { color: #004a99; text-align: center; margin-bottom: 20px; }
        label { font-weight: bold; display: block; margin-top: 10px; }
        input, textarea, button {
            width: 100%; padding: 8px; margin-top: 5px;
            border: 1px solid #ccc; border-radius: 5px;
        }
        textarea { resize: none; }
        button {
            background: #007acc; color: white; font-weight: bold;
            margin-top: 15px; cursor: pointer; transition: 0.3s;
        }
        button:hover { background: #005fa3; }
        .error { color: red; text-align: center; margin-top: 10px; }
    </style>
</head>
<body>
    <div class="form-container">
        <h2>📝 Đơn xin nghỉ phép</h2>
        <form action="create" method="POST" accept-charset="UTF-8">
            <label>Từ ngày:</label>
            <input type="date" name="from" required>

            <label>Tới ngày:</label>
            <input type="date" name="to" required>

            <label>Lý do:</label>
            <textarea name="reason" rows="4" placeholder="Nhập lý do nghỉ..." required></textarea>

            <button type="submit">Gửi yêu cầu</button>
        </form>
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
    </div>
</body>
</html>
