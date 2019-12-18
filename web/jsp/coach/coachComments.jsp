<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${sessionScope.local}" scope="session"/>
<fmt:setBundle basename="locale.pagecontent" var="locale"/>

<fmt:message bundle="${locale}" key="no_comments" var="no_comments"/>
<fmt:message bundle="${locale}" key="coach_no_comments" var="coach_no_comments"/>
<fmt:message bundle="${locale}" key="client_no_image" var="client_no_image"/>
<fmt:message bundle="${locale}" key="footer.copyright" var="footer"/>

<html>
<head>
    <link rel="shortcut icon" href="img/favicon/1.ico"/>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <title>Comments About Me</title>
</head>
<body class="d-flex flex-column">
<c:choose>
    <c:when test="${sessionScope.role eq 'coach'}">
        <jsp:include page="../menu.jsp">
            <jsp:param name="pageTopic" value="coachComments"/>
            <jsp:param name="currentPage" value="coach_comments"/>
        </jsp:include>
    </c:when>
    <c:otherwise>
        <jsp:include page="../menu.jsp">
            <jsp:param name="pageTopic" value="comments_about_coach"/>
            <jsp:param name="currentPage" value="coach_comments"/>
        </jsp:include>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${fn:length(comments) eq 0}">
        <c:choose>
            <c:when test="${sessionScope.role eq 'coach'}">
                <h3><c:out value="${no_comments}"/></h3>
            </c:when>
            <c:otherwise>
                <h3><c:out value="${coach_no_comments}"/></h3>
            </c:otherwise>
        </c:choose>
    </c:when>

    <c:otherwise>
        <c:forEach items="${comments}" var="comment">
            <div class="media border p-3">
                <c:choose>
                    <c:when test="${not empty comment.value.image}">
                        <img src="data:image/jpg;base64,${comment.value.image}" alt="${client_no_image}" class="mr-3 mt-3 rounded-circle" style="width:60px;">
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-user-circle mr-3 mt-3" style="font-size: 55px"></i>
                    </c:otherwise>
                </c:choose>
                <div class="media-body">
                    <h4><c:out value="${comment.value.name} ${comment.value.surname} (login : ${comment.value.login}) "/>
                        <small><i>
                            <c:choose>
                                <c:when test="${sessionScope.local eq 'en_US'}">
                                    <fmt:formatDate value="${comment.key.paymentData}" pattern="dd-MM-YYYY HH:mm:ss" />
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatDate value="${comment.key.paymentData}" pattern="dd.MM.YYYY HH:mm:ss" />
                                </c:otherwise>
                        </c:choose>
                        </i></small>
                    </h4>
                    <p><c:out value="${comment.key.commentContent}"/></p>
                </div>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>

<footer class="footer mt-auto py-3">
    <div class="container text-center">
        <span class="text-muted">${footer}</span>
    </div>
</footer>
</body>
</html>
