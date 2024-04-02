<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Shopping Cart</title>
</head>
<body>
    <h1>Shopping Cart</h1>

    <form action="update_cart" method="post">
        <ul>
            ${itemsList}
        </ul>

        &emsp;&emsp;&emsp;&emsp;&emsp;Total: $${totalPrice} <button>Update Cart</button>
    </form>
    &emsp;&emsp;&emsp;&emsp;&emsp;<a href="student_store">Continue Shopping</a>
</body>
</html>
