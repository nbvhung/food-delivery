<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanh toán - Mạnh Mall</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/checkout.css'/>">
</head>

<body style="background-color: #f5f5f5;">

<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="container checkout-container">
    <h2>Xác nhận đơn hàng</h2>

    <div class="cart-actions" style="margin-top: 0; margin-bottom: 18px; justify-content: flex-start;">
        <a class="btn-primary" href="${pageContext.request.contextPath}/checkout/orders">Xem đơn hàng của tôi</a>
    </div>

    <c:choose>
        <c:when test="${empty items}">
            <p class="empty-state">Bạn chưa có món nào để thanh toán.</p>
        </c:when>

        <c:otherwise>
            <c:if test="${not empty checkoutError}">
                <p class="empty-state" style="margin-bottom: 16px; color: #ee4d2d;">${checkoutError}</p>
            </c:if>
            <c:if test="${not empty checkoutSuccess}">
                <p class="empty-state" style="margin-bottom: 16px; color: #16a34a;">${checkoutSuccess}</p>
            </c:if>
            <form id="checkoutForm" action="${pageContext.request.contextPath}/checkout/place-order" method="post">

            <!-- Danh sách món -->
            <div class="checkout-list">
                <c:forEach items="${items}" var="item">
                    <div class="checkout-item">
                        <img src="${pageContext.request.contextPath}${item.food.image}">
                        <div>
                            <p class="cart-name">${item.food.name}</p>
                            <p class="cart-desc">Số lượng: ${item.quantity}</p>
                            <p class="cart-price">${item.subtotal}đ</p>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- KHÔNG DÙNG FORM -->
            <div class="checkout-section">
                <h3>Thông tin giao hàng</h3>

                <div class="form-group">
                    <label>Họ và tên</label>
                    <input type="text" id="fullName" name="fullName" value="${sessionScope.currentUser.fullName}">
                    <small class="error-msg"></small>
                </div>

                <div class="form-group">
                    <label>Số điện thoại</label>
                    <input type="text" id="phone" name="phone" value="${sessionScope.currentUser.phone}">
                    <small class="error-msg"></small>
                </div>

                <div class="form-group">
                    <label>Địa chỉ nhận hàng</label>
                    <input type="text" id="address" name="address">
                    <small class="error-msg"></small>
                </div>
            </div>

            <div class="checkout-section">
                <h3>Phương thức thanh toán</h3>

                <div class="payment-method">
                    <label>
                        <input type="radio" name="paymentMethod" value="COD">
                        Thanh toán khi nhận hàng
                    </label>

                    <label>
                        <input type="radio" name="paymentMethod" value="BANK">
                        Chuyển khoản ngân hàng
                    </label>

                    <small class="error-msg"></small>
                </div>
            </div>

            <div class="checkout-bar">
                <div class="checkout-total">
                    Tổng cộng: <strong>${total}đ</strong>
                </div>

                <button type="button" class="btn-confirm" id="confirmBtn">
                    Xác nhận thanh toán
                </button>
            </div>
            </form>

        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />

<script>
    document.getElementById("confirmBtn").onclick = function () {
        let isValid = true;

        document.querySelectorAll(".error-msg").forEach(e => e.innerText = "");
        document.querySelectorAll("input").forEach(e => e.classList.remove("input-error"));

        const fullName = document.getElementById("fullName");
        const phone = document.getElementById("phone");
        const address = document.getElementById("address");
        const payment = document.querySelector('input[name="paymentMethod"]:checked');

        if (!fullName.value.trim()) {
            showError(fullName, "Vui lòng nhập họ tên");
            isValid = false;
        }

        if (!phone.value.trim()) {
            showError(phone, "Vui lòng nhập số điện thoại");
            isValid = false;
        }

        if (!address.value.trim()) {
            showError(address, "Vui lòng nhập địa chỉ");
            isValid = false;
        }

        if (!payment) {
            document.querySelector(".payment-method .error-msg").innerText =
                "Vui lòng chọn phương thức thanh toán";
            isValid = false;
        }

        if (isValid) {
            document.getElementById("checkoutForm").submit();
        }
    };

    function showError(input, msg) {
        input.classList.add("input-error");
        input.nextElementSibling.innerText = msg;
    }
</script>

</body>
</html>
