<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>${food.name} - Mạnh Mall</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
  <link rel="stylesheet" href="<c:url value='/css/home.css'/>">
  <link rel="stylesheet" href="<c:url value='/css/food.css'/>">
</head>
<body style="background-color: #f5f5f5;">
<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="container detail-container">
  <div class="detail-card">
    <div class="detail-image">
      <img src="${pageContext.request.contextPath}${food.image}" alt="${food.name}">
    </div>
    <div class="detail-info">
      <h1>${food.name}</h1>
      <p class="detail-price">${food.price}đ</p>
      <p class="detail-description">${food.description}</p>

<%--      <div class="detail-meta">--%>
<%--        <span class="detail-pill">--%>
<%--          <i class="fas fa-store"></i>--%>
<%--          <a href="${pageContext.request.contextPath}/shops/${food.shop.id}">${food.shop.name}</a>--%>
<%--        </span>--%>
<%--        <span class="detail-pill">--%>
<%--          <i class="fas fa-layer-group"></i>--%>
<%--          ${food.category.name}--%>
<%--        </span>--%>
<%--      </div>--%>

      <form method="post" class="detail-actions">
        <input type="hidden" name="foodId" value="${food.id}">
        <div class="quantity-bar">
          <button type="button" onclick="this.nextElementSibling.stepDown()">-</button>
          <input type="number" name="quantity" value="1" min="1">
          <button type="button" onclick="this.previousElementSibling.stepUp()">+</button>
        </div>

        <div class="action-buttons">
          <button type="submit" class="btn-outline" formaction="${pageContext.request.contextPath}/cart/add">
            <i class="fas fa-cart-plus"></i> Thêm vào giỏ hàng
          </button>
          <button type="submit" class="btn-primary" formaction="${pageContext.request.contextPath}/cart/buy-now">
            Mua ngay
          </button>
        </div>
      </form>

      <a class="detail-back" href="${pageContext.request.contextPath}/">
        <i class="fas fa-arrow-left"></i>
        Quay lại
      </a>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />
</body>
</html>
