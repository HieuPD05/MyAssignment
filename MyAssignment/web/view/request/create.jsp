<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Tạo đơn nghỉ phép</title>
  <style>
    :root { --base:#3b4b63; --base-strong:#334257; --base-soft:#51637d; --ink:#ffffff; --line:#2d394a; --shadow:0 8px 20px rgba(0,0,0,.25); }
    /* Thu gọn: không cần chiếm full 100vh nữa, thêm padding top/bottom vừa phải */
    body {
      margin:0; font-family:Inter,"Segoe UI",Arial,sans-serif;
      min-height:100vh;
      display:flex; justify-content:center; align-items:flex-start;
      padding:36px 0;   /* ↓ thu nhỏ vùng trống trên/dưới */
      background: linear-gradient(180deg, rgba(255,255,255,.12), rgba(255,255,255,.2)),
                  url('${pageContext.request.contextPath}/img/hinhnen3.jpg') center/cover no-repeat fixed;
    }
    body::after{content:"";position:fixed;inset:0;backdrop-filter:blur(2px) saturate(1.05);z-index:0}

    .form-box {
      position:relative; z-index:1;
      width:440px; max-width:92vw;                   /* ↓ hẹp hơn */
      background:var(--base); border:1px solid var(--line);
      border-radius:16px; padding:20px;              /* ↓ ít padding hơn */
      box-shadow:var(--shadow);
    }

    h2 { margin:0 0 12px; color:#fff; font-size:20px;  /* ↓ nhỏ hơn */
         display:flex; align-items:center; gap:8px; justify-content:center; }

    label { display:block; margin-top:10px; margin-bottom:6px; font-weight:600; color:#fff; }
    .input, textarea {
      width:100%; padding:10px 12px;                 /* ↓ thấp hơn */
      border-radius:10px; border:1px solid var(--line);
      background:var(--base-strong); color:#fff; font-size:14px; outline:none; box-sizing:border-box;
    }
    .input:focus, textarea:focus { border-color:#51637d; box-shadow:0 0 0 3px rgba(81,99,125,.35); }
    textarea { resize:vertical; min-height:90px }    /* ↓ thấp hơn */

    .actions{ display:flex; gap:10px; margin-top:16px; }
    .btn{ flex:1; padding:10px; border:none; border-radius:10px; font-weight:700; cursor:pointer; font-size:15px; transition:all .25s; }
    .btn-primary{ background:#1e293b; color:#fff; }
    .btn-primary:hover{ background:#2d3e59; transform:translateY(-1px); }
    .btn-cancel{ background:#4b5563; color:#fff; }
    .btn-cancel:hover{ background:#374151; transform:translateY(-1px); }

    .error {
      background:rgba(239,68,68,.15);
      border:1px solid rgba(239,68,68,.4);
      color:#fee2e2; padding:10px; border-radius:10px; margin-bottom:10px;
      font-size:14px;
    }

    input[type="date"]::-webkit-calendar-picker-indicator { filter: invert(1); opacity: 1; display: block; cursor: pointer; }
    .disabled { opacity:.9; }
  </style>
</head>
<body>
<div class="form-box">
  <h2>📝 Đơn xin nghỉ phép</h2>

  <c:if test="${not empty error}">
    <div class="error">${error}</div>
  </c:if>

  <form action="${pageContext.request.contextPath}/request/create" method="post" class="${permError ? 'disabled' : ''}">
    <label>Loại đơn:</label>
    <input class="input" type="text" name="rtype" placeholder="VD: Nghỉ phép / Nghỉ ốm / Làm việc từ xa..." ${permError ? 'disabled' : ''} required />

    <label>Từ ngày:</label>
    <input class="input" type="date" name="from" ${permError ? 'disabled' : ''} required>

    <label>Đến ngày:</label>
    <input class="input" type="date" name="to" ${permError ? 'disabled' : ''} required>

    <label>Lý do nghỉ phép:</label>
    <textarea name="reason" placeholder="Nhập lý do nghỉ phép..." ${permError ? 'disabled' : ''} required></textarea>

    <div class="actions">
      <button type="button" class="btn btn-cancel" onclick="history.back()" ${permError ? '' : ''}>Hủy</button>
      <button type="submit" class="btn btn-primary" ${permError ? 'disabled' : ''}>Gửi đơn</button>
    </div>
  </form>
</div>
</body>
</html>
