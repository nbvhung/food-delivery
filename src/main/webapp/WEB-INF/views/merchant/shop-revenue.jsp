<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Doanh thu cua hang</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css'/>">
    <style>
        .stat-card {
            border: none;
            border-radius: 14px;
            box-shadow: 0 10px 24px rgba(0, 0, 0, 0.08);
            height: 100%;
        }
        .stat-value {
            font-size: 28px;
            font-weight: 800;
            color: #ee4d2d;
            margin: 8px 0 0;
        }
    </style>
</head>
<body style="background-color: #f5f5f5;">

<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="container py-4">
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
        <div>
            <h3 class="m-0" style="color:#ee4d2d;">Bao cao doanh thu</h3>
            <div class="text-secondary">Cua hang: <strong>${shop.name}</strong></div>
        </div>
        <div class="d-flex gap-2">
            <a href="<c:url value='/shops/my-shop'/>" class="btn btn-outline-secondary">
                <i class="fa-solid fa-store"></i> Ve cua hang
            </a>
            <a href="<c:url value='/shops/foods'/>" class="btn btn-outline-dark">
                <i class="fa-solid fa-utensils"></i> Quan ly mon
            </a>
        </div>
    </div>

    <div class="row g-3 mb-3">
        <div class="col-12 col-md-6">
            <div class="stat-card card p-4">
                <div class="text-secondary">Tong doanh thu (tat ca thoi gian)</div>
                <div class="stat-value">
                    <fmt:formatNumber value="${totalRevenue}" type="number" groupingUsed="true"/> đ
                </div>
            </div>
        </div>

        <div class="col-12 col-md-6">
            <div class="stat-card card p-4">
                <div class="text-secondary">Doanh thu thang ${currentMonth}</div>
                <div class="stat-value">
                    <fmt:formatNumber value="${monthlyRevenue}" type="number" groupingUsed="true"/> đ
                </div>
            </div>
        </div>
    </div>

    <div class="row g-3">
        <div class="col-12 col-sm-6 col-lg-3">
            <div class="stat-card card p-4">
                <div class="text-secondary">Tong don hang</div>
                <h4 class="mt-2 mb-0">${totalOrders}</h4>
            </div>
        </div>

        <div class="col-12 col-sm-6 col-lg-3">
            <div class="stat-card card p-4">
                <div class="text-secondary">Don da giao</div>
                <h4 class="mt-2 mb-0 text-success">${deliveredCount}</h4>
            </div>
        </div>

        <div class="col-12 col-sm-6 col-lg-3">
            <div class="stat-card card p-4">
                <div class="text-secondary">Don san sang giao</div>
                <h4 class="mt-2 mb-0 text-primary">${readyCount}</h4>
            </div>
        </div>

        <div class="col-12 col-sm-6 col-lg-3">
            <div class="stat-card card p-4">
                <div class="text-secondary">Don dang van chuyen</div>
                <h4 class="mt-2 mb-0 text-warning">${shippingCount}</h4>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />

</body>
</html>
