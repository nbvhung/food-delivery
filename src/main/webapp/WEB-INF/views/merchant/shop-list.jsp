<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sach cua hang</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css'/>">
    <style>
        .shop-card {
            border: 1px solid #ececec;
            border-radius: 14px;
            overflow: hidden;
            background: #fff;
            cursor: pointer;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            height: 100%;
        }
        .shop-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 24px rgba(0, 0, 0, 0.08);
        }
        .shop-card img {
            width: 100%;
            height: 180px;
            object-fit: cover;
            background: #f6f6f6;
        }
        .shop-card .content {
            padding: 14px;
        }
        .shop-card .name {
            font-weight: 700;
            margin-bottom: 6px;
            color: #212529;
            display: -webkit-box;
            -webkit-line-clamp: 1;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }
        .shop-card .address {
            color: #6c757d;
            font-size: 14px;
            min-height: 40px;
        }
    </style>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body style="background-color: #f5f5f5;">

<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="container py-4">
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
        <h2 class="m-0" style="color: #ee4d2d; font-size: 28px;">Danh sach cua hang</h2>

        <div class="d-flex gap-2">
            <c:if test="${not empty sessionScope.currentUser and sessionScope.currentUser.role == 'MERCHANT'}">
                <a class="btn btn-outline-primary" href="<c:url value='/shops/my-shop'/>">
                    <i class="fa-solid fa-store"></i> Quan cua toi
                </a>
            </c:if>
            <c:if test="${not empty sessionScope.currentUser and sessionScope.currentUser.role != 'MERCHANT'}">
                <a class="btn btn-primary" href="<c:url value='/shops/register'/>">
                    <i class="fa-solid fa-shop"></i> Dang ky mo quan
                </a>
            </c:if>
        </div>
    </div>

    <c:if test="${not empty info}">
        <div class="alert alert-info">${info}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <c:choose>
        <c:when test="${empty shops}">
            <div class="alert alert-secondary mb-0">Chua co cua hang nao dang hoat dong.</div>
        </c:when>
        <c:otherwise>
            <div class="row g-3">
                <c:forEach items="${shops}" var="shop">
                    <div class="col-12 col-sm-6 col-lg-4">
                        <div class="shop-card" onclick="window.location.href='${pageContext.request.contextPath}/shops/${shop.id}'">
                            <img src="${pageContext.request.contextPath}${shop.image}" alt="${shop.name}"
                                 onerror="this.onerror=null;this.src='https://via.placeholder.com/640x360?text=No+Image';">
                            <div class="content">
                                <div class="name">${shop.name}</div>
                                <div class="address"><i class="fa-solid fa-location-dot"></i> ${shop.address}</div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />

<c:if test="${not empty message and message == 'pending'}">
    <script>
        Swal.fire({
            title: 'Dang ky thanh cong',
            text: 'Yeu cau mo quan cua ban da duoc gui va dang cho duyet.',
            icon: 'success',
            confirmButtonColor: '#0d6efd',
            confirmButtonText: 'Dong y'
        });
    </script>
</c:if>

</body>
</html>
