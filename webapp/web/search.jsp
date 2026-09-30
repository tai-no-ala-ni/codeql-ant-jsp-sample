<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*, com.example.fw.Db" %>
<%--
  【脆弱性サンプル】レガシーによくある「JSP にロジックを直書き」パターン。
   - 入力値をそのまま出力（XSS）
   - 入力値を連結して SQL 実行（SQL インジェクション）
  JspC で Java に変換してからコンパイルすることで CodeQL の解析対象になる。
--%>
<%
    request.setCharacterEncoding("UTF-8");
    String q = request.getParameter("q");
    if (q == null) {
        q = "";
    }
%>
<!DOCTYPE html>
<html>
<head><title>JSP 検索</title></head>
<body>
<h1>検索キーワード: <%= q %></h1>
<ul>
<%
    try (Connection con = Db.connect(application);
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery("SELECT title FROM items WHERE title LIKE '%" + q + "%'")) {
        while (rs.next()) {
%>
    <li><%= rs.getString("title") %></li>
<%
        }
    } catch (SQLException e) {
        out.println("<li>DB エラー: " + e.getMessage() + "</li>");
    }
%>
</ul>
</body>
</html>
