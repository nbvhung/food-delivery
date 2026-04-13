<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tết món ăn</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css'/>">
</head>
<body style="background-color: #f5f5f5;">

<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<c:set var="isOwner" value="${not empty sessionScope.currentUser and sessionScope.currentUser.role == 'MERCHANT' and not empty food.shop and not empty food.shop.owner and sessionScope.currentUser.id == food.shop.owner.id}" />

<div class="container py-4" style="max-width: 900px;">
    <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
        <div class="row g-0">
            <div class="col-12 col-md-5">
                <img src="${pageContext.request.contextPath}${food.image}" alt="${food.name}"
                     style="width: 100%; height: 100%; min-height: 320px; object-fit: cover;"
                     onerror="this.onerror=null;this.src='https://via.placeholder.com/600x600?text=No+Image';">
            </div>
            <div class="col-12 col-md-7">
                <div class="p-4 p-md-5">
                    <h3 class="mb-2" style="color:#ee4d2d;">${food.name}</h3>
                    <div class="mb-3 text-danger fw-bold" style="font-size: 28px;">
                        <fmt:formatNumber value="${food.price}" type="number" groupingUsed="true"/> đ
                    </div>

                    <p class="mb-2"><span class="text-secondary">Cua hang:</span> ${food.shop.name}</p>
                    <p class="mb-2"><span class="text-secondary">Danh muc:</span> ${food.category.name}</p>
                    <p class="mb-0 text-secondary">${food.description}</p>

                    <div class="d-flex flex-wrap gap-2 mt-4">
                        <c:if test="${isOwner}">
                            <a class="btn btn-outline-secondary" href="<c:url value='/shops/foods/edit/${food.id}'/>">
                                <i class="fa-solid fa-pen"></i> Sửa món
                            </a>
                            <a class="btn btn-outline-danger" href="<c:url value='/shops/foods/delete/${food.id}'/>"
                               onclick="return confirm('Ban chac chan muon xoa mon nay?')">
                                <i class="fa-solid fa-trash"></i> Xóa món
                            </a>
                            <a class="btn btn-primary" href="<c:url value='/shops/foods'/>">
                                <i class="fa-solid fa-arrow-left"></i> Về danh sách món
                            </a>
                        </c:if>

                        <c:if test="${not isOwner}">
                            <a class="btn btn-primary" href="<c:url value='/foods/detail/${food.id}'/>">
                                <i class="fa-solid fa-cart-shopping"></i> Đặt món
                            </a>
                            <a class="btn btn-outline-secondary" href="<c:url value='/'/>">
                                <i class="fa-solid fa-arrow-left"></i> Quay lại
                            </a>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />

</body>
</html>
