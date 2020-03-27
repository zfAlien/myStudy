package com.juquren.trace.filter;

import com.juquren.trace.content.TraceLogContext;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.RequestContextFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;

public class RequestDigestLogFilter extends TraceLogContext implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(StringUtils.isEmpty(CONTEXT_HOLDER.get())) {
            String s = UUID.randomUUID().toString();
            CONTEXT_HOLDER.set(s);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
