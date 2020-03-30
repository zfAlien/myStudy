package com.juquren.log.trace.filter;


import com.juquren.dubbo.content.TraceLogContext;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class RequestDigestLogFilter extends TraceLogContext implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //生成http的traceId
        httpTraceId();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            clearMDC();
        }
    }

    @Override
    public void destroy() {

    }
}