<%@ page import="at.sheldor5.tr.api.userMapping.User" %>
<%@include file="header.html"%>
<h1>Hallo, <%
    Object object = session.getAttribute("userMapping");
    if (object != null && object instanceof User) {
      User userMapping = (User) object;
        System.out.println(userMapping.getUsername());
        System.out.println(userMapping.getForename());
        System.out.println(userMapping.getSurname());
%>
<%=userMapping.getForename()%> <%=userMapping.getSurname()%>
    <%
    } else {
        System.out.println("null");
    }%>
</h1>
<%@include file="footer.html"%>