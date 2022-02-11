<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${cookie['lang'].value}"/>
<fmt:setBundle basename="i18n/students/students"/>

<!DOCTYPE html>
<html lang="${cookie['lang'].value}">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title><fmt:message key="label.title_students"/></title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/filter.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/table.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin/student_list.css">
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <div class="title-box">
        <div class="container">
            <div class="title-row">
                <h1><fmt:message key="label.title_students"/></h1>
            </div>
        </div>
    </div>

    <div class="filter-box">
        <div class="container">
            <div class="search-row">
                <input type="text" id="search-input" placeholder="<fmt:message key="label.search_placeholder"/>"/>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="students-block">
            <c:if test="${requestScope.students != null && !requestScope.students.isEmpty()}">
                <table>
                    <tr>
                        <th class="first-name-header"><fmt:message key="label.table_first_name"/></th>
                        <th class="last-name-header"><fmt:message key="label.table_last_name"/></th>
                        <th class="email-header"><fmt:message key="label.table_email"/></th>
                        <th class="block-header"><fmt:message key="label.table_block_status"/></th>
                    </tr>

                    <c:forEach var="student" items="${requestScope.students}">
                        <tr class="student-row">
                            <td class="first-name">
                                <a href="${pageContext.request.contextPath}/user?user_id=${student.getId()}">
                                        ${student.getFirstName()}
                                </a>
                            </td>
                            <td class="last-name">
                                <a href="${pageContext.request.contextPath}/user?user_id=${student.getId()}">
                                        ${student.getLastName()}
                                </a>
                            </td>
                            <td class="email">
                                <a href="${pageContext.request.contextPath}/user?user_id=${student.getId()}">
                                        ${student.getEmail()}
                                </a>
                            </td>
                            <td class="block">
                                <c:if test="${student.isBlocked()}">
                                    <button class="block-button">
                                        <a href="${pageContext.request.contextPath}/admin/unblock?student_id=${student.getId()}">
                                            <fmt:message key="label.button_unblock"/>
                                        </a>
                                    </button>
                                </c:if>
                                <c:if test="${!student.isBlocked()}">
                                    <button class="block-button">
                                        <a href="${pageContext.request.contextPath}/admin/block?student_id=${student.getId()}">
                                            <fmt:message key="label.button_block"/>
                                        </a>
                                    </button>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>

            <c:if test="${requestScope.students == null || requestScope.students.isEmpty()}}">
                <div class="no-students">
                    #
                </div>
            </c:if>
        </div>
    </div>
</main>

<footer>
    <div class="container"></div>
</footer>

<script src="${pageContext.request.contextPath}/static/js/admin/student_list.js"></script>
</body>
</html>