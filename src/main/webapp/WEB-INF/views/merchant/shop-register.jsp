<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dang ky cua hang</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css'/>">
    <style>
        .register-card {
            border: none;
            border-radius: 16px;
            box-shadow: 0 10px 24px rgba(0, 0, 0, 0.08);
        }
        .image-preview-container {
            margin-top: 0.75rem;
            border: 2px dashed #e5e7eb;
            border-radius: 10px;
            padding: 1rem;
            text-align: center;
            background: #fff;
        }
        .image-preview-container img {
            max-width: 100%;
            max-height: 220px;
            border-radius: 8px;
            display: none;
        }
    </style>
</head>
<body style="background-color: #f5f5f5;">

<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-8 col-xl-7">
            <div class="register-card card">
                <div class="card-body p-4 p-md-5">
                    <h4 class="fw-bold mb-4" style="color:#ee4d2d;">
                        <i class="fa-solid fa-shop me-2"></i>Dang ky cua hang moi
                    </h4>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>
                    <c:if test="${not empty info}">
                        <div class="alert alert-info">${info}</div>
                    </c:if>

                    <form action="<c:url value='/shops/register'/>" method="post" enctype="multipart/form-data">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Ten cua hang</label>
                            <input type="text" name="name" class="form-control" placeholder="Nhap ten cua hang" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Dia chi</label>
                            <textarea name="address" class="form-control" rows="3" placeholder="So nha, duong, phuong/xa..." required></textarea>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Hinh anh dai dien</label>
                            <input type="file" name="imageFile" class="form-control" id="shopImage" accept="image/*" required>

                            <div class="image-preview-container" id="previewBox">
                                <i class="fas fa-image fa-2x text-muted mb-2 d-block" id="placeholderIcon"></i>
                                <span class="text-muted d-block" id="placeholderText">Chua co tep nao duoc chon</span>
                                <img id="imgPreview" src="#" alt="Preview">
                            </div>
                        </div>

                        <div class="d-flex justify-content-end gap-2 mt-4">
                            <a href="<c:url value='/shops'/>" class="btn btn-light border">Huy</a>
                            <button type="submit" class="btn btn-primary">Gui yeu cau xet duyet</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    const shopImage = document.getElementById('shopImage');
    const imgPreview = document.getElementById('imgPreview');
    const placeholderIcon = document.getElementById('placeholderIcon');
    const placeholderText = document.getElementById('placeholderText');

    shopImage.onchange = () => {
        const [file] = shopImage.files;
        if (file) {
            imgPreview.src = URL.createObjectURL(file);
            imgPreview.style.display = 'inline-block';
            placeholderIcon.style.display = 'none';
            placeholderText.style.display = 'none';
        }
    };
</script>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />

</body>
</html>
