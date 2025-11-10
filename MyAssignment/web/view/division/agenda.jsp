<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://jakarta.ee/tags/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Agenda hôm nay</title>
        <style>
            .container{
                max-width:1100px;
                margin:18px auto;
                padding:0 14px
            }
            h1{
                margin:14px 0
            }
            .filter{
                display:flex;
                gap:10px;
                align-items:center;
                margin:10px 0
            }
            select{
                padding:8px;
                border:1px solid #cbd5e1;
                border-radius:8px
            }
            table{
                width:100%;
                border-collapse:collapse;
                background:#fff;
                border:1px solid #e5e7eb
            }
            th,td{
                padding:10px;
                border-bottom:1px solid #e5e7eb;
                text-align:left
            }
            .ok{
                color:#16a34a;
                font-weight:600
            }
            .off{
                color:#dc2626;
                font-weight:600
            }
            .pager{
                margin-top:10px;
                display:flex;
                gap:6px
            }
            .pager a, .pager span{
                padding:6px 10px;
                border:1px solid #e5e7eb;
                border-radius:8px;
                text-decoration:none
            }
            .active{
                background:#1f2937;
                color:#fff;
                border-color:#1f2937
            }
            .stat{
                margin:8px 0;
                color:#6b7280
            }
        </style>
    </head>
    <body>

        <!-- Nếu bạn dùng topbar.jspf thì đổi thành /view/common/topbar.jspf -->
        <jsp:include page="/view/common/topbar.jsp"/>

        <div class="container">
            <h1>Tình trạng nhân sự hôm nay</h1>

            <!-- Bộ lọc phòng ban -->
            <form class="filter" method="get" action="${pageContext.request.contextPath}/division/agenda">
                <select name="div" onchange="this.form.submit()">

                    <!-- All -->
                    <c:choose>
                        <c:when test="${empty param['div']}">
                            <option value="" selected>— Tất cả phòng —</option>
                        </c:when>
                        <c:otherwise>
                            <option value="">— Tất cả phòng —</option>
                        </c:otherwise>
                    </c:choose>

                    <!-- IT -->
                    <c:choose>
                        <c:when test="${param['div'] eq 'IT'}">
                            <option value="IT" selected>IT</option>
                        </c:when>
                        <c:otherwise>
                            <option value="IT">IT</option>
                        </c:otherwise>
                    </c:choose>

                    <!-- HR -->
                    <c:choose>
                        <c:when test="${param['div'] eq 'HR'}">
                            <option value="HR" selected>HR</option>
                        </c:when>
                        <c:otherwise>
                            <option value="HR">HR</option>
                        </c:otherwise>
                    </c:choose>

                    <!-- MKT -->
                    <c:choose>
                        <c:when test="${param['div'] eq 'MKT'}">
                            <option value="MKT" selected>Marketing</option>
                        </c:when>
                        <c:otherwise>
                            <option value="MKT">Marketing</option>
                        </c:otherwise>
                    </c:choose>

                    <!-- EXE -->
                    <c:choose>
                        <c:when test="${param['div'] eq 'EXE'}">
                            <option value="EXE" selected>Executive</option>
                        </c:when>
                        <c:otherwise>
                            <option value="EXE">Executive</option>
                        </c:otherwise>
                    </c:choose>

                </select>
            </form>

            <!-- Thống kê: controller đã set stats_off -->
            <div class="stat">
                Đang nghỉ (chờ duyệt/đã duyệt) trong trang này:
                <b>${stats_off}</b>
            </div>

            <!-- Bảng -->
            <table>
                <thead>
                    <tr>
                        <th>EID</th>
                        <th>Tên</th>
                        <th>Vị trí</th>
                        <th>Phòng</th>
                        <th>Hôm nay</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${list}" var="e">
                        <tr>
                            <td>${e['eid']}</td>
                            <td>${e['ename']}</td>
                            <td>${e['position']}</td>
                            <td>${e['dcode']}</td>
                            <td>
                                <c:choose>
                                    <!-- leaveStatus: nullable/0/1/2/3 -->
                                    <c:when test="${e['leaveStatus'] == 0 || e['leaveStatus'] == 1}">
                                        <span class="off">Nghỉ</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="ok">Đi làm</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty list}">
                        <tr>
                            <td colspan="5">Không có dữ liệu.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>

            <!-- Phân trang -->
            <div class="pager">
                <c:forEach begin="1" end="${pages}" var="p">
                    <c:choose>
                        <c:when test="${p == page}">
                            <span class="active">${p}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/division/agenda?page=${p}&div=${param['div']}">
                                ${p}
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </div>
    </body>
</html>
