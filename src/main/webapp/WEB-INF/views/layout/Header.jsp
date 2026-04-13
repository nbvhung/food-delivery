<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<head>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<header class="shopee-header">
    <div class="container">
        <div class="navbar-top">
            <div class="navbar-left">
                Trang chủ ManhMall | <a href="${pageContext.request.contextPath}/shops/register">Trở thành Người bán</a> | <a href="${pageContext.request.contextPath}/shipper/register">Trở thành Shipper</a> | Kết nối
                <i class="fab fa-facebook"></i> <i class="fab fa-instagram"></i>
            </div>
            <div class="navbar-right">
                <a href="${pageContext.request.contextPath}/checkout/orders"><i class="fa-solid fa-ticket"></i>Đơn hàng</a>
                <a href="#"><i class="fas fa-question-circle"></i> Hỗ trợ</a>
                <a href="#"><i class="fas fa-globe"></i> Tiếng Việt</a>
                <c:choose>
                    <c:when test="${empty sessionScope.currentUser}">
                        <span style="margin: 0 5px;">|</span>
                        <a href="/register" style="font-weight: bold;">Đăng ký</a>
                        <span style="margin: 0 5px;">|</span>
                        <a href="/login" style="font-weight: bold;">Đăng nhập</a>
                    </c:when>

                    <c:otherwise>
                        <span style="margin: 0 5px;">|</span>
                        <c:choose>
                            <c:when test="${sessionScope.currentUser.role == 'SHIPPER'}">
                                <a href="${pageContext.request.contextPath}/shipper/dashboard"
                                   class="user-info"
                                   style="font-weight: bold; color: white; text-decoration: none;">
                                    <i class="fas fa-user-circle"></i> ${sessionScope.currentUser.fullName}
                                </a>
                            </c:when>

                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/profile" class="text-white text-decoration-none d-flex align-items-center">
                                    <c:choose>
                                        <c:when test="${not empty currentUser.avatar}">
                                            <img src="${pageContext.request.contextPath}${currentUser.avatar}"
                                                 class="rounded-circle me-1 border border-white"
                                                 style="width: 25px; height: 25px; object-fit: cover; border-radius: 50%">
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fas fa-user-circle fs-5 me-1"></i>
                                        </c:otherwise>
                                    </c:choose>
                                    <span class="fw-bold">${currentUser.fullName}</span>
                                </a>
                            </c:otherwise>
                        </c:choose>
                        <span style="margin: 0 5px;">|</span>
                        <a href="${pageContext.request.contextPath}/logout"
                           onclick="return confirm('Bạn có muốn đăng xuất?')"
                           style="color: #fff; font-weight: normal;">
                           Đăng xuất
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="header-main">
            <a href="/" class="logo">
                <i class="fas fa-shopping-bag" style="font-size: 40px; margin-right: 10px;"></i>
                <span class="mall-text">Mạnh Mall</span>
            </a>

            <form action="${pageContext.request.contextPath}/" method="GET" class="search-box" style="display: flex; width: 100%;">

                <input type="text"
                       name="keyword"
                       value="${keyword}"
                       placeholder="Tìm trong Mạnh Mall"
                       class="search-input">

                <button type="submit" class="search-btn">
                    <i class="fas fa-search"></i>
                </button>

            </form>

            <a href="${pageContext.request.contextPath}/cart" class="cart-icon">
                <i class="fa fa-shopping-cart"></i>
            </a>


        </div>
    </div>
</header>