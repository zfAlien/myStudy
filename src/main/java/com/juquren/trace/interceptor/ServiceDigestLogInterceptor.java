package com.juquren.trace.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ServiceDigestLogInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("我tm的看看怎么调用的");
        Object result = invocation.proceed();
        return result;
    }
}
