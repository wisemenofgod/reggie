package com.wisemenofgod.reggie.filter;
/*
    com.wisemenofgod
    2022-05-29-15:34
*/


import com.alibaba.fastjson.JSON;
import com.wisemenofgod.reggie.common.BaseContext;
import com.wisemenofgod.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@SuppressWarnings("all")
/**
 * 检查用户是否是已经登录了
 */
@Slf4j
@Component
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();

        String [] urls = {
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
            "/user/login",
            "/user/sendMsg"
        };

        boolean check = check(urls, uri);

        if (check){
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession();
        if (session.getAttribute("employee")!=null){
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }
        if (session.getAttribute("user")!=null){
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("user"));
            filterChain.doFilter(request, response);
            return;
        }

        log.info("拦截到了请求： {}",request.getRequestURI());
        response.getWriter().write(JSON.toJSONString(R.error("notlogin")));
        return;
    }

    public boolean check (String[] urls , String uri){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, uri);
            if (match){
                return true;
            }
        }
        return false;
    }
}
