<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hồ sơ cá nhân</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css' />">
    <link rel="stylesheet" href="<c:url value='/css/profile.css' />">
</head>
<body class="bg-light">

<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="profile-page">
    <div class="container profile-container">
        <div class="card shadow-sm border-0 rounded-4">
            <div class="card-header text-white text-center py-3 rounded-top-4" style="background-color: #EE4D2D;">
                <h4 class="mb-0"><i class="fas fa-id-badge me-2"></i> Hồ sơ của tôi</h4>
            </div>
            <div class="card-body p-4 p-md-5">
                <form action="${pageContext.request.contextPath}/profile/update" method="post" enctype="multipart/form-data">

                    <div class="text-center mb-4">
                        <c:choose>
                            <c:when test="${not empty user.avatar}">
                                <img src="${pageContext.request.contextPath}${user.avatar}" class="avatar-preview shadow-sm" alt="Avatar">
                            </c:when>
                            <c:otherwise>
                                <img src="https://ui-avatars.com/api/?name=${user.fullName}&background=random" class="avatar-preview shadow-sm" alt="Default Avatar">
                            </c:otherwise>
                        </c:choose>
                        <div class="mt-3">
                            <label for="avatarFile" class="form-label fw-bold">Thay đổi ảnh đại diện</label>
                            <input class="form-control" type="file" id="avatarFile" name="avatarFile" accept="image/png, image/jpeg">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold">Tên đăng nhập</label>
                        <input type="text" class="form-control bg-light" value="${user.username}" disabled>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold">Họ và tên</label>
                        <input type="text" name="fullName" class="form-control" value="${user.fullName}" required>
                    </div>

                    <div class="mb-4">
                        <label class="form-label fw-bold">Số điện thoại</label>
                        <input type="text" name="phone" class="form-control" value="${user.phone}">
                    </div>

                    <button type="submit" class="btn btn-submit w-100 py-2 fw-bold text-uppercase">
                        Lưu thay đổi
                    </button>
                </form>
            </div>
        </div>

        <c:if test="${sessionScope.currentUser.role == 'MERCHANT'}">
            <div class="card shadow-sm border-0 rounded-4 mt-4">
                <div class="card-header text-white text-center py-3 rounded-top-4" style="background-color: #1f2937;">
                    <h5 class="mb-0"><i class="fas fa-store me-2"></i> Thông tin cửa hàng</h5>
                </div>
                <div class="card-body p-4">
                    <c:choose>
                        <c:when test="${not empty merchantShop}">
                            <div class="d-flex flex-column flex-md-row gap-3 align-items-md-center">
                                <img src="${pageContext.request.contextPath}${merchantShop.image}"
                                     alt="${merchantShop.name}"
                                     style="width: 120px; height: 120px; object-fit: cover; border-radius: 12px; border: 1px solid #eee;"
                                     onerror="this.onerror=null;this.src='https://via.placeholder.com/240x240?text=No+Image';">
                                <div class="flex-grow-1">
                                    <h5 class="mb-1">${merchantShop.name}</h5>
                                    <p class="text-muted mb-1"><i class="fas fa-location-dot me-1"></i>${merchantShop.address}</p>
                                    <span class="badge ${merchantShop.status == 'ACTIVE' ? 'bg-success' : (merchantShop.status == 'PENDING' ? 'bg-warning text-dark' : 'bg-danger')}">
                                        ${merchantShop.status}
                                    </span>
                                </div>
                            </div>

                            <div class="d-flex flex-wrap gap-2 mt-4">
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/shops/my-shop">
                                    <i class="fas fa-store me-1"></i> Xem cửa hàng
                                </a>
                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/shops/edit">
                                    <i class="fas fa-pen me-1"></i> Sửa quán
                                </a>
                                <a class="btn btn-dark" href="${pageContext.request.contextPath}/shops/foods">
                                    <i class="fas fa-utensils me-1"></i> Quản lý món
                                </a>
                                <a class="btn btn-success" href="${pageContext.request.contextPath}/shops/foods/create">
                                    <i class="fas fa-plus me-1"></i> Thêm món
                                </a>
                                <a class="btn btn-warning text-dark" href="${pageContext.request.contextPath}/shops/revenue">
                                    <i class="fas fa-chart-line me-1"></i> Doanh thu
                                </a>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="alert alert-warning mb-3">
                                Tài khoản Merchant chưa có cửa hàng.
                            </div>
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/shops/register">
                                <i class="fas fa-shop me-1"></i> Đăng ký mở cửa hàng
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />
</body>
</html>
