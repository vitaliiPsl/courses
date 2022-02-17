<%@ page import="com.example.courses.utils.TimeUtils" %>
<%@ page import="com.example.courses.persistence.entity.Role" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="i18n/course_list/course_list"/>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title><fmt:message key="label.title"/> 👀</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/filter.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course_list.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course_row.css">
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <div class="filter-box">
        <div class="container">
            <form action="${pageContext.request.contextPath}/courses">
                <div class="search-row">
                    <input type="text" id="search-input" name="query"
                           data-request-url="${pageContext.request.contextPath}/courses/search?query="
                           data-course-url="${pageContext.request.contextPath}/course?course_id="
                           placeholder="<fmt:message key="label.search.placeholder"/>" value="${requestScope.query}"
                           autocomplete="off"/>
                    <button type="submit" class="search-btn"><fmt:message key="label.search.button"/></button>
                    <div class="hints-block"></div>
                </div>
            </form>
            <div class="filter-row">
                <div class="filter-block">
                    <span class="filter-by-span"><fmt:message key="label.filter.filter_by"/></span>
                    <div class="filters">

                        <c:set var="filter" value="${requestScope.filters.get('subject')}"/>
                        <div class="filter dropdown-element">
                            <div class="dropdown-control filter-control">
                                <h5><fmt:message key="label.filter.subject_key"/></h5>
                                <div class="arrow"></div>
                            </div>
                            <div class="dropdown-menu filter-menu hidden">
                                <c:forEach var="value" items="${filter}">
                                    <c:set var="checked"
                                           value="${sessionScope.applied_filters.get('subject').contains(value)}"/>
                                    <div class="filter-option option">
                                        <form class="option-form"
                                              action="${pageContext.request.contextPath}/courses/filter" method="post">
                                            <input class="filter-checkbox" type="checkbox" name="subject"
                                                   value="${value}"
                                                ${checked ? 'checked' : ''}/>
                                            <label>${value}</label>
                                        </form>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <c:set var="filter" value="${requestScope.filters.get('teacher')}"/>
                        <div class="filter dropdown-element">
                            <div class="dropdown-control filter-control">
                                <h5><fmt:message key="label.filter.teacher_key"/></h5>
                                <div class="arrow"></div>
                            </div>
                            <div class="dropdown-menu filter-menu hidden">
                                <c:forEach var="value" items="${filter}">
                                    <c:set var="checked"
                                           value="${sessionScope.applied_filters.get('teacher').contains(value)}"/>
                                    <div class="filter-option option">
                                        <form class="option-form"
                                              action="${pageContext.request.contextPath}/courses/filter" method="post">
                                            <input class="filter-checkbox" type="checkbox" name="teacher"
                                                   value="${value}"
                                                ${checked ? 'checked' : ''}/>
                                            <label>${value}</label>
                                        </form>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                    </div>
                </div>

                <div class="sorting-block">
                    <span class="sort-by-span"><fmt:message key="label.sort.sort_by"/></span>
                    <div class="sorting">

                        <div class="filter sort dropdown-element">
                            <div class="dropdown-control filter-control sort">
                                <h5><fmt:message key="label.sort.sort_key"/></h5>
                                <div class="arrow"></div>
                            </div>
                            <div class="dropdown-menu sort-menu hidden">
                                <c:set var="appliedSorting" value="${sessionScope.sorting}"/>
                                <c:forEach var="option" items="${requestScope.sorting_options}">
                                    <c:set var="selected" value="${option.equals(appliedSorting)}"/>
                                    <div class="option sort-option ${selected ? 'selected' : ''}">
                                        <form class="option-form"
                                              action="${pageContext.request.contextPath}/courses/sort" method="post">
                                            <input class="filter-checkbox" type="checkbox"
                                                   name="sorting" value="${option}" ${selected ? 'checked' : ''} hidden>
                                            <label>${option}</label>
                                        </form>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="filter sort dropdown-element">
                            <div class="dropdown-control filter-control">
                                <h5><fmt:message key="label.sort.order_key"/></h5>
                                <div class="arrow"></div>
                            </div>
                            <div class="dropdown-menu sort-menu hidden">
                                <c:set var="appliedOrder" value="${sessionScope.sorting_order}"/>
                                <c:forEach var="option" items="${requestScope.sorting_order_options}">
                                    <c:set var="selected"
                                           value="${option.equals(appliedOrder)}"/>
                                    <div class="option sort-option ${selected ? 'selected' : ''}">
                                        <form class="option-form"
                                              action="${pageContext.request.contextPath}/courses/sort" method="post">
                                            <input class="filter-checkbox" type="checkbox" name="sorting_order"
                                                   value="${option}" ${selected ? 'checked' : ''} hidden>
                                            <label>${option}</label>
                                        </form>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="content-box">
        <div class="container">
            <c:forEach var="courseDTO" items="${requestScope.courses}">
                <c:set var="course" value="${courseDTO.getCourse()}"/>
                <c:set var="subject" value="${courseDTO.getSubject()}"/>
                <c:set var="language" value="${courseDTO.getLanguage()}"/>
                <c:set var="teacher" value="${courseDTO.getTeacher()}"/>
                <c:set var="students" value="${courseDTO.getStudents()}"/>

                <div class="course">
                    <div class="img-box">
                        <img src="${pageContext.request.contextPath}/static/images/default.jpeg" alt=""/>
                    </div>
                    <div class="info-box">
                        <div class="title-row">
                            <a href="${pageContext.request.contextPath}/course?course_id=${course.getId()}">
                                <h2>${course.getTitle()}</h2>
                            </a>
                        </div>
                        <div class="info-row">
                            <div class="info">
                                <p class="subject-row"><fmt:message key="label.course.subject"/>:</p>
                                <span class="subject">${subject.getSubject()}</span>
                            </div>
                        </div>
                        <div class="info-row">
                            <div class="info">
                                <p class="teacher-row"><fmt:message key="label.course.teacher"/>:</p>
                                <span class="teacher">${teacher.getFullName()}</span>
                            </div>
                            <div class="info">
                                <p class="language-row"><fmt:message key="label.course.language"/>:</p>
                                <span class="language">${language.getName()}</span>
                            </div>
                        </div>
                        <div class="info-row">
                            <div class="info">
                                <p class="duration-row"><fmt:message key="label.course.duration"/>:</p>
                                <span class="duration">
                                        ${TimeUtils.calculateDuration(course.getStartDate(), course.getEndDate())}
                                </span>
                            </div>
                            <div class="info">
                                <p class="number-of-students-row"><fmt:message key="label.course.students"/>:</p>
                                <span class="number_of_students">${students.size()}</span>
                            </div>
                        </div>
                        <div class="info-row">
                        </div>
                    </div>

                    <c:if test="${sessionScope.user.getRole().equals(Role.ADMIN)}">
                        <div class="manage-box">
                            <button class="manage-btn edit-btn">
                                <a href="${pageContext.request.contextPath}/admin/course/edit?course_id=${course.getId()}">
                                    <fmt:message key="label.manage_box.edit"/>
                                </a>
                            </button>
                            <button class="manage-btn delete-btn">
                                <a href="${pageContext.request.contextPath}/admin/course/delete?course_id=${course.getId()}">
                                    <fmt:message key="label.manage_box.delete"/>
                                </a>
                            </button>
                        </div>
                    </c:if>
                </div>
            </c:forEach>

            <div class="pages-box">
                <c:set var="page" value="${requestScope.page}"/>
                <c:forEach var="page_number" begin="1" end="${requestScope.number_of_pages}">
                    <div class="page-number ${page == page_number ? 'selected' : ''}">
                        <a href="${pageContext.request.contextPath}/courses?page=${page_number}">
                            <h5>${page_number}</h5>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</main>

<footer>
    <div class="container"></div>
</footer>

<script src="${pageContext.request.contextPath}/static/js/course_list.js"></script>
<script src="${pageContext.request.contextPath}/static/js/course_list_filter.js"></script>
<script src="${pageContext.request.contextPath}/static/js/course_list_sorting.js"></script>
<script src="${pageContext.request.contextPath}/static/js/course_search.js"></script>
</body>
</html>