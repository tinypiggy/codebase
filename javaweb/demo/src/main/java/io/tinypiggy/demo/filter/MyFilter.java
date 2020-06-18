package io.tinypiggy.demo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

//@WebFilter(filterName = "myFilter", urlPatterns = "/main")
//上面注释的放松 启动类添加这个 @ServletComponentScan 注解才会扫描
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("进入MyFilter");
        ServletRequest requestWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        }

        if (null == requestWrapper) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            BufferedReader reader = requestWrapper.getReader();
            String line ;
            while ((line = reader.readLine()) != null){
                System.out.println(line);
            }
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
