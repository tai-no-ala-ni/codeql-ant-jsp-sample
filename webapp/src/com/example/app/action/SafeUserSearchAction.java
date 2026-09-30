package com.example.app.action;

import com.example.fw.Action;
import com.example.fw.ActionContext;
import com.example.fw.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/** 【安全な例】PreparedStatement を使うので CodeQL は検出しない（比較用）。 */
public class SafeUserSearchAction implements Action {
    @Override
    public String execute(ActionContext ctx) throws Exception {
        List<String> users = new ArrayList<>();
        try (Connection con = Db.connect(ctx.servletContext());
             PreparedStatement ps = con.prepareStatement("SELECT name FROM users WHERE name LIKE ?")) {
            ps.setString(1, "%" + ctx.param("name") + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(rs.getString("name"));
                }
            }
        }
        ctx.setAttribute("users", users);
        return "forward:/WEB-INF/jsp/userList.jsp";
    }
}
