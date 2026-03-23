<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đơn chờ nhận</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css' />">
    <link rel="stylesheet" href="<c:url value='/css/shipper.css' />">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="shipper-container">
    <div class="shipper-header">
        <div>
            <h2>Đơn hàng đang chờ Shipper nhận</h2>
            <span class="shipper-badge">READY</span>
        </div>
        <nav class="shipper-nav">
            <a href="${pageContext.request.contextPath}/shipper/dashboard" class="tab">
                <i class="fas fa-chart-pie"></i><span class="nav-text">Tổng quan</span>
            </a>
            <a href="${pageContext.request.contextPath}/shipper/waiting" class="tab active">
                <i class="fas fa-box"></i><span class="nav-text">Đơn chờ nhận</span>
            </a>
            <a href="${pageContext.request.contextPath}/shipper/delivering" class="tab">
                <i class="fas fa-motorcycle"></i><span class="nav-text">Đang giao</span>
            </a>
            <a href="${pageContext.request.contextPath}/shipper/stats" class="tab">
                <i class="fas fa-chart-line"></i><span class="nav-text">Thống kê</span>
            </a>
        </nav>
    </div>

    <div class="shipper-card">
        <c:choose>
            <c:when test="${empty orders}">
                <div class="shipper-empty">Hiện chưa có đơn nào sẵn sàng nhận.</div>
            </c:when>
            <c:otherwise>
                <fmt:setLocale value="vi_VN"/>
                <table class="shipper-table">
                    <thead>
                    <tr>
                        <th>Mã đơn</th>
                        <th>Khách hàng</th>
                        <th>Quán</th>
                        <th>Địa chỉ giao</th>
                        <th>Liên hệ</th>
                        <th>Tổng tiền</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${orders}" var="order">
                        <tr>
                            <td>#${order.id}</td>
                            <td>${order.customer.fullName}</td>
                            <td>${order.shop.name}</td>
                            <td>${order.address}</td>
                            <td>${order.phone}</td>
                            <td><fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/orders/accept" method="post">
                                    <input type="hidden" name="orderId" value="${order.id}" />
                                    <button type="submit" class="shipper-action">
                                        <i class="fas fa-motorcycle"></i> Nhận giao
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />
</body>
</html>
