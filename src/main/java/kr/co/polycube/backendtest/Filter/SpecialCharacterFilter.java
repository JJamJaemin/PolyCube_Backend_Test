package kr.co.polycube.backendtest.Filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public class SpecialCharacterFilter implements Filter {

    private String excludePatterns; //제외할 패턴

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.excludePatterns = filterConfig.getInitParameter("excludePatterns");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String url = httpRequest.getRequestURL().toString();
        String queryString = httpRequest.getQueryString();

        //제외할 패턴이 있는 경우
        if (excludePatterns != null && url.contains(excludePatterns)) {
            chain.doFilter(request, response);
            return;
        }
        //`? & = : //`를 제외한 특수문자가 포함되어 있을경우 접속을 차단
        if (queryString != null && queryString.matches(".*[^a-zA-Z0-9?&=:\\s/].*")) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write("{\"reason\": \"주소창에 특수 문자가 들어가있습니다.\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}
