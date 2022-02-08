<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/error_page/error_page.css">
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>
    <main>
        <div class="container">
                <span class="code">403</span>
                <h1 class="message">You don't have access to this resource</h1>
                <a href="${pageContext.request.contextPath}/" class="home_link">Home page</a>
        </div>
    </main>
</body>
</html>
