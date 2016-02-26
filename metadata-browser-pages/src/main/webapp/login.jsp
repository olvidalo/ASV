<%--
    Document   : login.jsp
    Created on : Jan 13, 2014, 2:10:32 PM
    Author     : Jean-Charles Ferrières <jean-charles.ferrieres@mpi.nl>
--%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String userid = request.getRemoteUser();
    if( userid == null || userid.length() == 0 ){
       userid = (String) session.getAttribute("userid");
      if( userid == null || userid.trim().equals("")) userid = "anonymous";
    }
%>
<html>
  <head>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" charset="iso-8859-1" href="<%= request.getContextPath() %>/style/style.css">
    <meta http-equiv="refresh" content="1; url=<%= request.getContextPath() %>?<%= 
        request.getQueryString() == null ? "" 
                : request.getQueryString().replaceAll("(login=[^?&]+)", "") 
    %>" />
  </head>
  <body>
        <table border="0" cellspacing="2" cellpadding="0" height="300" align="center">
            <tr>
                <td width="400" align="center"><img src="<%= request.getContextPath() %>/lana_big.gif" alt="" width="224" height="100" border="0"></td>
            </tr>
            <tr>
                <td width="400" align="center">You are now logged in as <%=userid%> </td>
            </tr>
        </table>
    </body>
</html>

