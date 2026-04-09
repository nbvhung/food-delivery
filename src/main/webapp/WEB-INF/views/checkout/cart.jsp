<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Giỏ hàng - Mạnh Mall</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/cart.css'/>">
</head>
<body style="background-color: #f5f5f5;">
<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="container cart-container">
    <h2>Giỏ hàng của bạn</h2>

    <c:choose>
        <c:when test="${empty items}">
            <p class="empty-state">Giỏ hàng đang trống. Hãy chọn món ăn bạn thích nhé!</p>
        </c:when>
        <c:otherwise>
            <div class="cart-list">
                <c:forEach items="${items}" var="item">
                    <div class="cart-item">
                        <img src="${pageContext.request.contextPath}${item.food.image}">

                        <div>
                            <p class="cart-name">${item.food.name}</p>
                            <p class="cart-price">${item.food.price}đ</p>
                        </div>

                        <div class="cart-qty">
                            <form action="${pageContext.request.contextPath}/cart/update" method="post" class="qty-form">
                                <input type="hidden" name="foodId" value="${item.food.id}">
                                <button type="submit" name="action" value="decrease" class="qty-btn">−</button>
                                <input type="number" name="quantity" value="${item.quantity}" min="1" class="qty-input">
                                <button type="submit" name="action" value="increase" class="qty-btn">+</button>
                            </form>
                        </div>

                        <div class="cart-subtotal">
                                ${item.subtotal}đ
                        </div>

                        <div class="cart-remove">
                            <form action="${pageContext.request.contextPath}/cart/remove"
                                  method="post"
                                  onsubmit="return confirmRemove();">

                                <input type="hidden" name="foodId" value="${item.food.id}">

                                <button type="submit" class="remove-btn">
                                    <i class="fa fa-trash"></i>
                                    <span class="remove-text">Xóa</span>
                                </button>
                            </form>
                        </div>

                    </div>
                    <script>
                        function confirmRemove() {
                            return confirm("Bạn có chắc chắn muốn xóa món ăn này khỏi giỏ hàng không?");
                        }
                    </script>

                </c:forEach>
            </div>
            <div class="cart-summary">
                <span>Tổng cộng</span>
                <strong>${total}đ</strong>
            </div>
            <div class="cart-actions">
                <a class="btn-primary" href="${pageContext.request.contextPath}/checkout">Thanh toán</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />
</body>
</html>
