<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng ký trở thành Đối tác Giao hàng</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/home.css' />">
    <style>
        .register-container {
            max-width: 600px;
            margin: 40px auto;
        }
    </style>
</head>
<body style="background-color: #f8f9fa;">

<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="container register-container">
    <div class="card shadow-lg border-0 rounded-3">
        <div class="card-header text-white text-center py-4" style="background-color: #EE4D2D;">
            <h3 class="mb-0 fw-bold text-white"><i class="fas fa-motorcycle me-2"></i> ĐĂNG KÝ GIAO HÀNG</h3>
            <p class="mb-0 mt-2 text-white" style="opacity: 0.75;">Gia nhập đội ngũ Mạnh Mall ngay hôm nay!</p>
        </div>

        <div class="card-body p-4 p-md-5 bg-white">
            <h5 class="text-center text-muted mb-4">Vui lòng cung cấp thông tin phương tiện của bạn</h5>

            <form action="${pageContext.request.contextPath}/shipper/register" method="post">

                <div class="mb-4">
                    <label class="form-label fw-bold" style="color: #EE4D2D;"><i class="fas fa-phone-alt me-2" style="color: #EE4D2D;"></i>Số điện thoại liên hệ</label>
                    <input type="text" name="phone" value="${currentUser.phone}"
                           class="form-control form-control-lg bg-light"
                           required placeholder="Nhập SĐT đang sử dụng...">
                    <div class="form-text">Chúng tôi sẽ dùng số này để liên hệ nhận đơn.</div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-4">
                        <label class="form-label fw-bold" style="color: #EE4D2D;"><i class="fas fa-id-card me-2" style="color: #EE4D2D;"></i>Biển số xe</label>
                        <input type="text" name="licensePlate"
                               class="form-control form-control-lg bg-light"
                               required placeholder="VD: 29A1-123.45">
                    </div>

                    <div class="col-md-6 mb-4">
                        <label class="form-label fw-bold" style="color: #EE4D2D;"><i class="fas fa-car-side me-2" style="color: #EE4D2D;"></i>Loại xe</label>
                        <input type="text" name="vehicleType"
                               class="form-control form-control-lg bg-light"
                               required placeholder="VD: Honda Wave">
                    </div>
                </div>

                <hr class="my-4 text-muted">

                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-lg py-3 fw-bold text-uppercase shadow-sm" style="background-color: #EE4D2D; color: white; border-color: #0d2a57;">
                        <i class="fas fa-paper-plane me-2"></i> Gửi yêu cầu mở tài khoản
                    </button>
                    <a href="${pageContext.request.contextPath}/" class="btn btn-lg py-3 shadow-sm mt-2" style="background-color: #f8f9fa; color: #EE4D2D; border-color: #f8f9fa;">
                        Quay lại trang chủ
                    </a>
                </div>

            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />

</body>
</html>