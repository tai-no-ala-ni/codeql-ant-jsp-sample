<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%
    @SuppressWarnings("unchecked")
    List<String> users = (List<String>) request.getAttribute("users");
%>
<!DOCTYPE html>
<html>
<head><title>ユーザー一覧</title></head>
<body>
<h1>ユーザー一覧（<%= users.size() %> 件）</h1>
<ul>
<% for (String u : users) { %>
    <li><%= u %></li>
<% } %>
</ul>
<a href="<%= request.getContextPath() %>/">戻る</a>
</body>
</html>
