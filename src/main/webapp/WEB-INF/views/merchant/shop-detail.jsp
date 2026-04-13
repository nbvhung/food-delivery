<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi tiết cửa hàng</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css'/>">
    <style>
        .hero-card {
            border: none;
            border-radius: 16px;
            overflow: hidden;
            box-shadow: 0 10px 26px rgba(0, 0, 0, 0.08);
        }
        .hero-banner {
            width: 100%;
            height: 260px;
            object-fit: cover;
            background: #f2f2f2;
        }
        .food-card {
            border: 1px solid #efefef;
            border-radius: 14px;
            overflow: hidden;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            height: 100%;
            background: #fff;
        }
        .food-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 24px rgba(0, 0, 0, 0.08);
        }
        .food-card img {
            width: 100%;
            height: 170px;
            object-fit: cover;
            background: #f7f7f7;
        }
        .food-name {
            font-weight: 700;
            color: #212529;
            margin-bottom: 4px;
        }
        .food-desc {
            color: #6c757d;
            font-size: 14px;
            min-height: 42px;
        }
        .food-price {
            color: #ee4d2d;
            font-weight: 700;
        }
    </style>
</head>
<body style="background-color: #f5f5f5;">

<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="container py-4">
    <div class="hero-card mb-4">
        <img class="hero-banner" src="${pageContext.request.contextPath}${shop.image}" alt="${shop.name}"
             onerror="this.onerror=null;this.src='https://via.placeholder.com/1200x400?text=No+Image';">

        <div class="p-4 bg-white">
            <div class="d-flex flex-wrap justify-content-between gap-3 align-items-start">
                <div>
                    <h2 class="mb-2" style="color:#ee4d2d;">${shop.name}</h2>
                    <p class="text-secondary mb-2"><i class="fa-solid fa-location-dot"></i> ${shop.address}</p>
                    <span class="badge ${shop.status == 'ACTIVE' ? 'bg-success' : (shop.status == 'PENDING' ? 'bg-warning text-dark' : 'bg-danger')}">
                        ${shop.status}
                    </span>
                </div>

                <c:if test="${isOwner}">
                    <div class="d-flex flex-wrap gap-2">
                        <a href="<c:url value='/shops/edit'/>" class="btn btn-outline-secondary">
                            <i class="fa-solid fa-pen"></i> Chỉnh sửa thông tin
                        </a>
                        <a href="<c:url value='/shops/foods'/>" class="btn btn-primary">
                            <i class="fa-solid fa-utensils"></i> Quản lý món ăn
                        </a>
                        <a href="<c:url value='/shops/foods/create'/>" class="btn btn-success">
                            <i class="fa-solid fa-plus"></i> Thêm món mới
                        </a>
                        <a href="<c:url value='/shops/revenue'/>" class="btn btn-dark">
                            <i class="fa-solid fa-chart-line"></i> Doanh thu
                        </a>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="m-0">Danh sách món ăn</h4>
        <c:if test="${isOwner}">
            <a href="<c:url value='/shops/foods/create'/>" class="btn btn-sm btn-primary">
                <i class="fa-solid fa-plus"></i> Thêm món
            </a>
        </c:if>
    </div>

    <c:choose>
        <c:when test="${empty foods}">
            <div class="alert alert-secondary mb-0">
                Cửa hàng chưa có món ăn nào
            </div>
        </c:when>
        <c:otherwise>
            <div class="row g-3">
                <c:forEach items="${foods}" var="food">
                    <div class="col-12 col-sm-6 col-lg-4">
                        <div class="food-card">
                            <img src="${pageContext.request.contextPath}${food.image}" alt="${food.name}"
                                 onerror="this.onerror=null;this.src='https://via.placeholder.com/600x400?text=No+Image';">
                            <div class="p-3">
                                <div class="food-name">${food.name}</div>
                                <div class="food-desc">${food.description}</div>
                                <div class="d-flex justify-content-between align-items-center mt-2">
                                    <span class="food-price">
                                        <fmt:formatNumber value="${food.price}" type="number" groupingUsed="true"/> đ
                                    </span>
                                    <span class="badge text-bg-light">${food.category.name}</span>
                                </div>

                                <div class="d-flex gap-2 mt-3">
                                    <c:if test="${isOwner}">
                                        <a class="btn btn-sm btn-outline-primary" href="<c:url value='/shops/foods/${food.id}'/>">Chi tiết</a>
                                        <a class="btn btn-sm btn-outline-secondary" href="<c:url value='/shops/foods/edit/${food.id}'/>">Sửa</a>
                                    </c:if>
                                    <c:if test="${not isOwner}">
                                        <a class="btn btn-sm btn-primary" href="<c:url value='/foods/detail/${food.id}'/>">Đặt món</a>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />

</body>
</html>
