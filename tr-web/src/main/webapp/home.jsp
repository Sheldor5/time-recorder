<%@ page import="at.sheldor5.tr.api.user.User" %>
<%@include file="header.html"%>
<h1>Hallo, <%
    Object object = session.getAttribute("user");
    if (object != null && object instanceof User) {
      User user = (User) object;
        System.out.println(user.getUsername());
        System.out.println(user.getForename());
        System.out.println(user.getSurname());
%>
<%=user.getForename()%> <%=user.getSurname()%>
    <%
    } else {
        System.out.println("null");
    }%>
</h1>
<%@include file="footer.html"%>