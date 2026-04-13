<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thống kê Shipper</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css' />">
    <link rel="stylesheet" href="<c:url value='/css/shipper.css' />">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="shipper-container">
    <div class="shipper-header">
        <div>
            <h2>Thống kê giao hàng</h2>
            <span class="shipper-badge">Tổng hợp</span>
        </div>
        <nav class="shipper-nav">
            <a href="${pageContext.request.contextPath}/shipper/dashboard" class="tab">
                <i class="fas fa-chart-pie"></i><span class="nav-text">Tổng quan</span>
            </a>
            <a href="${pageContext.request.contextPath}/shipper/waiting" class="tab">
                <i class="fas fa-box"></i><span class="nav-text">Đơn chờ nhận</span>
            </a>
            <a href="${pageContext.request.contextPath}/shipper/delivering" class="tab">
                <i class="fas fa-motorcycle"></i><span class="nav-text">Đang giao</span>
            </a>
            <a href="${pageContext.request.contextPath}/shipper/stats" class="tab active">
                <i class="fas fa-chart-line"></i><span class="nav-text">Thống kê</span>
            </a>
        </nav>
    </div>

    <div class="shipper-card">
        <div class="shipper-summary">
            <div class="summary-item">
                <h3>Đơn đã giao thành công</h3>
                <p>${deliveredCount}</p>
                <span class="summary-note">Đơn hàng hoàn tất</span>
            </div>
            <div class="summary-item">
                <h3>Tổng giá trị đơn đã giao</h3>
                <p>${totalEarnings} đ</p>
                <span class="summary-note">Doanh thu tích lũy</span>
            </div>
            <div class="summary-item">
                <h3>Điểm đánh giá trung bình</h3>
                <div class="rating" title="${avgRating}">
                    <div class="rating-back">
                        ★★★★★
                    </div>
                    <div class="rating-front"
                         style="width: ${avgRating * 20}%">
                        ★★★★★
                    </div>
                </div>
            </div>
        </div>

        <h3>Danh sách đơn đã giao thành công</h3>

        <c:choose>
            <c:when test="${not empty completedOrders}">
                <table class="shipper-table">
                    <fmt:setLocale value="vi_VN"/>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Địa chỉ</th>
                        <th>Số điện thoại</th>
                        <th>Tổng giá trị đơn</th>
                        <th>Đánh giá</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="order" items="${completedOrders}">
                        <tr>
                            <td>${order.id}</td>
                            <td>${order.address}</td>
                            <td>${order.phone}</td>
                            <td><fmt:formatNumber value="${order.totalPrice}" groupingUsed="true" maxFractionDigits="0"/> đ</td>
                            <td>${order.shipperRating} ⭐</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>

            <c:otherwise>
                <div class="shipper-empty">
                    Chưa có đơn hàng nào hoàn tất.
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />
</body>
</html>
