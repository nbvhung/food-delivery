<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đơn hàng của tôi - Mạnh Mall</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/cart.css?v=2'/>">
    <link rel="stylesheet" href="<c:url value='/css/checkout.css?v=2'/>">
</head>
<body style="background-color: #f5f5f5;">
<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="container cart-container checkout-container">
    <h2>Đơn hàng của tôi</h2>

    <c:choose>
        <c:when test="${empty orders}">
            <p class="empty-state">Bạn chưa có đơn hàng nào. Hãy quay lại và chọn món để đặt hàng.</p>
        </c:when>
        <c:otherwise>
            <div class="order-history-list cart-list">
                <c:forEach items="${orders}" var="order">
                    <div class="order-history-card">
                        <div class="order-history-head">
                            <div class="order-history-title">
                                <p class="order-history-id">Đơn #${order.id}</p>
                                <p class="order-history-shop">${order.shopName}</p>
                            </div>
                            <span class="order-status-badge status-${order.status}">
                                <c:choose>
                                    <c:when test="${order.status == 'READY'}">Chờ shipper nhận</c:when>
                                    <c:when test="${order.status == 'SHIPPING'}">Đang giao</c:when>
                                    <c:when test="${order.status == 'DELIVERED'}">Đã giao</c:when>
                                    <c:when test="${order.status == 'CANCELLED'}">Đã hủy</c:when>
                                    <c:otherwise>${order.status}</c:otherwise>
                                </c:choose>
                            </span>
                        </div>

                        <div class="order-history-meta">
                            <div class="order-meta-item">
                                <i class="fas fa-location-dot"></i>
                                <span>${order.address}</span>
                            </div>
                            <div class="order-meta-item">
                                <i class="fas fa-phone"></i>
                                <span>${order.phone}</span>
                            </div>
                            <div class="order-meta-item">
                                <i class="fas fa-clock"></i>
                                <span>${order.createdAtText}</span>
                            </div>
                            <div class="order-meta-item">
                                <i class="fas fa-motorcycle"></i>
                                <span>${order.shipperName}</span>
                            </div>
                        </div>

                        <div class="order-line-list">
                            <c:forEach items="${order.details}" var="detail">
                                <div class="order-line-item">
                                    <span>${detail.foodName}</span>
                                    <span>x${detail.quantity}</span>
                                    <span><fmt:formatNumber value="${detail.price}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ</span>
                                </div>
                            </c:forEach>
                        </div>

                        <div class="order-history-foot">
                            <span class="order-status-text">Trạng thái hiện tại:
                                <strong>
                                    <c:choose>
                                        <c:when test="${order.status == 'READY'}">Chờ shipper nhận</c:when>
                                        <c:when test="${order.status == 'SHIPPING'}">Đang giao</c:when>
                                        <c:when test="${order.status == 'DELIVERED'}">Đã giao</c:when>
                                        <c:when test="${order.status == 'CANCELLED'}">Đã hủy</c:when>
                                        <c:otherwise>${order.status}</c:otherwise>
                                    </c:choose>
                                </strong>
                            </span>
                            <span class="order-total"> Tổng: <strong><fmt:formatNumber value="${order.total}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ</strong></span>
                        </div>
                        <c:if test="${order.status == 'READY'}">
                            <div class="order-history-actions">
                                <form action="${pageContext.request.contextPath}/checkout/orders/cancel" method="post" onsubmit="return confirm('Bạn có chắc muốn hủy đơn hàng này không?');">
                                    <input type="hidden" name="orderId" value="${order.id}">
                                    <button type="submit" class="order-cancel-btn">Hủy đơn hàng</button>
                                </form>
                            </div>
                        </c:if>
                        <c:if test="${order.canReview}">
                            <div class="order-review-box">
                                <div class="order-review-head">
                                    <div>
                                        <p class="order-review-title">Xác nhận đã nhận hàng</p>
                                        <p class="order-review-subtitle">Đánh giá shipper bằng cách chọn số sao.</p>
                                    </div>
                                    <button type="button" class="btn-confirm order-review-toggle" onclick="toggleReviewForm('review-${order.id}')">
                                        Đã nhận thành công
                                    </button>
                                </div>

                                <form id="review-${order.id}" class="order-review-form" action="${pageContext.request.contextPath}/checkout/orders/review" method="post" style="display: none;">
                                    <input type="hidden" name="orderId" value="${order.id}">

                                    <div class="star-rating">
                                        <input id="star5-${order.id}" type="radio" name="rating" value="5" required>
                                        <label for="star5-${order.id}" title="5 sao"><i class="fas fa-star"></i></label>
                                        <input id="star4-${order.id}" type="radio" name="rating" value="4">
                                        <label for="star4-${order.id}" title="4 sao"><i class="fas fa-star"></i></label>
                                        <input id="star3-${order.id}" type="radio" name="rating" value="3">
                                        <label for="star3-${order.id}" title="3 sao"><i class="fas fa-star"></i></label>
                                        <input id="star2-${order.id}" type="radio" name="rating" value="2">
                                        <label for="star2-${order.id}" title="2 sao"><i class="fas fa-star"></i></label>
                                        <input id="star1-${order.id}" type="radio" name="rating" value="1">
                                        <label for="star1-${order.id}" title="1 sao"><i class="fas fa-star"></i></label>
                                    </div>

                                    <button type="submit" class="btn-confirm order-submit-review">Gửi đánh giá</button>
                                </form>
                            </div>
                        </c:if>
                        <c:if test="${order.reviewed}">
                            <div class="order-reviewed-box">
                                <p class="order-reviewed-title">Bạn đã đánh giá shipper</p>
                                <p class="order-reviewed-rating">${order.shipperRating}/5 sao</p>
                            </div>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />
<script>
    function toggleReviewForm(formId) {
        const form = document.getElementById(formId);
        if (!form) {
            return;
        }
        form.style.display = form.style.display === "none" ? "block" : "none";
    }
</script>
</body>
</html>
