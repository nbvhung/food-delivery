<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
  <title>Chi tiết món ăn</title>
  <link rel="stylesheet" href="/css/food.css">
</head>
<body>

<h2>🍽️ CHI TIẾT MÓN ĂN</h2>

<p><b>ID:</b> ${food.id}</p>
<p><b>Tên món:</b> ${food.name}</p>
<p><b>Giá:</b> ${food.priceFormatted}</p>

<p><b>Hình ảnh:</b><br>
  <img src="${pageContext.request.contextPath}${food.image}" width="200">

</p>

<p><b>Mô tả:</b> ${food.description}</p>
<p><b>Shop:</b> ${food.shop.name}</p>
<p><b>Danh mục:</b> ${food.category.name}</p>

<br>
<a href="${pageContext.request.contextPath}/foods">⬅ Quay lại danh sách</a>

</body>
</html>
