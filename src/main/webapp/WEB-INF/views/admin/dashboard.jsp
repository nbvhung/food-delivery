<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hệ thống Quản trị - Food Delivery</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body { background-color: #f8f9fa; }
        .sidebar { height: 100vh; background: #212529; color: white; position: fixed; width: 240px; }
        .sidebar a { color: #adb5bd; text-decoration: none; padding: 12px 20px; display: block; }
        .sidebar a:hover, .sidebar a.active { background: #343a40; color: white; border-left: 4px solid #ffc107; }
        .main-content { margin-left: 240px; padding: 30px; }
        .stat-card { border: none; border-radius: 10px; transition: transform 0.3s; }
        .stat-card:hover { transform: translateY(-5px); }
        .revenue-card { border-radius: 15px; border: none; }
    </style>
</head>
<body>

<div class="sidebar">
    <div class="text-center py-4">
        <i class="fas fa-user-shield fa-3x text-warning"></i>
        <h5 class="mt-2">SITE ADMIN</h5>
    </div>
    <a href="${pageContext.request.contextPath}/admin/dashboard" class="active"><i class="fas fa-chart-line me-2"></i> Dashboard</a>
    <a href="${pageContext.request.contextPath}/admin/users"><i class="fas fa-users me-2"></i> Quản lý User</a>
    <a href="${pageContext.request.contextPath}/admin/shops"><i class="fas fa-store me-2"></i> Duyệt Cửa hàng</a>
    <hr>
    <a href="${pageContext.request.contextPath}/" target="_blank"><i class="fas fa-external-link-alt me-2"></i> Xem trang chủ</a>
    <a href="${pageContext.request.contextPath}/logout" class="text-danger"><i class="fas fa-sign-out-alt me-2"></i> Đăng xuất</a>
</div>

<div class="main-content">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Tổng quan hệ thống</h2>
        <span class="badge bg-dark p-2">Chào, ${sessionScope.currentUser.fullName}</span>
    </div>

    <div class="row mb-4">
        <div class="col-md-3">
            <div class="card stat-card bg-primary text-white p-3">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h6 class="text-uppercase">Người dùng</h6>
                        <h2 class="mb-0">${totalUsers}</h2>
                    </div>
                    <i class="fas fa-users fa-2x opacity-50"></i>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card stat-card bg-success text-white p-3">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h6 class="text-uppercase">Cửa hàng</h6>
                        <h2 class="mb-0">${totalShops}</h2>
                    </div>
                    <i class="fas fa-store fa-2x opacity-50"></i>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card stat-card bg-warning text-white p-3">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h6 class="text-uppercase">Đơn hàng</h6>
                        <h2 class="mb-0">${totalOrders}</h2>
                    </div>
                    <i class="fas fa-shipping-fast fa-2x opacity-50"></i>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card stat-card bg-danger text-white p-3">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h6 class="text-uppercase">Món ăn</h6>
                        <h2 class="mb-0">${totalFoods}</h2>
                    </div>
                    <i class="fas fa-utensils fa-2x opacity-50"></i>
                </div>
            </div>
        </div>
    </div>

    <div class="row mb-4">
        <div class="col-md-6">
            <div class="card revenue-card text-white p-4 shadow-sm" style="background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h6 class="text-uppercase mb-2">Tổng doanh số toàn hệ thống</h6>
                        <h2 class="mb-0 fw-bold">${totalRevenueStr} VNĐ</h2>
                    </div>
                    <i class="fas fa-hand-holding-usd fa-3x opacity-50"></i>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card revenue-card text-white p-4 shadow-sm" style="background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h6 class="text-uppercase mb-2">Lợi nhuận sàn (${commissionPercent}%)</h6>
                        <h2 class="mb-0 fw-bold">${totalCommissionStr} VNĐ</h2>
                    </div>
                    <i class="fas fa-wallet fa-3x opacity-50"></i>
                </div>
            </div>
        </div>
    </div>

    <div class="card shadow-sm p-4 border-0 rounded-3">
        <h5 class="fw-bold mb-4"><i class="fas fa-file-invoice-dollar text-primary me-2"></i>Báo cáo chiết khấu theo Cửa hàng</h5>
        <div class="table-responsive">
            <table class="table table-hover align-middle">
                <thead class="table-light">
                    <tr>
                        <th>Tên Cửa hàng</th>
                        <th>Chủ sở hữu</th>
                        <th class="text-end">Doanh thu</th>
                        <th class="text-end text-danger">Phí dịch vụ phải thu</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${reportList}" var="item">
                        <tr>
                            <td><span class="fw-bold">${item.shopName}</span></td>
                            <td>${item.ownerName}</td>
                            <td class="text-end">
                                <fmt:formatNumber value="${item.revenue}" type="number" maxFractionDigits="0"/> VNĐ
                            </td>
                            <td class="text-end fw-bold text-danger">
                                <fmt:formatNumber value="${item.commission}" type="number" maxFractionDigits="0"/> VNĐ
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty reportList}">
                        <tr>
                            <td colspan="4" class="text-center text-muted py-4">Hệ thống chưa có dữ liệu giao dịch thành công.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>