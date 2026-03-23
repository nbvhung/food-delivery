<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hồ sơ Shipper</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="<c:url value='/css/home.css' />">
    <link rel="stylesheet" href="<c:url value='/css/shipper.css' />">
</head>
<body>
<jsp:include page="/WEB-INF/views/layout/Header.jsp" />

<div class="shipper-container">
    <div class="shipper-header">
        <div>
            <h2>Hồ sơ Shipper</h2>
            <span class="shipper-badge">Profile</span>
        </div>
        <nav class="shipper-nav">
            <a href="${pageContext.request.contextPath}/shipper/dashboard" class="tab">Tổng quan</a>
            <a href="${pageContext.request.contextPath}/shipper/profile" class="tab active">Hồ sơ</a>
            <a href="${pageContext.request.contextPath}/shipper/stats" class="tab">Thống kê</a>
        </nav>
    </div>

    <div class="shipper-card">
        <form action="${pageContext.request.contextPath}/shipper/profile" method="post">
            <div style="margin-bottom: 12px;">
                <label><b>Avatar URL</b></label>
                <input type="text" name="avatar" value="${shipper.avatar}" style="width:100%; padding:8px;" placeholder="https://..." />
            </div>

            <div style="margin-bottom: 12px;">
                <label><b>Biển số xe mới</b> (cần admin duyệt)</label>
                <input type="text" name="licensePlate" value="" style="width:100%; padding:8px;" placeholder="51H1-12345" />
            </div>

            <c:if test="${not empty shipper.licensePlate}">
                <p><b>Biển số hiện tại:</b> ${shipper.licensePlate}</p>
            </c:if>
            <c:if test="${not empty shipper.pendingLicensePlate}">
                <p style="color:#b7791f;"><b>Biển số chờ duyệt:</b> ${shipper.pendingLicensePlate}</p>
            </c:if>

            <button type="submit" class="shipper-action">Lưu hồ sơ</button>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/Footer.jsp" />
</body>
</html>
