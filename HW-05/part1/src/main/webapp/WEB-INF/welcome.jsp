<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome <%= session.getAttribute("username") %></title>
</head>
<body>
    <h1>Welcome <%= session.getAttribute("username") %></h1>
</body>
</html>
