<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quan ly mon an</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css'/>">
</head>
<body style="background-color: #f5f5f5;">

<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="container py-4">
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
        <div>
            <h3 class="m-0" style="color:#ee4d2d;">Quan ly mon an</h3>
            <div class="text-secondary small">Cua hang: ${shop.name}</div>
        </div>

        <div class="d-flex gap-2">
            <a class="btn btn-primary" href="<c:url value='/shops/foods/create'/>">
                <i class="fa-solid fa-plus"></i> Them mon
            </a>
            <a class="btn btn-outline-secondary" href="<c:url value='/shops/my-shop'/>">
                <i class="fa-solid fa-store"></i> Ve cua hang
            </a>
        </div>
    </div>

    <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card border-0 shadow-sm rounded-4">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th style="width: 72px;">ID</th>
                        <th style="width: 120px;">Anh</th>
                        <th>Mon an</th>
                        <th style="width: 160px;">Gia</th>
                        <th style="width: 160px;">Danh muc</th>
                        <th style="width: 180px;" class="text-center">Hanh dong</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${foods}" var="food">
                        <tr>
                            <td>#${food.id}</td>
                            <td>
                                <img src="${pageContext.request.contextPath}${food.image}" alt="${food.name}"
                                     style="width: 88px; height: 68px; object-fit: cover; border-radius: 8px; border: 1px solid #efefef;"
                                     onerror="this.onerror=null;this.src='https://via.placeholder.com/176x136?text=No+Image';">
                            </td>
                            <td>
                                <div class="fw-semibold">${food.name}</div>
                                <div class="text-secondary small">${food.description}</div>
                            </td>
                            <td class="text-danger fw-bold">
                                <fmt:formatNumber value="${food.price}" type="number" groupingUsed="true"/> đ
                            </td>
                            <td>${food.category.name}</td>
                            <td class="text-center">
                                <div class="d-inline-flex gap-1">
                                    <a class="btn btn-sm btn-outline-primary" href="<c:url value='/shops/foods/${food.id}'/>">
                                        <i class="fa-solid fa-eye"></i>
                                    </a>
                                    <a class="btn btn-sm btn-outline-secondary" href="<c:url value='/shops/foods/edit/${food.id}'/>">
                                        <i class="fa-solid fa-pen"></i>
                                    </a>
                                    <a class="btn btn-sm btn-outline-danger"
                                       href="<c:url value='/shops/foods/delete/${food.id}'/>"
                                       onclick="return confirm('Ban chac chan muon xoa mon nay?')">
                                        <i class="fa-solid fa-trash"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty foods}">
                        <tr>
                            <td colspan="6" class="text-center py-4 text-secondary">
                                Chua co mon an nao. Hay them mon dau tien cho cua hang.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />

</body>
</html>
