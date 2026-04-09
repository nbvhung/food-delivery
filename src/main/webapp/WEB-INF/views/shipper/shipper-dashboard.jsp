<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tổng quan Shipper</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css' />">
    <link rel="stylesheet" href="<c:url value='/css/shipper.css' />">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="shipper-container">
    <div class="shipper-header">
        <h2>Tổng quan</h2>
        <nav class="shipper-nav">
            <a href="${pageContext.request.contextPath}/shipper/dashboard" class="tab active">
                <i class="fas fa-chart-pie"></i><span class="nav-text">Tổng quan</span>
            </a>
            <a href="${pageContext.request.contextPath}/shipper/waiting" class="tab">
                <i class="fas fa-box"></i><span class="nav-text">Đơn chờ nhận</span>
            </a>
            <a href="${pageContext.request.contextPath}/shipper/delivering" class="tab">
                <i class="fas fa-motorcycle"></i><span class="nav-text">Đang giao</span>
            </a>
            <a href="${pageContext.request.contextPath}/shipper/stats" class="tab">
                <i class="fas fa-chart-line"></i><span class="nav-text">Thống kê</span>
            </a>
        </nav>
    </div>

    <div class="shipper-card shipper-profile-card">
        <div class="shipper-profile">
            <div class="shipper-avatar" style="position: relative; width: 110px; height: 110px; flex-shrink: 0;">

                <form action="${pageContext.request.contextPath}/shipper/update-avatar" method="POST" enctype="multipart/form-data" style="margin: 0; width: 100%; height: 100%;">

                    <label style="cursor: pointer; display: block; width: 100%; height: 100%; margin: 0;">

                        <c:choose>
                            <c:when test="${not empty shipper.avatar}">
                                <img src="${pageContext.request.contextPath}${shipper.avatar}" alt="Avatar" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%; border: 3px solid #e5e7eb;">
                            </c:when>
                            <c:otherwise>
                                <div class="avatar-placeholder" style="width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; border-radius: 50%; background-color: #f3f4f6; border: 3px solid #e5e7eb;">
                                    <i class="fas fa-user" style="font-size: 40px; color: #9ca3af;"></i>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <div style="position: absolute; bottom: 0; right: 0; background: #fff; color: #666; border-radius: 50%; width: 28px; height: 28px; display: flex; align-items: center; justify-content: center; border: 1px solid #ccc; box-shadow: 0 1px 3px rgba(0,0,0,0.2);">
                            <i class="fas fa-camera" style="font-size: 14px;"></i>
                        </div>

                        <input type="file" name="avatarFile" accept="image/png, image/jpeg, image/jpg" style="display: none;"
                               onchange="if(confirm('Bạn có chắc chắn muốn cập nhật ảnh đại diện này không?')) { this.form.submit(); } else { this.value = ''; }">

                    </label>
                </form>
            </div>
            <div class="shipper-info">
                <h3>${shipper.fullName}</h3>
                <div class="shipper-details">
                    <div class="detail-item">
                        <i class="fas fa-motorcycle"></i>
                        <span>
                            <c:choose>
                                <c:when test="${not empty shipper.licensePlate}">
                                    ${shipper.licensePlate}
                                </c:when>
                                <c:otherwise>
                                    Bạn chưa được duyệt biển số
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>

                    <div class="detail-item" style="background: rgba(16, 185, 129, 0.2); border: 1px solid rgba(16, 185, 129, 0.4);">
                        <i class="fas fa-circle" style="color: #10b981; font-size: 12px;"></i>
                        <span style="color: #fff;">Đang hoạt động</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="shipper-card">
        <h3 style="margin-top: 0; margin-bottom: 20px; font-size: 20px; color: #333;">Thống kê tổng quan</h3>

        <div class="shipper-summary">
            <div class="summary-item">
                <div class="summary-icon" style="color: #3b82f6; background: #eff6ff;">
                    <i class="fas fa-box-open"></i>
                </div>
                <div class="summary-info">
                    <h3>Đơn chờ nhận</h3>
                    <p>${readyCount}</p>
                    <span class="summary-note">Sẵn sàng để nhận</span>
                </div>
            </div>

            <div class="summary-item">
                <div class="summary-icon" style="color: #10b981; background: #ecfdf5;">
                    <i class="fas fa-motorcycle"></i>
                </div>
                <div class="summary-info">
                    <h3>Đang giao của tôi</h3>
                    <p>${deliveringCount}</p>
                    <span class="summary-note">Đang xử lý</span>
                </div>
            </div>

            <div class="summary-item">
                <div class="summary-icon" style="color: #8b5cf6; background: #f5f3ff;">
                    <i class="fas fa-check-circle"></i>
                </div>
                <div class="summary-info">
                    <h3>Đã giao thành công</h3>
                    <p>${deliveredCount}</p>
                    <span class="summary-note">Tích lũy đến hiện tại</span>
                </div>
            </div>

            <div class="summary-item">
                <div class="summary-icon" style="color: #f59e0b; background: #fffbeb;">
                    <i class="fas fa-star"></i>
                </div>
                <div class="summary-info">
                    <h3>Điểm đánh giá</h3>
                    <div class="rating" title="${avgRating}">
                        <div class="rating-back">★★★★★</div>
                        <div class="rating-front" style="width: ${avgRating * 20}%">★★★★★</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />
</body>
</html>
