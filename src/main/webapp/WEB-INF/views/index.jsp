<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang chủ Mạnh Mall</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css?v=3'/>">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body style="background-color: #f5f5f5;"> <jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="container">
    <div class="main-layout">

        <aside class="sidebar">
            <h3 class="filter-title"><i class="fas fa-list"></i> Danh mục</h3>
            <ul class="cat-list" style="list-style-type: none; padding-left: 0;">
                <c:forEach items="${categories}" var="cat">
                    <li class="mb-2">
                        <a href="?categoryId=${cat.id}"
                           class="text-decoration-none ${categoryId == cat.id ? 'fw-bold text-primary' : 'text-dark'}">
                            ${cat.name}
                        </a>
                    </li>
                </c:forEach>

                <li class="mt-3 pt-3" style="border-top: 1px dashed #ddd;">
                    <a href="${pageContext.request.contextPath}/"
                       class="text-decoration-none ${categoryId == null ? 'fw-bold text-primary' : 'text-dark'}">
                        Tất cả món ăn
                    </a>
                </li>

                <li class="mt-2 pt-2" style="border-top: 1px solid #f0f0f0;">
                    <a href="${pageContext.request.contextPath}/shops" class="text-decoration-none text-success fw-bold">
                        Danh sách Cửa hàng
                    </a>
                </li>
            </ul>
            <div class="price-range-filter" style="margin-top: 30px; border-top: 1px solid #eee; padding-top: 20px;">
                    <h3 class="filter-title" style="font-size: 16px; margin-bottom: 15px;">Khoảng Giá</h3>
                    <form action="${pageContext.request.contextPath}/" method="GET">
                        <div class="price-input-group" style="display: flex; align-items: center; gap: 5px; margin-bottom: 15px;">
                            <input type="number" name="minPrice"
                                   value="<fmt:formatNumber value='${minPrice}' pattern='#' />"
                                   placeholder="₫ TỪ" class="price-input">

                            <span style="color: #bdbdbd">-</span>

                            <input type="number" name="maxPrice"
                                   value="<fmt:formatNumber value='${maxPrice}' pattern='#' />"
                                   placeholder="₫ ĐẾN" class="price-input">
                        </div>
                        <button type="submit" class="btn-apply">ÁP DỤNG</button>
                        <a href="${pageContext.request.contextPath}/"
                            style="display: block; text-align: center; margin-top: 10px; font-size: 12px; color: #666; text-decoration: none; border: 1px solid #ddd; padding: 5px; border-radius: 2px; background: #fff;">
                            XÓA TẤT CẢ
                        </a>
                    </form>
            </div>
        </aside>

        <div class="main-content">
            <h2 style="margin: 0 0 20px 0; color: #ee4d2d; border-bottom: 2px solid #ee4d2d; display: inline-block; padding-bottom: 5px;">
                GỢI Ý HÔM NAY
            </h2>

            <div class="sort-bar">
                <span>Sắp xếp theo: </span>

                <a href="?sort=name,asc"
                   class="sort-btn ${currentSort == 'name,asc' ? 'active' : ''}">
                   Phổ biến
                </a>

                <a href="?sort=id,desc"
                   class="sort-btn ${currentSort == 'id,desc' || empty currentSort ? 'active' : ''}">
                   Mới nhất
                </a>

                <a href="?sort=price,desc"
                   class="sort-btn ${currentSort == 'price,desc' ? 'active' : ''}">
                   Best Seller
                </a>
                <div class="price-filter-dropdown">
                        <div class="dropdown-label">
                            <span>Giá</span>
                            <i class="fas fa-chevron-down"></i>
                        </div>
                        <ul class="dropdown-menu">
                            <li><a href="?sort=price,asc">Giá: Thấp đến Cao</a></li>
                            <li><a href="?sort=price,desc">Giá: Cao đến Thấp</a></li>
                        </ul>
                    </div>
            </div>

            <div class="product-grid">
                <c:forEach items="${foods}" var="f">
                    <a href="${pageContext.request.contextPath}/foods/detail/${f.id}"
                       class="product-link"
                       style="text-decoration: none;">

                        <div class="product-card">
                            <span class="mall-badge">Mall</span>

                            <img src="<c:url value='${f.image}'/>"
                                 alt="${f.name}"
                                 onerror="this.onerror=null; this.src='https://via.placeholder.com/200x200?text=No+Image';"
                                 style="width: 100%; aspect-ratio: 1 / 1; object-fit: cover; border-bottom: 1px solid #f0f0f0;">

                            <div class="product-info">
                                <p class="product-name" style="color: #333; margin-bottom: 8px;">${f.name}</p>

                                <p class="product-price" style="text-decoration: none;">
                                    <fmt:formatNumber value="${f.price}" type="number" groupingUsed="true"/> đ
                                </p>

                                <p class="product-description" style="color: #666; font-size: 12px;">${f.description}</p>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </div>
            <c:if test="${totalPages > 0}">
                <div class="pagination">
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a href="?page=${i-1}&sort=${currentSort}${not empty keyword ? '&keyword=' += keyword : ''}${not empty categoryId ? '&categoryId=' += categoryId : ''}"
                           class="page-btn ${currentPage == i-1 ? 'active' : ''}">
                            ${i}
                        </a>
                    </c:forEach>
                </div>
            </c:if>

            <c:if test="${totalPages == 0}">
                <div style="text-align: center; padding: 50px; color: #666;">
                    <img src="https://deo.shopeemobile.com/shopee/shopee-pcmall-live-sg/assets/a60759ad1dabe909c46a817ecbf71878.png" style="width: 100px; margin-bottom: 20px;">
                    <p>Tiếc quá! Không tìm thấy món nào phù hợp với khoảng giá này.</p>
                    <a href="${pageContext.request.contextPath}/" style="color: #ee4d2d; text-decoration: none;">Xóa bộ lọc và tìm lại</a>
                </div>
            </c:if>
        </div> </div> </div> ```






<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />

<c:if test="${not empty message}">
    <script>
        const status = "${message}";
        if (status === "pending_shipper") {
            Swal.fire({
                title: 'Đăng ký thành công!',
                text: 'Yêu cầu trở thành shipper của bạn đã được gửi.',
                icon: 'success',
                confirmButtonColor: '#0d6efd',
                confirmButtonText: 'Đồng ý'
            });
        }
    </script>
</c:if>

</body>
</html>