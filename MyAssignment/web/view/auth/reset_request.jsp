<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/core"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Quên mật khẩu</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 24px; }
    .wrap { max-width: 480px; margin: auto; }
    .msg { padding:10px; margin:10px 0; border-radius:6px; }
    .err { background:#ffecec; color:#b10000; border:1px solid #ffbcbc; }
    .ok { background:#e9fff0; color:#0b6b2f; border:1px solid #b6f0c8; }
    label { display:block; margin-top:10px; }
    input[type=text], input[type=number] { width:100%; padding:8px; }
    button { margin-top:12px; padding:8px 16px; }
    code { background:#f6f6f6; padding:3px 6px; border-radius:4px; }
  </style>
</head>
<body>
  <jsp:include page="/view/common/bell.jspf"/>

  <div class="wrap">
    <h2>Yêu cầu đặt lại mật khẩu</h2>

    <c:if test="${not empty error}">
      <div class="msg err">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
      <div class="msg ok">${success}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/auth/reset/request">
      <label>Username / Email</label>
      <input type="text" name="username" required placeholder="vd: mrB1">

      <label>Thời hạn token (phút) – demo</label>
      <input type="number" name="ttl" min="5" max="120" value="30">

      <button type="submit">Tạo token</button>
    </form>

    <c:if test="${not empty resetLink}">
      <p>Link đặt lại (demo hiển thị trực tiếp):</p>
      <p><a href="${resetLink}"><code>${resetLink}</code></a></p>
    </c:if>

    <p style="margin-top:16px;">
      <a href="${pageContext.request.contextPath}/login">← Quay lại đăng nhập</a>
    </p>
  </div>
</body>
</html>
