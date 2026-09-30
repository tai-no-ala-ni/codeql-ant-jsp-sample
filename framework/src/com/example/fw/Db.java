package com.example.fw;

import jakarta.servlet.ServletContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** web.xml の context-param（db.url / db.user / db.password）から接続を取得する。 */
public final class Db {
    private Db() {
    }

    public static Connection connect(ServletContext sc) throws SQLException {
        return DriverManager.getConnection(
                sc.getInitParameter("db.url"),
                sc.getInitParameter("db.user"),
                sc.getInitParameter("db.password"));
    }
}
