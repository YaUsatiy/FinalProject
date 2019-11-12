<jsp:useBean id="client" scope="session" type="by.epam.fitness.entity.User"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="fitnessTag" prefix="fitness" %>

<fmt:setLocale value="${sessionScope.local}" scope="session"/>
<fmt:setBundle basename="locale.pagecontent" var="locale"/>

<fmt:message bundle="${locale}" key="card_pattern_error" var="card_pattern_error"/>
<fmt:message bundle="${locale}" key="card_placeholder" var="card_placeholder"/>

<html>
<head>
    <script src="${pageContext.request.contextPath}/script/orderCostSetter.js"></script>
    <script src="${pageContext.request.contextPath}/script/validation/orderValidation.js"></script>
    <title>«Olympia» Fitness Centre</title>
</head>
<body>
<jsp:include page="../menu.jsp">
    <jsp:param name="pageTopic" value="gym_membership"/>
    <jsp:param name="currentPage" value="orderPage"/>
</jsp:include>

    <div class="col-1">
        <label for="period_cost">how long</label>
    </div>
    <div class="col-2">
        <select id="period_cost" onchange="setCost()" name="period">
            <fitness:prices/>
        </select>
    </div>

    <div class="col-1">
        <label for="cost">money</label>
    </div>
    <div class="col-2">
        <input type="text" id="cost" name="cost" value="0$" readonly>
    </div>

    <div class="col-1">
        <label for="personal_discount">discount (%)</label>
    </div>
    <div class="col-2">
        <input type="text" id="personal_discount" name="personal_discount" value="${client.personalDiscount}" readonly>
    </div>

    <div class="col-1">
        <label for="final_cost">final cost</label>
    </div>
    <div class="col-2">
        <input type="text" id="final_cost" name="final_cost" value="0.0$" readonly>
    </div>
    <br/>

    <form name="form" action="${pageContext.servletContext.contextPath}/controller?command=update_gym_membership" method="post">
        <input type="hidden" id="period" name="period">
        <label for="cardNumber">credit card</label>
        <input onchange="checkCardNumber()" type="text" id="cardNumber" name="cardNumber" placeholder="${card_placeholder}" title="${card_pattern_error}">
        <label for="finalCostModalWindow">final cost (2)</label>
        <input id="finalCostModalWindow" name="cost" type="text" readonly>
        <c:choose>
            <c:when test="${not empty requestScope.wrongCard}">
                ${card_pattern_error}
            </c:when>
        </c:choose>
        <input onclick="checkOffer()" class="button" type="submit" value="buy">
    </form>

</body>
</html>