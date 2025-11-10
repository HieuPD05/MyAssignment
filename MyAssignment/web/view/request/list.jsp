<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Danh sách đơn nghỉ</title>
<style>
.container{max-width:1100px;margin:18px auto;padding:0 14px}
h1{margin:14px 0}
.filter{display:flex;gap:8px;align-items:center;margin:10px 0}
input,select{padding:8px;border:1px solid #cbd5e1;border-radius:8px}
table{width:100%;border-collapse:collapse;background:#fff;border:1px solid #e5e7eb}
th,td{padding:10px;border-bottom:1px solid #e5e7eb;text-align:left;vertical-align:middle}
.badge{padding:4px 8px;border-radius:10px;font-weight:600}
.s0{background:#fde68a} .s1{background:#86efac} .s2{background:#fca5a5} .s3{background:#c7d2fe}
.actions{display:flex;gap:6px}
button{padding:6px 10px;border:none;border-radius:8px;cursor:pointer}
.btn-approve{background:#16a34a;color:#fff}
.btn-reject{background:#dc2626;color:#fff}
.pager{margin-top:10px;display:flex;gap:6px}
.pager a,.pager span{padding:6px 10px;border:1px solid #e5e7eb;border-radius:8px;text-decoration:none}
.active{background:#1f2937;color:#fff;border-color:#1f2937}
.msg{margin:8px 0}
</style>
</head>
<body>
<jsp:include page="/view/common/topbar.jsp"/>

<div class="container">
  <h1>Danh sách đơn nghỉ</h1>

  <c:if test="${not empty param.msg}">
    <div class="msg">✅ ${param.msg}</div>
  </c:if>

  <form class="filter" method="get" action="${pageContext.request.contextPath}/request/list">
    <input type="text" name="name" placeholder="Lọc theo tên" value="${name}">
    <select name="status">
      <option value=""  <c:if test="${empty status}">selected</c:if>>— Tất cả —</option>
      <option value="0" <c:if test="${status eq '0'}">selected</c:if>>Inprogress</option>
      <option value="1" <c:if test="${status eq '1'}">selected</c:if>>Approved</option>
      <option value="2" <c:if test="${status eq '2'}">selected</c:if>>Rejected</option>
      <option value="3" <c:if test="${status eq '3'}">selected</c:if>>Canceled</option>
    </select>
    <button type="submit">Lọc</button>
  </form>

  <table>
    <thead>
      <tr>
        <th>RID</th><th>Người tạo</th><th>Từ</th><th>Đến</th>
        <th>Loại nghỉ</th><th>Lý do</th><th>Trạng thái</th><th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${list}" var="r">
        <tr>
          <td>${r.rid}</td>
          <td>${r.createdBy}</td>
          <td>${r.from}</td>
          <td>${r.to}</td>
          <td>${r.ltid}</td>
          <td>${r.reason}</td>
          <td>
            <c:choose>
              <c:when test="${r.status==0}"><span class="badge s0">Inprogress</span></c:when>
              <c:when test="${r.status==1}"><span class="badge s1">Approved</span></c:when>
              <c:when test="${r.status==2}"><span class="badge s2">Rejected</span></c:when>
              <c:otherwise><span class="badge s3">Canceled</span></c:otherwise>
            </c:choose>
          </td>
          <td>
            <div class="actions">
              <c:if test="${r.status==0}">
                <form method="post" action="${pageContext.request.contextPath}/request/review">
                  <input type="hidden" name="rid" value="${r.rid}">
                  <input type="hidden" name="action" value="approve">
                  <button class="btn-approve" type="submit">Approve</button>
                </form>
                <form method="post" action="${pageContext.request.contextPath}/request/review">
                  <input type="hidden" name="rid" value="${r.rid}">
                  <input type="hidden" name="action" value="reject">
                  <button class="btn-reject" type="submit">Reject</button>
                </form>
              </c:if>
            </div>
          </td>
        </tr>
      </c:forEach>
      <c:if test="${empty list}">
        <tr><td colspan="8">Không có dữ liệu.</td></tr>
      </c:if>
    </tbody>
  </table>

  <div class="pager">
    <c:forEach begin="1" end="${pages}" var="p">
      <c:choose>
        <c:when test="${p==page}"><span class="active">${p}</span></c:when>
        <c:otherwise>
          <a href="${pageContext.request.contextPath}/request/list?page=${p}&status=${status}&name=${name}">${p}</a>
        </c:otherwise>
      </c:choose>
    </c:forEach>
  </div>
</div>
</body>
</html>
