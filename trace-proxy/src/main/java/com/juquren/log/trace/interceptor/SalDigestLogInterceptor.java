package com.juquren.log.trace.interceptor;

import com.juquren.log.trace.content.TraceLogContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class SalDigestLogInterceptor extends TraceLogContext implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //生成dubbo的traceId
        dubboTraceId();
        System.out.println("我tm的看看怎么调用的" + CONTEXT_HOLDER.get());
        Object result = invocation.proceed();
        return result;
    }
}
