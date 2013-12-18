<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>No Access</title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" >
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/imdi_browser.css" >

<SCRIPT LANGUAGE="JavaScript" >
<!--
parent.header.location.href="<%= request.getContextPath() %>/imdi_browser_header.jsp";
//-->
</SCRIPT>
</head>

<body>
        <table border="0" cellspacing="2" cellpadding="0" height="300" align="center">
            <tr>
                <td width="400" align="center"><img src="<%= request.getContextPath() %>/lana_big.gif" alt="" width="224" height="100" border="0"></td>
            </tr>
            <tr>
                <td width="400" align="center">Sorry - Access Denied!</td>
            </tr>
            <tr>
                <td width="400" align="center">You have requested a protected resource you have no access to.</td>
            </tr>
        </table>
</body>
</html>
