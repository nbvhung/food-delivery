<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Food Form</title>
    <link rel="stylesheet" href="/css/food.css">
</head>
<body>

<h2>
    <c:if test="${food.id == null}">➕ Thêm món ăn</c:if>
    <c:if test="${food.id != null}">✏️ Sửa món ăn</c:if>
</h2>

<form action="/foods/save" method="post">

    <input type="hidden" name="id" value="${food.id}">

    <label>Tên món</label>
    <input type="text" name="name" value="${food.name}" required>

    <label>Giá</label>
    <input type="number" step="0.01" name="price" value="${food.price}" required>

    <label>Link hình ảnh</label>
    <input type="file" name="image" value="${food.image}" accept="image/*">

    <label>Mô tả</label>
    <textarea name="description">${food.description}</textarea>

    <label>Shop</label>
    <select name="shopId" required>
        <c:forEach items="${shops}" var="shop">
            <option value="${shop.id}"
                    <c:if test="${food.shop != null && food.shop.id == shop.id}">
                        selected
                    </c:if>>
                    ${shop.name}
            </option>
        </c:forEach>
    </select>

    <label>Danh mục</label>
    <select name="categoryId" required>
        <c:forEach items="${categories}" var="cate">
            <option value="${cate.id}"
                    <c:if test="${food.category != null && food.category.id == cate.id}">
                        selected
                    </c:if>>
                    ${cate.name}
            </option>
        </c:forEach>
    </select>

    <div class="form-actions">
        <button type="submit">💾 Lưu</button>
        <a class="btn-back" href="/foods">⬅ Quay lại</a>
    </div>

</form>

</body>
</html>
