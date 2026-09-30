package com.example.fw;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * リクエスト/レスポンスをラップしたコンテキスト。
 * 業務コードは Servlet API を直接触らずにこのクラス経由で値を取得する。
 */
public class ActionContext {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ServletContext servletContext;

    public ActionContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) {
        this.request = request;
        this.response = response;
        this.servletContext = servletContext;
    }

    /** 入力パラメータ（未入力は空文字）。 */
    public String param(String name) {
        String v = request.getParameter(name);
        return v == null ? "" : v;
    }

    public void setAttribute(String name, Object value) {
        request.setAttribute(name, value);
    }

    public PrintWriter writer() throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        return response.getWriter();
    }

    public HttpServletResponse response() {
        return response;
    }

    public ServletContext servletContext() {
        return servletContext;
    }
}
