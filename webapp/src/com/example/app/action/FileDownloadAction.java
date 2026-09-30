package com.example.app.action;

import com.example.fw.Action;
import com.example.fw.ActionContext;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * 【脆弱性サンプル】パストラバーサル (CodeQL: java/path-injection)
 * file=../../WEB-INF/web.xml のような入力で任意ファイルを読める。
 */
public class FileDownloadAction implements Action {
    @Override
    public String execute(ActionContext ctx) throws Exception {
        File baseDir = new File(ctx.servletContext().getRealPath("/files"));
        File target = new File(baseDir, ctx.param("file"));
        ctx.response().setContentType("application/octet-stream");
        try (OutputStream out = ctx.response().getOutputStream()) {
            Files.copy(target.toPath(), out);
        }
        return null;
    }
}
