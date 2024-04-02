<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${productName}</title>
</head>
<body>
  <h1>${productName}</h1>
  <img src="${productImage}">
  <br><br>
  $${productPrice}
  <a href="add_to_cart?id=${productId}">
      <button>Add to Cart</button>
  </a>
</body>
</html>
