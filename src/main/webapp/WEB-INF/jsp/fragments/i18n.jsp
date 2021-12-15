<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<i18n class="i18n">
    <script type="text/javascript">
        const i18n = [];
        <c:forEach var="key" items='<%=new String[]{"common.deleted","common.saved","common.errorStatus","common.confirm"}%>'>
        i18n["${key}"] = "<spring:message code="${key}"/>";
        </c:forEach>
    </script>
</i18n>
