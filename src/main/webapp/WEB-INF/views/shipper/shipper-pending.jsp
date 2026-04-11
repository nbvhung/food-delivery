<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Yêu cầu đang chờ duyệt | Food Delivery</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
  <style>
    .pending-container {
      min-height: 70vh;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .status-card {
      border: none;
      border-radius: 20px;
      box-shadow: 0 10px 30px rgba(0,0,0,0.1);
      padding: 40px;
      text-align: center;
      max-width: 500px;
      width: 100%;
    }
    .icon-wrapper {
      width: 100px;
      height: 100px;
      background-color: #fff4e5;
      color: #ff9800;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 50px;
      margin: 0 auto 20px;
      /* Hiệu ứng hoạt ảnh rung nhẹ thu hút chú ý */
      animation: pulse 2s infinite;
    }
    @keyframes pulse {
      0% { transform: scale(1); }
      50% { transform: scale(1.05); }
      100% { transform: scale(1); }
    }
    .btn-home {
      background-color: #ff4757;
      color: white;
      border-radius: 30px;
      padding: 10px 30px;
      font-weight: 600;
      transition: 0.3s;
    }
    .btn-home:hover {
      background-color: #e84118;
      color: white;
      transform: translateY(-2px);
    }
  </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/Header.jsp" />
<div class="container pending-container">
  <div class="card status-card">
    <div class="icon-wrapper">
      <i class="fa-solid fa-clock-rotate-left"></i>
    </div>
    <h2 class="fw-bold mb-3">Đang chờ phê duyệt!</h2>
    <p class="text-muted mb-4">
      Chào <strong>${currentUser.fullName}</strong>, yêu cầu trở thành đối tác giao hàng của bạn đã được gửi thành công.
      Quản trị viên đang xem xét hồ sơ của bạn.
    </p>

    <div class="alert alert-info border-0 shadow-sm mb-4">
      <i class="fa-solid fa-circle-info me-2"></i>
      Thời gian xử lý dự kiến: 24h - 48h làm việc.
    </div>

    <div class="d-grid gap-2 mb-3">
      <a href="${pageContext.request.contextPath}/shipper/register/review" class="btn btn-outline-warning rounded-pill">
        <i class="fa-solid fa-file-pen me-2"></i>Xem lại đơn đã gửi
      </a>
    </div>

    <div class="d-grid">
      <a href="/" class="btn btn-home">
        <i class="fa-solid fa-house me-2"></i> Quay lại trang chủ
      </a>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>