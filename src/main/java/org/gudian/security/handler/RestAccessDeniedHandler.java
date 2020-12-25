package org.gudian.security.handler;

import cn.hutool.json.JSONUtil;
import org.gudian.web.result.ResultStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author GJW
 * 当访问接口没有权限时的处理
 * : 2020/12/24 9:21
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");


        PrintWriter writer = response.getWriter();
        writer.write(JSONUtil.toJsonStr(ResultStatus.NOAUTHORITY.getResponseResult()));
    }

}
