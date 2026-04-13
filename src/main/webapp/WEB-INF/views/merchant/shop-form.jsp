<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cập nhật cửa hàng</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css'/>">
</head>
<body style="background-color: #f5f5f5;">

<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="container py-4" style="max-width: 760px;">
    <div class="card border-0 shadow-sm rounded-4">
        <div class="card-body p-4 p-md-5">
            <h3 class="mb-4" style="color:#ee4d2d;">Cập nhật thông tin cửa hàng</h3>

            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="<c:url value='/shops/update'/>" method="post" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${shop.id}">

                <div class="mb-3">
                    <label class="form-label fw-semibold">Tên cửa hàng</label>
                    <input type="text" class="form-control" name="name" value="${shop.name}" required>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Địa chỉ</label>
                    <textarea class="form-control" name="address" rows="3" required>${shop.address}</textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Hình ảnh</label>
                    <input type="file" class="form-control" name="imageFile" accept="image/*">
                </div>

                <c:if test="${not empty shop.image}">
                    <div class="mb-3">
                        <div class="text-muted small mb-2">Ảnh hiện tại</div>
                        <img src="${pageContext.request.contextPath}${shop.image}" alt="${shop.name}"
                             style="width: 220px; max-width: 100%; border-radius: 10px; border: 1px solid #ececec;"
                             onerror="this.onerror=null;this.src='https://via.placeholder.com/400x300?text=No+Image';">
                    </div>
                </c:if>

                <div class="d-flex flex-wrap gap-2 pt-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="fa-solid fa-floppy-disk"></i> Lưu
                    </button>
                    <a href="<c:url value='/shops/my-shop'/>" class="btn btn-outline-secondary">
                        <i class="fa-solid fa-store"></i> Về cửa hàng
                    </a>
                    <a href="<c:url value='/shops/foods'/>" class="btn btn-outline-dark">
                        <i class="fa-solid fa-utensils"></i> Quản lý món ăn
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />

</body>
</html>
