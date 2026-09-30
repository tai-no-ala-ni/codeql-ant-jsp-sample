package com.example.fw;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * *.do を受けて /WEB-INF/actions.properties の定義に従い Action を呼び出す。
 * 例: /user/search.do=com.example.app.action.UserSearchAction
 */
public class FrontControllerServlet extends HttpServlet {
    private static final String FORWARD_PREFIX = "forward:";
    private final Properties actions = new Properties();

    @Override
    public void init() throws ServletException {
        try (InputStream in = getServletContext().getResourceAsStream("/WEB-INF/actions.properties")) {
            if (in == null) {
                throw new ServletException("/WEB-INF/actions.properties not found");
            }
            actions.load(in);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String className = actions.getProperty(req.getServletPath());
        if (className == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            Action action = (Action) Class.forName(className).getDeclaredConstructor().newInstance();
            String result = action.execute(new ActionContext(req, resp, getServletContext()));
            if (result != null && result.startsWith(FORWARD_PREFIX)) {
                req.getRequestDispatcher(result.substring(FORWARD_PREFIX.length())).forward(req, resp);
            }
        } catch (ServletException | IOException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
