<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Đăng nhập – HP</title>
  <style>
    :root { --overlay: rgba(255,255,255,.75); }
    *{box-sizing:border-box}
    body{
      margin:0;min-height:100vh;display:flex;align-items:center;justify-content:center;
      font-family:"Segoe UI",Arial,sans-serif;
      background:
        linear-gradient(135deg,#e9f5f3,#fdfcfb),
        url('${pageContext.request.contextPath}/background.jsp?v=1') center/cover no-repeat fixed; /* ✅ đúng file của bạn */
      position:relative;
    }
    body::before{content:"";position:fixed;inset:0;background:var(--overlay);pointer-events:none;}
    .card{
      width:440px;background:#fff;border-radius:14px;padding:26px 24px;
      box-shadow:0 10px 30px rgba(0,0,0,.08)
    }
    .brand{display:flex;gap:12px;align-items:center;margin-bottom:16px}
    .logo{width:40px;height:40px;border-radius:50%;display:grid;place-items:center;background:#007acc;color:#fff;font-weight:800}
    .title{font-size:22px;margin:0;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;}
    label{display:block;margin-top:12px;font-weight:600;color:#0f3554}
    input[type=text],input[type=password]{width:100%;padding:12px 14px;margin-top:6px;border:1px solid #c9d4df;border-radius:10px;font-size:14px}
    .btn{width:100%;margin-top:16px;padding:12px;border:none;border-radius:10px;background:#007acc;color:#fff;font-weight:800;cursor:pointer;transition:.2s}
    .btn:hover{background:#005fa3;transform:translateY(-1px)}
    .hint{margin-top:10px;color:#6b7986;font-size:12px}
    .error{margin-top:10px;background:#ffebee;color:#c62828;border:2px solid #f44336;border-radius:10px;padding:10px;font-weight:600}
  </style>
</head>
<body>
  <div class="card">
    <div class="brand">
      <div class="logo">eL</div>
      <h1 class="title">Đăng nhập hệ thống nghỉ phép</h1>
    </div>

    <c:if test="${not empty requestScope.message}">
      <div class="error">${requestScope.message}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post" accept-charset="UTF-8">
      <label for="txtUsername">Tên đăng nhập</label>
      <input type="text" id="txtUsername" name="username" required autofocus>

      <label for="txtPassword">Mật khẩu</label>
      <input type="password" id="txtPassword" name="password" required>

      <button type="submit" class="btn">ĐĂNG NHẬP</button>
      <div class="hint">Tài khoản mẫu: <b>mra/mrb/mrc/mrd/mre</b> – mật khẩu <b>123</b>.</div>
    </form>
  </div>
</body>
</html>
