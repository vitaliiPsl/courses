<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.example.courses.persistence.entity.User" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<% List<String> images = Arrays.asList("/static/img/coffee2.jpeg", "/static/img/python.jpeg", "/static/img/france.jpeg");%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Title</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/filter.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/table.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/admin/student_list.css">
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <%
        List<User> studentList = (List<User>) request.getAttribute("students");
    %>

    <div class="title-box">
        <div class="container">
            <div class="title-row">
                <h1>Students</h1>
            </div>
        </div>
    </div>

    <div class="filter-box">
        <div class="container">
            <div class="search-row">
                <input type="text" id="search-input" placeholder="Find by first name, last name or email"/>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="students-block">
            <%
                if (!studentList.isEmpty()) {
            %>
            <table class="students-table">
                <tr>
                    <th class="first-name-header">First Name</th>
                    <th class="last-name-header">Last Name</th>
                    <th class="email-header">Email</th>
                    <th class="block-header">Block</th>
                </tr>

                <%
                    for (User student : studentList) {
                %>
                <tr class="student-row">
                    <td class="first-name">
                        <%= student.getFirstName()%>
                    </td>
                    <td class="last-name">
                        <%= student.getLastName()%>
                    </td>
                    <td class="email">
                        <%= student.getEmail()%>
                    </td>
                    <td class="block">
                        <%
                            if (student.isBlocked()) {
                        %>
                        <button class="block-button">
                            <a href="${pageContext.request.contextPath}/admin/unblock?student_id=<%=student.getId()%>">Unblock</a>
                        </button>
                        <%
                        } else {
                        %>
                        <button class="block-button">
                            <a href="${pageContext.request.contextPath}/admin/block?student_id=<%=student.getId()%>">Block</a>
                        </button>
                        <%
                            }
                        %>
                    </td>
                </tr>
                <%
                    }
                %>
            </table>
            <%
            } else {
            %>
            <div class="no-students">
                <h3>There are no students</h3>
            </div>
            <%
                }
            %>
        </div>
    </div>
</main>

<footer>
    <div class="container"></div>
</footer>

<script src="${pageContext.request.contextPath}/static/js/admin/student_list.js"></script>
</body>
</html>