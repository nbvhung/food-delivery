<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:choose><c:when test="${empty food.id}">Them mon an</c:when><c:otherwise>Chinh sua mon an</c:otherwise></c:choose></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css'/>">
</head>
<body style="background-color: #f5f5f5;">

<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<c:choose>
    <c:when test="${empty food.id}">
        <c:url var="formAction" value="/shops/foods/save" />
    </c:when>
    <c:otherwise>
        <c:url var="formAction" value="/shops/foods/update/${food.id}" />
    </c:otherwise>
</c:choose>

<div class="container py-4" style="max-width: 760px;">
    <div class="card border-0 shadow-sm rounded-4">
        <div class="card-body p-4 p-md-5">
            <h3 class="mb-4" style="color:#ee4d2d;">
                <c:choose>
                    <c:when test="${empty food.id}">Them mon an moi</c:when>
                    <c:otherwise>Cap nhat mon an</c:otherwise>
                </c:choose>
            </h3>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="${formAction}" method="post" enctype="multipart/form-data">
                <div class="mb-3">
                    <label class="form-label fw-semibold">Ten mon</label>
                    <input type="text" class="form-control" name="name" value="${food.name}" required>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Gia (VND)</label>
                    <input type="number" min="1000" step="1000" class="form-control" name="price" value="${food.price}" required>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Danh muc</label>
                    <select class="form-select" name="categoryId" required>
                        <option value="">-- Chon danh muc --</option>
                        <c:forEach items="${categories}" var="cate">
                            <option value="${cate.id}" ${food.category != null && food.category.id == cate.id ? 'selected' : ''}>
                                ${cate.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Mo ta ngan</label>
                    <textarea class="form-control" name="description" rows="4">${food.description}</textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Hinh anh mon an</label>
                    <input type="file" class="form-control" name="imageFile" accept="image/*">
                </div>

                <c:if test="${not empty food.image}">
                    <div class="mb-3">
                        <div class="text-muted small mb-2">Anh hien tai</div>
                        <img src="${pageContext.request.contextPath}${food.image}" alt="${food.name}"
                             style="width: 220px; max-width: 100%; border-radius: 10px; border: 1px solid #ececec;"
                             onerror="this.onerror=null;this.src='https://via.placeholder.com/400x300?text=No+Image';">
                    </div>
                </c:if>

                <div class="d-flex flex-wrap gap-2 pt-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="fa-solid fa-floppy-disk"></i> Luu
                    </button>
                    <a href="<c:url value='/shops/foods'/>" class="btn btn-outline-secondary">
                        <i class="fa-solid fa-arrow-left"></i> Quay lai
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />

</body>
</html>
