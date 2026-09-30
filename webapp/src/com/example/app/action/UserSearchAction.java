package com.example.app.action;

import com.example.fw.Action;
import com.example.fw.ActionContext;
import com.example.fw.Db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 【脆弱性サンプル】SQL インジェクション (CodeQL: java/sql-injection)
 * 入力値を文字列連結で SQL に埋め込んでいる。
 */
public class UserSearchAction implements Action {
    @Override
    public String execute(ActionContext ctx) throws Exception {
        String name = ctx.param("name");
        List<String> users = new ArrayList<>();
        try (Connection con = Db.connect(ctx.servletContext());
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT name FROM users WHERE name LIKE '%" + name + "%'")) {
            while (rs.next()) {
                users.add(rs.getString("name"));
            }
        }
        ctx.setAttribute("users", users);
        return "forward:/WEB-INF/jsp/userList.jsp";
    }
}
