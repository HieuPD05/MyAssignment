<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Tạo đơn nghỉ</title>
<style>
.container{max-width:880px;margin:18px auto;padding:0 14px}
h1{margin:14px 0}
form{background:#fff;border:1px solid #e5e7eb;border-radius:10px;padding:16px}
.row{display:grid;grid-template-columns:160px 1fr;gap:10px;align-items:center;margin:8px 0}
input,select,textarea{padding:10px;border:1px solid #cbd5e1;border-radius:8px}
.btn{padding:10px 14px;border:none;border-radius:8px;background:#1f2937;color:#fff;cursor:pointer}
.error{color:#dc2626;margin:8px 0 0 0}
.hint{color:#6b7280;font-size:13px}
</style>
</head>
<body>
<jsp:include page="/view/common/topbar.jspf"/>
<div class="container">
  <h1>Tạo đơn nghỉ</h1>
  <form method="post" action="${pageContext.request.contextPath}/request/create">
    <div class="row">
      <label>Từ ngày</label>
      <input type="date" name="from" value="${stick_from != null ? stick_from : ''}" required>
    </div>
    <div class="row">
      <label>Đến ngày</label>
      <input type="date" name="to" value="${stick_to != null ? stick_to : ''}" required>
    </div>
    <div class="row">
      <label>Loại nghỉ</label>
      <select name="ltid" required>
        <option value="" disabled ${empty stick_ltid ? 'selected' : ''}>-- chọn --</option>
        <option value="1" ${stick_ltid=='1'?'selected':''}>Nghỉ phép năm (ANNUAL)</option>
        <option value="2" ${stick_ltid=='2'?'selected':''}>Nghỉ không lương (UNPAID)</option>
        <option value="3" ${stick_ltid=='3'?'selected':''}>Nghỉ thai sản (MATERNITY)</option>
        <option value="4" ${stick_ltid=='4'?'selected':''}>Khác (OTHER)</option>
      </select>
    </div>
    <div class="row">
      <label>Lý do</label>
      <textarea name="reason" rows="3">${stick_reason}</textarea>
    </div>
    <div class="row"><label></label>
      <button class="btn" type="submit">Gửi đơn</button>
    </div>
    <div class="row"><label></label>
      <div class="hint">Lưu ý: Nếu xin nghỉ trong **hôm nay**, phải tạo trước 08:00.</div>
    </div>
    <c:if test="${not empty error}">
      <div class="row"><label></label><div class="error">${error}</div></div>
    </c:if>
  </form>
</div>
</body>
</html>
