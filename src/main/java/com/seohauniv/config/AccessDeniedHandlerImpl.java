package com.seohauniv.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    // 권한이 없는 사용자가 접근하려는 페이지에 대한 처리
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        if (isAjax) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"message\": \"해당 페이지에 대한 권한이 없습니다.\"}");
        } else {
            // alert 메시지와 함께 인덱스 페이지로 리다이렉트
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<script>alert('해당 페이지에 대한 권한이 없습니다.'); window.location.href='/';</script>");
        }
    }
}
