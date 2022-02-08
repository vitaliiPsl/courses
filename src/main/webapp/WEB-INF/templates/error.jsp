<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<body>

<c:if test="${requestScope.error != null}">
    <div class="container">
        <div class="error">
                ${requestScope.error}
        </div>
    </div>
</c:if>

</body>
</html>
