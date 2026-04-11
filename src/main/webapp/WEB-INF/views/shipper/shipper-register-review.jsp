<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Xem lại form đăng ký shipper</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shipper.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        /* Form Layout & Typography */
        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        .form-group {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }
        .form-group.full-width {
            grid-column: span 2;
        }
        .form-label {
            font-size: 14px;
            font-weight: 600;
            color: #475569;
        }

        /* Modern Input Styling */
        .input-wrapper {
            position: relative;
            display: flex;
            align-items: center;
        }
        .input-wrapper i {
            position: absolute;
            left: 14px;
            color: #94a3b8;
            font-size: 16px;
            transition: color 0.3s;
        }
        .form-control {
            width: 100%;
            padding: 12px 16px 12px 42px; /* padding-left chừa chỗ cho icon */
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            font-size: 15px;
            color: #334155;
            transition: all 0.3s ease;
            background: #fff;
            outline: none;
            box-sizing: border-box;
        }

        /* Focus & Readonly States */
        .form-control:focus {
            border-color: #ee4d2d;
            box-shadow: 0 0 0 3px rgba(238, 77, 45, 0.15);
        }
        .form-control:focus + i, .input-wrapper:focus-within i {
            color: #ee4d2d;
        }
        .form-control[readonly] {
            background: #f8fafc;
            color: #64748b;
            border-color: #e2e8f0;
            cursor: not-allowed;
        }

        /* Action Buttons Area */
        .form-actions {
            display: flex;
            gap: 12px;
            margin-top: 28px;
            padding-top: 20px;
            border-top: 1px dashed #e2e8f0;
        }

        /* Responsive */
        @media (max-width: 640px) {
            .form-grid {
                grid-template-columns: 1fr;
            }
            .form-group.full-width {
                grid-column: span 1;
            }
            .form-actions {
                flex-direction: column;
            }
            .form-actions .shipper-action {
                width: 100%;
                justify-content: center;
            }
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="shipper-container">
    <div class="shipper-header">
        <div>
            <h2>Chi tiết đơn đăng ký</h2>
            <span class="shipper-badge"><i class="fas fa-clock-rotate-left"></i> Chờ duyệt</span>
        </div>
    </div>

    <div class="shipper-card" style="max-width: 760px; margin: 0 auto;">
        <form action="${pageContext.request.contextPath}/shipper/register/modify" method="post">
            <div class="form-grid">

                <div class="form-group">
                    <label class="form-label">Họ và tên</label>
                    <div class="input-wrapper">
                        <i class="fas fa-user"></i>
                        <input type="text" class="form-control" value="${user.fullName}" readonly>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Tài khoản</label>
                    <div class="input-wrapper">
                        <i class="fas fa-id-badge"></i>
                        <input type="text" class="form-control" value="${user.username}" readonly>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Số điện thoại</label>
                    <div class="input-wrapper">
                        <i class="fas fa-phone"></i>
                        <input type="text" name="phone" class="form-control" value="${user.phone}">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Biển số xe</label>
                    <div class="input-wrapper">
                        <i class="fas fa-motorcycle"></i>
                        <input type="text" name="licensePlate" class="form-control" value="${shipperProfile.licensePlate}" placeholder="VD: 29A1-123.45">
                    </div>
                </div>

                <div class="form-group full-width">
                    <label class="form-label">Loại xe đăng ký</label>
                    <div class="input-wrapper">
                        <i class="fas fa-truck-fast"></i>
                        <input type="text" name="vehicleType" class="form-control" value="${shipperProfile.vehicleType}" placeholder="VD: Honda Wave Alpha">
                    </div>
                </div>

            </div>

            <div class="form-actions">
                <button type="submit" class="shipper-action">
                    <i class="fas fa-floppy-disk"></i> Lưu thay đổi
                </button>
                <a href="${pageContext.request.contextPath}/shipper/register" class="shipper-action secondary" style="text-decoration:none;">
                    <i class="fas fa-arrow-left"></i> Quay lại
                </a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />

<c:if test="${not empty message}">
    <script>
        if ("${message}" === "update_shipper_success") {
            Swal.fire({
                icon: "success",
                title: "Thành công",
                text: "Chỉnh sửa thông tin thành công!",
                confirmButtonColor: "#ee4d2d",
            });
        }
    </script>
</c:if>

</body>
</html>