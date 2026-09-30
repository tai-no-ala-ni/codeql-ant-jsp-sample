<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>CodeQL サンプル</title></head>
<body>
<h1>CodeQL サンプル（Ant + 独自FW + JSP）</h1>
<ul>
    <li><form action="user/search.do">ユーザー検索（SQLi）: <input name="name"><button>検索</button></form></li>
    <li><form action="user/safeSearch.do">ユーザー検索（安全）: <input name="name"><button>検索</button></form></li>
    <li><form action="file/download.do">ダウンロード（パストラバーサル）: <input name="file"><button>DL</button></form></li>
    <li><form action="echo.do">エコー（XSS）: <input name="msg"><button>送信</button></form></li>
    <li><form action="search.jsp">JSP 検索（JSP 内の XSS / SQLi）: <input name="q"><button>検索</button></form></li>
</ul>
</body>
</html>
