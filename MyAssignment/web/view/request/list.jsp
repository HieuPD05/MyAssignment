<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Danh sách đơn nghỉ phép</title>
<style>
:root{ --base:#3b4b63; --base-strong:#334257; --base-soft:#51637d; --ink:#ffffff; --muted:#e5edf7; --line:#2d394a; --shadow:0 16px 40px rgba(0,0,0,.28); --radius:18px; --ok:#22c55e; --bad:#ef4444; }
html{ min-height:100%; background: linear-gradient(180deg, rgba(255,255,255,.12), rgba(255,255,255,.2)), url('${pageContext.request.contextPath}/img/hinhnen3.jpg') center/cover no-repeat fixed; }
body{margin:0;min-height:100dvh;font-family:Inter,"Segoe UI",Arial,sans-serif;color:var(--ink)}
body::after{content:"";position:fixed;inset:0;backdrop-filter:blur(2px) saturate(1.05);z-index:0}
.topbar{position:sticky;top:0;z-index:10;background:var(--base);border-bottom:1px solid var(--line); box-shadow:var(--shadow);display:flex;align-items:center;justify-content:space-between;padding:12px 18px}
.brand{display:flex;gap:10px;align-items:center;font-weight:800}
.logo{width:28px;height:28px;border-radius:9px;background:linear-gradient(135deg,#6a7d97,var(--base)); display:grid;place-items:center}
.menu{display:flex;gap:10px}
.menu a{color:#fff;text-decoration:none;background:var(--base-strong);padding:9px 14px;border-radius:12px;border:1px solid var(--line)}
.menu a:hover{background:var(--base-soft)}
.wrap{position:relative;z-index:1;max-width:1200px;margin:36px auto;padding:0 20px}
.head{display:flex;justify-content:space-between;align-items:center;margin-bottom:14px;gap:12px;flex-wrap:wrap}
h2{margin:0}
.btn{background:var(--base);border:1px solid var(--line);color:#fff;padding:10px 14px;border-radius:12px;text-decoration:none;font-weight:800}
.btn:hover{background:var(--base-soft);border-color:#51637d}
.panel{background:var(--base);border:1px solid var(--line);border-radius:var(--radius);box-shadow:var(--shadow);padding:18px}
table{width:100%;border-collapse:separate;border-spacing:0 10px;color:#fff; table-layout:fixed}
thead th{padding:8px 10px;text-align:left; position:relative}
tbody tr{background:var(--base-strong);border:1px solid var(--line);box-shadow:var(--shadow);border-radius:12px}
tbody td{padding:12px;vertical-align:middle; overflow:hidden; text-overflow:ellipsis; white-space:nowrap}
tbody tr:hover{background:var(--base-soft)}
a.rowlink{color:#fff;text-decoration:none;border-bottom:1px dashed transparent}
a.rowlink:hover{border-bottom-color:#fff}
.status-pill{padding:6px 10px;border-radius:999px;font-weight:800;display:inline-block}
.status-approved{background:#22c55e;color:#fff;border:1px solid #178a41}
.status-rejected{background:#ef4444;color:#fff;border:1px solid #b72f2f}
.status-progress{background:rgba(245,158,11,.18);color:#fde68a;border:1px solid rgba(245,158,11,.35)}
.actions{display:flex; gap:8px; align-items:center; overflow:visible; white-space:normal;}
.actions a,.actions form button{ display:inline-block; padding:6px 10px; border-radius:10px; border:1px solid #44556f; background:#2b3748; color:#fff; text-decoration:none; font-weight:700; cursor:pointer;}
.actions .danger{ background:#7f1d1d; border-color:#991b1b }
.errorBox{ background:rgba(239,68,68,.15); border:1px solid rgba(239,68,68,.4); color:#fee2e2; padding:14px; border-radius:12px; }
.pop{ display:none; position:absolute; left:8px; top:100%; margin-top:6px; background:#2b3748; border:1px solid var(--line); border-radius:10px; padding:8px; box-shadow:0 10px 20px rgba(0,0,0,.35); z-index:5; }
.pop input, .pop select{ width:220px; padding:8px 10px; border-radius:8px; border:1px solid #44556f; background:#2b3748; color:#fff; outline:none; }
.th-click{cursor:pointer; user-select:none}
.badge{display:inline-block; padding:6px 12px; border-radius:999px; background:#111827; border:1px solid #8a7a3e; color:#fde68a; font-weight:800}
</style>
<script>
function qs(){ return new URLSearchParams(window.location.search); }
function setAndGo(params){ window.location = window.location.pathname + '?' + params.toString(); }
function toggleNamePop(ev){ ev.stopPropagation(); const el=document.getElementById('name-pop'); el.style.display = (el.style.display==='block')?'none':'block'; if(el.style.display==='block'){ const ip=document.getElementById('qinput'); ip.focus(); ip.select(); } }
function submitNameFilter(ev){ ev.preventDefault(); const q=document.getElementById('qinput').value.trim(); const p=qs(); if(q) p.set('q',q); else p.delete('q'); if(!p.has('status')) p.set('status','all'); setAndGo(p); }
function toggleStatusPop(ev){ ev.stopPropagation(); const el=document.getElementById('status-pop'); el.style.display = (el.style.display==='block')?'none':'block'; if(el.style.display==='block'){ document.getElementById('statusSel').focus(); } }
function onStatusChange(sel){ const p=qs(); p.set('status', sel.value); setAndGo(p); }
document.addEventListener('click', function(e){
  ['name-pop','status-pop'].forEach(id=>{
    const pop=document.getElementById(id);
    const th =document.getElementById(id==='name-pop'?'th-creator':'th-status');
    if (!pop || !th) return;
    if (!pop.contains(e.target) && !th.contains(e.target)) pop.style.display='none';
  });
});
function stopInside(ev){ ev.stopPropagation(); }
</script>
</head>
<body>
<header class="topbar">
  <div class="brand"><div class="logo">HP</div>Hệ thống Quản lý Nghỉ phép</div>
  <nav class="menu">
    <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
    <c:if test="${sessionScope.allowed['/request/create']}"><a href="${pageContext.request.contextPath}/request/create">Tạo đơn</a></c:if>
    <c:if test="${sessionScope.allowed['/division/agenda']}"><a href="${pageContext.request.contextPath}/division/agenda">Agenda</a></c:if>
    <c:if test="${sessionScope.allowed['/admin/audit']}"><a href="${pageContext.request.contextPath}/admin/audit">Audit</a></c:if>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
  </nav>
</header>

<main class="wrap">
  <div class="head">
    <h2>📁 Danh sách đơn nghỉ phép</h2>
    <c:if test="${sessionScope.allowed['/request/create']}">
      <a class="btn" href="${pageContext.request.contextPath}/request/create">+ Tạo đơn mới</a>
    </c:if>
  </div>

  <c:if test="${not empty sessionScope.message}">
    <div class="panel" style="margin-bottom:12px;">${sessionScope.message}</div>
    <c:remove var="message" scope="session"/>
  </c:if>

  <c:if test="${permError}">
    <section class="panel"><div class="errorBox">${error}</div>
      <div style="margin-top:12px"><a class="btn" href="${pageContext.request.contextPath}/home">← Về trang chủ</a></div>
    </section>
  </c:if>

  <c:if test="${not permError}">
    <section class="panel">
      <table>
        <thead>
          <tr>
            <th>Loại đơn</th>
            <th>Từ</th>
            <th>Đến</th>

            <th id="th-creator" class="th-click" onclick="toggleNamePop(event)">
              Tạo bởi ⌕
              <div id="name-pop" class="pop" onclick="stopInside(event)">
                <form onsubmit="submitNameFilter(event)">
                  <input id="qinput" name="q" type="text" value="${q}" placeholder="Nhập tên cần tìm..."/>
                </form>
              </div>
            </th>

            <th id="th-status" class="th-click" onclick="toggleStatusPop(event)">
              Trạng thái ⌄
              <div id="status-pop" class="pop" onclick="stopInside(event)">
                <select id="statusSel" onchange="onStatusChange(this)">
                  <option value="all" ${statusSelected == 'all' ? 'selected' : ''}>Tất cả</option>
                  <option value="0"   ${statusSelected == '0'   ? 'selected' : ''}>In Progress</option>
                  <option value="1"   ${statusSelected == '1'   ? 'selected' : ''}>Approved</option>
                  <option value="2"   ${statusSelected == '2'   ? 'selected' : ''}>Rejected</option>
                </select>
              </div>
              <c:if test="${statusSelected != 'all'}">
                &nbsp;<span class="badge">
                  <c:choose>
                    <c:when test="${statusSelected == '0'}">In Progress</c:when>
                    <c:when test="${statusSelected == '1'}">Approved</c:when>
                    <c:when test="${statusSelected == '2'}">Rejected</c:when>
                  </c:choose>
                </span>
              </c:if>
            </th>

            <th>Lí do</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
        <c:forEach var="r" items="${rfls}">
          <c:set var="raw" value="${r.reason}" />
          <c:set var="pos" value="${fn:indexOf(raw,']')}" />
          <c:set var="type" value="${ (not empty raw and fn:startsWith(raw,'[') and pos gt 0) ? fn:substring(raw,1, pos) : 'N/A' }" />
          <tr>
            <td>${type}</td>
            <td>${r.from}</td>
            <td>${r.to}</td>
            <td title="${r.created_by.name}">${r.created_by.name}</td>
            <td>
              <c:choose>
                <c:when test="${r.status == 1}"><span class="status-pill status-approved">Approved</span></c:when>
                <c:when test="${r.status == 2}"><span class="status-pill status-rejected">Rejected</span></c:when>
                <c:otherwise><span class="status-pill status-progress">In Progress</span></c:otherwise>
              </c:choose>
            </td>
            <td><a class="rowlink" href="${pageContext.request.contextPath}/request/review?rid=${r.id}" title="${r.reason}">🔍 Chi tiết</a></td>
            <td class="actions">
              <c:if test="${r.status == 0 && r.created_by.id == sessionScope.auth.employee.id}">
                <a href="${pageContext.request.contextPath}/request/edit?rid=${r.id}">✏️ Sửa</a>
                <form action="${pageContext.request.contextPath}/request/cancel" method="post" style="display:inline" onsubmit="return confirm('Hủy đơn này?');">
                  <input type="hidden" name="rid" value="${r.id}"/>
                  <button type="submit" class="danger">🗑️ Hủy</button>
                </form>
              </c:if>

              <!-- Nút Admin xoá cứng -->
              <c:if test="${sessionScope.allowed['/admin/request/delete']}">
                <form action="${pageContext.request.contextPath}/admin/request/delete" method="post" style="display:inline" onsubmit="return confirm('Admin xoá đơn #${r.id}?');">
                  <input type="hidden" name="rid" value="${r.id}"/>
                  <button type="submit" class="danger">🗑️ Admin Delete</button>
                </form>
              </c:if>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </section>
  </c:if>
</main>
</body>
</html>
