<%@ page import="at.sheldor5.tr.web.authentication.Login" %>
<%
    if (session.getAttribute("user") == null) {
      response.sendRedirect(Login.HTML_LOGIN);
    }
%>
<%@include file="header.html"%>
<h1>Exporter</h1>
<%@include file="footer.html"%>