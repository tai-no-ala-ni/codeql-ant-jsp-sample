package com.example.app.action;

import com.example.fw.Action;
import com.example.fw.ActionContext;

import java.io.PrintWriter;

/**
 * 【脆弱性サンプル】クロスサイトスクリプティング (CodeQL: java/xss)
 * 入力値をエスケープせずに HTML に出力している。
 */
public class EchoAction implements Action {
    @Override
    public String execute(ActionContext ctx) throws Exception {
        PrintWriter out = ctx.writer();
        out.println("<html><body>");
        out.println("<p>入力値: " + ctx.param("msg") + "</p>");
        out.println("</body></html>");
        return null;
    }
}
